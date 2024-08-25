package dev.xkmc.glimmeringtales.init.data.spell.snow;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.block.SetBlock;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.predicate.AndPredicate;
import dev.xkmc.l2magic.content.engine.predicate.BlockMatchCondition;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.predicate.OrPredicate;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxBallSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class IceSpells {

	private static final DoubleVariable ICE_DMG = DoubleVariable.of("1");
	private static final IntVariable ICE_DUR = IntVariable.of("100");

	private static final DoubleVariable PACK_DMG = DoubleVariable.of("4");
	private static final IntVariable PACK_DUR = IntVariable.of("200");

	private static final DoubleVariable BLUE_DMG = DoubleVariable.of("6");
	private static final IntVariable BLUE_DUR = IntVariable.of("300");

	public static final NatureSpellBuilder ICE = GTRegistries.SNOW.get()
			.build(GlimmeringTales.loc("ice")).cost(40).damageFreeze()
			.spell(ctx -> NatureSpellEntry.ofBlock(gen(ctx, ICE_DMG, ICE_DUR), GTItems.RUNE_ICE, 1040))
			.block((b, e) -> b.add(Blocks.ICE, BlockSpell.liquid(e)))
			.block((b, e) -> b.add(Blocks.FROSTED_ICE, BlockSpell.liquid(e)))
			.lang("Freeze").desc(
					"[Block] Freeze nearby water and entity",
					"Freeze water / flowing water into frost ice, then inflict %s and %s to enemies in range",
					SpellTooltipData.damageAndEffect()
			);

	public static final NatureSpellBuilder PACK_ICE = GTRegistries.SNOW.get()
			.build(GlimmeringTales.loc("packed_ice")).cost(60).damageFreeze()
			.spell(ctx -> NatureSpellEntry.ofBlock(gen(ctx, PACK_DMG, PACK_DUR), GTItems.RUNE_PACKED_ICE, 1040))
			.block((b, e) -> b.add(Blocks.PACKED_ICE, BlockSpell.liquid(e)))
			.lang("Freeze II").desc(
					"[Block] Freeze nearby water and entity",
					"Freeze water / flowing water into frost ice, then inflict %s and %s to enemies in range",
					SpellTooltipData.damageAndEffect()
			);

	public static final NatureSpellBuilder BLUE_ICE = GTRegistries.SNOW.get()
			.build(GlimmeringTales.loc("blue_ice")).cost(80).damageFreeze()
			.spell(ctx -> NatureSpellEntry.ofBlock(gen(ctx, BLUE_DMG, BLUE_DUR), GTItems.RUNE_BLUE_ICE, 1040))
			.block((b, e) -> b.add(Blocks.BLUE_ICE, BlockSpell.liquid(e)))
			.lang("Freeze III").desc(
					"[Block] Freeze nearby water and entity",
					"Freeze water / flowing water into frost ice, then inflict %s and %s to enemies in range",
					SpellTooltipData.damageAndEffect()
			);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx, DoubleVariable dmg, IntVariable dur) {
		var range = "5";
		return new ListLogic(List.of(
				new DelayedIterator(
						IntVariable.of(range),
						IntVariable.of("2"),
						new ProcessorEngine(
								SelectionType.ENEMY_NO_FAMILY,
								new ApproxBallSelector(DoubleVariable.of("i+1")),
								List.of(
										new DamageProcessor(ctx.damage(), dmg, true, false),
										new EffectProcessor(
												LCEffects.ICE, dur,
												IntVariable.of("0"),
												false, false
										)
								)
						), "i"
				),
				new SetBlock(Blocks.FROSTED_ICE.defaultBlockState()).circular(
						DoubleVariable.of(range), DoubleVariable.of("2"), false, null,
						new OrPredicate(List.of(
								BlockMatchCondition.of(Blocks.FROSTED_ICE),
								new AndPredicate(List.of(
										BlockMatchCondition.of(Blocks.WATER),
										BlockMatchCondition.of(Blocks.WATER).invert().move(OffsetModifier.ABOVE),
										BlockTestCondition.Type.REPLACEABLE.get().move(OffsetModifier.ABOVE)
								))
						))
				),
				new LoopIterator(
						IntVariable.of("60"),
						new SimpleParticleInstance(
								ParticleTypes.SNOWFLAKE,
								DoubleVariable.of("0.5")
						).move(
								SetDirectionModifier.of("1", "0", "0"),
								RotationModifier.of("i*6", "10")
						), "i"
				).move(OffsetModifier.ABOVE)
		));
	}


}
