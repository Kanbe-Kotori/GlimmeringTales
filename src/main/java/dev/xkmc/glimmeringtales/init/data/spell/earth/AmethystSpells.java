package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.engine.processor.StackingEffectProcessor;
import dev.xkmc.glimmeringtales.content.engine.render.CrossRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.Map;

public class AmethystSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH.get()
			.build(GlimmeringTales.loc("amethyst")).cost(40)
			.damageCustom(msg -> new DamageType(msg, 0.1f),
					"%s is pierced by amethyst shards",
					"%s is pierced by %s with amethyst shards",
					DamageTypeTags.IS_PROJECTILE)
			.projectile(AmethystSpells::proj)
			.spell(ctx -> NatureSpellEntry.ofBlock(gen(ctx), GTItems.RUNE_AMETHYST, 1030))
			.block((b, e) -> b.add(GTTagGen.AMETHYST, BlockSpell.cost(e)))
			.lang("Scattering Amethyst").desc(
					"[Block] Splash amethyst shards",
					"Create a semisphere of amethyst shards, dealing %s and stack %s",
					SpellTooltipData.of(EngineRegistry.DAMAGE, GTEngine.EP_STACK)
			);

	private static final ResourceLocation TEX = GlimmeringTales.loc("textures/spell/amethyst.png");
	private static final DoubleVariable DMG = DoubleVariable.of("4");

	public static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.tick(new DustParticleInstance(
						ColorVariable.Static.of(0xCFA0F3),
						DoubleVariable.of("0.5"),
						DoubleVariable.ZERO,
						IntVariable.of("20")
				).move(ForwardOffsetModifier.of("-0.2")))
				.hit(new DamageProcessor(
						ctx.damage(), DMG, true, true
				)).hit(new StackingEffectProcessor(
						LCEffects.BLEED,
						IntVariable.of("100"),
						IntVariable.of("6")
				)).size(DoubleVariable.of("0.25"))
				.motion(SimpleMotion.BREAKING)
				.renderer(new CrossRenderData(TEX))
				.build();
	}

	public static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		int phi = 7;
		int theta = 24;
		return new LoopIterator(
				IntVariable.of("" + phi),
				new LoopIterator(
						IntVariable.of("" + theta),
						new CustomProjectileShoot(
								DoubleVariable.of("1"), ctx.proj,
								IntVariable.of("100"),
								false, true,
								Map.of()
						).move(new RotationModifier(
								DoubleVariable.of(360 / theta + "*j"),
								DoubleVariable.of(90 / phi + "*(i+0.5)")
						)), "j"
				), "i"
		).move(
				OffsetModifier.of("0", "0.55", "0"),
				SetDirectionModifier.of("1", "0", "0")
		);
	}

}
