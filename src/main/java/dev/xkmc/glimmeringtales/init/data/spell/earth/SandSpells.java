package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.processor.SetDeltaProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.MovePosMotion;
import dev.xkmc.l2magic.content.particle.engine.CustomParticleInstance;
import dev.xkmc.l2magic.content.particle.engine.DustParticleData;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class SandSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH
			.build(GlimmeringTales.loc("sand")).focusAndCost(60, 240)
			.damageCustom(s -> new DamageType(s, 0.1f),
					"%s is buried by sandstorm", "%s is buried by %s's sandstorm",
					DamageTypeTags.IS_PROJECTILE)
			.block(SandSpells::gen, GTItems.RUNE_SAND, RuneBlock::of,
					(b, e) -> b.add(BlockTags.SAND, BlockSpell.of(e)))
			.lang("Sandstorm").desc(
					"[Block] Create sandstorm trapping enemies",
					"Create a sand tornado, trapping enemies touched, dealing %s, and inflict %s",
					SpellTooltipData.damageAndEffect()
			);

	private static final DoubleVariable DMG = DoubleVariable.of("4");

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		double vsp = 0.5;
		int life = 20;
		double rate = Math.tan(10 * Mth.DEG_TO_RAD);
		double w = vsp * 180 / Math.PI / 2;
		double ir = 0.7;
		String radius = ir + "+TickCount*" + vsp * rate;
		String angle = w / (vsp * rate) + "*(log(" + radius + ")+log(" + ir + "))";

		var damage = new ProcessorEngine(SelectionType.ENEMY,
				new ApproxCylinderSelector(
						DoubleVariable.of("1"),
						DoubleVariable.of("6")
				),
				List.of(
						new DamageProcessor(ctx.damage(), DMG, true, true),
						SetDeltaProcessor.ZERO,
						new PushProcessor(
								DoubleVariable.of("-0.05"),
								DoubleVariable.ZERO,
								DoubleVariable.ZERO,
								PushProcessor.Type.HORIZONTAL
						),
						new EffectProcessor(
								MobEffects.MOVEMENT_SLOWDOWN,
								IntVariable.of("100"),
								IntVariable.of("2"),
								false, false
						)
				));

		var particle = new CustomParticleInstance(
				DoubleVariable.of("0"),
				DoubleVariable.of("0.15"),
				IntVariable.of("" + life),
				true,
				new MovePosMotion(List.of(
						ForwardOffsetModifier.of("-" + ir),
						RotationModifier.of(angle),
						ForwardOffsetModifier.of(radius),
						new Normal2DirModifier(),
						ForwardOffsetModifier.of("TickCount*" + vsp)
				)),
				new DustParticleData(
						RenderTypePreset.NORMAL,
						ColorVariable.Static.of(14406560)
				)
		).move(NormalOffsetModifier.of("rand(" + (-vsp) + "," + vsp + ")"));

		var tick = new ListLogic(List.of(

				damage,
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("1"),
						new RandomVariableLogic("r", 1,
								new RingRandomIterator(
										DoubleVariable.of(ir + ""),
										DoubleVariable.of(ir + ""),
										DoubleVariable.of("-180"),
										DoubleVariable.of("180"),
										IntVariable.of("3"),
										particle, null
								).move(new Dir2NormalModifier())
						), null
				)
		));
		return new ListLogic(List.of(
				new DelayedIterator(IntVariable.of("30"), IntVariable.of("2"),
						new SoundInstance(
								SoundEvents.BREEZE_IDLE_GROUND,
								DoubleVariable.of("1"),
								DoubleVariable.of("1+rand(-0.5,0.2)+rand(-0.5,0.2)")
						), null),
				new DelayedIterator(IntVariable.of("90"), IntVariable.of("1"), tick, null)
						.move(OffsetModifier.ABOVE)
		));

	}


}
