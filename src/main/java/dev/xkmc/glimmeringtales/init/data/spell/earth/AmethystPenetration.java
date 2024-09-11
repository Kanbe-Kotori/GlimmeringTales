package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.engine.processor.StackingEffectProcessor;
import dev.xkmc.glimmeringtales.content.engine.render.OrientedCrossRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.RingIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;
import java.util.Map;

public class AmethystPenetration {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH
			.build(GlimmeringTales.loc("amethyst_penetration")).focusAndCost(60, 300).mob(16, 1)
			.damageCustom(msg -> new DamageType(msg, 0.1f),
					"%s is pierced by amethyst shards",
					"%s is pierced by %s with amethyst shards",
					DamageTypeTags.IS_PROJECTILE)
			.projectile(AmethystPenetration::proj)
			.spell(ctx -> new SpellAction(gen(ctx), GTItems.AMETHYST_PENETRATION.get(),
					2000, SpellCastType.INSTANT, SpellTriggerType.FACING_FRONT))
			.lang("Amethyst Penetration").desc(
					"[Forward] Shoot several amethyst shards forward",
					"Shoot amethyst shards in a fan area in front of you, dealing %s and stack %s",
					SpellTooltipData.of(EngineRegistry.DAMAGE, GTEngine.EP_STACK)
			);

	private static final ResourceLocation TEX = GlimmeringTales.loc("textures/spell/infused_amethyst.png");
	private static final DoubleVariable DMG = DoubleVariable.of("12");

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
				.motion(SimpleMotion.ZERO)
				.renderer(new OrientedCrossRenderData(TEX))
				.build();
	}

	public static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.AMETHYST_BLOCK_BREAK,
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new RingIterator(
						DoubleVariable.of("0.5"),
						DoubleVariable.of("-15"),
						DoubleVariable.of("15"),
						IntVariable.of("15"),
						true,
						new CustomProjectileShoot(
								DoubleVariable.of("1"), ctx.proj,
								IntVariable.of("60"),
								false, true,
								Map.of()
						), null
				).move(ForwardOffsetModifier.of("-1"), OffsetModifier.of("0", "-0.1", "0"))
		));

	}

}
