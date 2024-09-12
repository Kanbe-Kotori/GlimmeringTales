package dev.xkmc.glimmeringtales.init.data.spell.life;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.content.engine.render.OrientedCrossRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;

public class CactusSpell {
	public static final NatureSpellBuilder BUILDER = GTRegistries.LIFE
			.build(GlimmeringTales.loc("cactus")).focusAndCost(20, 60)
			.damageCustom(msg -> new DamageType(msg, 1f),
					"%s is pierced by cactus thorn",
					"%s is pierced by %s with cactus thorn",
					DamageTypeTags.IS_PROJECTILE)
			.projectile(CactusSpell::proj)
			.block(CactusSpell::gen, GTItems.RUNE_CACTUS, RuneBlock::offset,
					(b, e) -> b.add(Blocks.CACTUS, BlockSpell.cost(e)))
			.lang("Cactus").desc(
					"[Block] Splash cactus spikes",
					"Shoot cactus spikes forming a circle, dealing %s ",
					SpellTooltipData.damage()
			);

	public static final ResourceLocation TEX = GlimmeringTales.loc("textures/spell/cactus.png");
	private static final DoubleVariable DMG = DoubleVariable.of("1");


	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		int theta = 120;
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.WIND_CHARGE_BURST.value(),
						DoubleVariable.of("1"),
						DoubleVariable.of("5+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new LoopIterator(
						IntVariable.of("" + theta),
						new CustomProjectileShoot(
								DoubleVariable.of("1"),
								ctx.proj,
								IntVariable.of("rand(8,12)"),
								false, true,
								Map.of()
						).move(new RotationModifier(
								DoubleVariable.of(360 / theta + "*j"),
								DoubleVariable.ZERO
						)), "j"
				).move(
						OffsetModifier.of("0", "0.55", "0"),
						SetDirectionModifier.of("1", "0", "0")
				)));

	}

	private static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.tick(new SimpleParticleInstance(
						ParticleTypes.CRIT,
						DoubleVariable.of("rand(" + 0.5 + "," + 1 + ")")
				).move(ForwardOffsetModifier.of("rand(-0.2,-0.1)")))
				.hit(new DamageProcessor(ctx.damage(), DMG, true, true))
				.size(DoubleVariable.of("0.25"))
				.motion(SimpleMotion.ZERO)
				.renderer(new OrientedCrossRenderData(TEX))
				.build();
	}
}
