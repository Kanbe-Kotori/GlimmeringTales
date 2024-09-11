package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.content.engine.render.VerticalRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.predicate.BlockMatchCondition;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.predicate.OrPredicate;
import dev.xkmc.l2magic.content.engine.predicate.SurfaceBelowCondition;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;

public class DripstoneSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH
			.build(GlimmeringTales.loc("dripstone")).focusAndCost(40, 160)
			.damageCustom(e -> new DamageType(e, 0.1f),
					"%s is pierced by stalagmite", "%s is pierced by %s's stalagmite",
					DamageTypeTags.IS_PROJECTILE)
			.projectile(DripstoneSpells::proj)
			.block(DripstoneSpells::gen, GTItems.RUNE_DRIPSTONE, RuneBlock::of,
					(b, e) -> b.add(Blocks.DRIPSTONE_BLOCK, BlockSpell.of(e)),
					(b, e) -> b.add(Blocks.POINTED_DRIPSTONE, BlockSpell.cost(e))
			).lang("Stalactite Burst").desc(
					"[Block] Shoot stalagmite spikes from ground",
					"Shoot stalagmite spikes from ground to pierce entities, dealing %s and inflict %s",
					SpellTooltipData.damageAndEffect()
			);

	public static final ResourceLocation TEX = GlimmeringTales.loc("textures/spell/pointed_dripstone_up_tip.png");
	public static final DoubleVariable DMG = DoubleVariable.of("4");

	private static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.tick(new DustParticleInstance(
						ColorVariable.Static.of(0x836356),
						DoubleVariable.of("0.5"),
						DoubleVariable.ZERO,
						IntVariable.of("20")
				).move(OffsetModifier.of("0", "-0.2", "0")))
				.hit(new DamageProcessor(
						ctx.damage(), DMG,
						true,
						true
				)).hit(new EffectProcessor(
						MobEffects.MOVEMENT_SLOWDOWN,
						IntVariable.of("100"),
						IntVariable.of("2"),
						false,
						true
				)).hit(new PushProcessor(
						DoubleVariable.of("1"),
						DoubleVariable.ZERO,
						DoubleVariable.ZERO,
						PushProcessor.Type.UNIFORM
				)).size(DoubleVariable.of("0.25"))
				.renderer(new VerticalRenderData(TEX))
				.build();
	}

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.POINTED_DRIPSTONE_LAND,
						DoubleVariable.of("1"),
						DoubleVariable.of("rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new CustomProjectileShoot(
						DoubleVariable.of("0.8"),
						ctx.proj,
						IntVariable.of("20"),
						false, true,
						Map.of()
				).move(OffsetModifier.of("0", "-0.45", "0"),
						SetDirectionModifier.UP).circular(
						DoubleVariable.of("2"),
						DoubleVariable.of("2"),
						false, null,
						new OrPredicate(List.of(
								SurfaceBelowCondition.full(),
								BlockMatchCondition.of(Blocks.POINTED_DRIPSTONE)
										.move(OffsetModifier.BELOW)
						)),
						BlockTestCondition.Type.BLOCKS_MOTION.get().invert()
				)));

	}

}
