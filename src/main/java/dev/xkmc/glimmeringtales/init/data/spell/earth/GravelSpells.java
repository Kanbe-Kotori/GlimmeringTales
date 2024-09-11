package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.SetDeltaProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.MovePosMotion;
import dev.xkmc.l2magic.content.particle.engine.*;
import dev.xkmc.l2magic.content.particle.render.SpriteGeom;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.function.BiFunction;

public class GravelSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH
			.build(GlimmeringTales.loc("gravel")).focusAndCost(50, 150)
			.damageCustom(s -> new DamageType(s, 0.1f),
					"%s is scratched to death by flint", "%s is scratched to death by %s with flint",
					DamageTypeTags.IS_PROJECTILE)
			.block(GravelSpells::gen, GTItems.RUNE_GRAVEL, RuneBlock::of,
					(b, e) -> b.add(Tags.Blocks.GRAVELS, BlockSpell.of(e)))
			.lang("Flint Storm").desc(
					"[Block] Create flint storm",
					"Create a flint storm, dealing %s, and inflict %s",
					SpellTooltipData.damageAndEffect()
			);

	private static final DoubleVariable DMG = DoubleVariable.of("4");

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		double vsp = 0.2;
		int life = 20;
		double rate = Math.tan(10 * Mth.DEG_TO_RAD);
		double w = vsp * 180 / Math.PI / 2 * 6;
		double ir = 3;
		String radius = "i_radius+TickCount*" + vsp * rate;
		String angle = w / (vsp * rate) + "*(log(" + radius + ")-log(i_radius))";

		var damage = new ProcessorEngine(SelectionType.ENEMY,
				new ApproxCylinderSelector(
						DoubleVariable.of("3"),
						DoubleVariable.of("3")
				),
				List.of(
						new DamageProcessor(
								ctx.damage(), DMG,
								true, true
						),
						SetDeltaProcessor.ZERO,
						new EffectProcessor(
								LCEffects.ARMOR_REDUCE,
								IntVariable.of("100"),
								IntVariable.of("2"),
								false, false
						)
				));

		BiFunction<ParticleRenderData<?>, Double, CustomParticleInstance> pf = (r, s) -> new CustomParticleInstance(
				DoubleVariable.of("0"),
				DoubleVariable.of("" + s),
				IntVariable.of("" + life),
				true,
				new MovePosMotion(List.of(
						ForwardOffsetModifier.of("-i_radius"),
						RotationModifier.of(angle),
						ForwardOffsetModifier.of(radius),
						new Normal2DirModifier(),
						ForwardOffsetModifier.of("TickCount*" + vsp)
				)), r
		);

		var particle = new PredicateLogic(
				BooleanVariable.of("rand(0,1)>0.05"),
				pf.apply(new DustParticleData(
						RenderTypePreset.NORMAL,
						ColorVariable.Static.of(-8356741)), 0.15),
				pf.apply(new ItemParticleData(
						RenderTypePreset.BLOCK,
						Items.FLINT,
						SpriteGeom.INSTANCE), 0.1)
		);

		var tick = new DelayedIterator(
				IntVariable.of("10"),
				IntVariable.of("1"),
				new RandomVariableLogic("r", 1,
						new RingRandomIterator(
								DoubleVariable.of("0.5"),
								DoubleVariable.of("" + ir),
								DoubleVariable.of("-180"),
								DoubleVariable.of("180"),
								IntVariable.of("6"),
								particle, "i"
						).move(new Dir2NormalModifier())
				), null
		);


		return new ListLogic(List.of(
				new DelayedIterator(IntVariable.of("30"), IntVariable.of("1"),
						new SoundInstance(
								SoundEvents.BREEZE_IDLE_GROUND,
								DoubleVariable.of("1"),
								DoubleVariable.of("0.8+rand(-0.5,0.2)+rand(-0.5,0.2)")
						),

						null),

				new DelayedIterator(IntVariable.of("18"), IntVariable.of("2"), damage, null),
				new DelayedIterator(IntVariable.of("22"), IntVariable.of("2"), tick, null)
		)).move(OffsetModifier.ABOVE);
	}


}
