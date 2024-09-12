package dev.xkmc.glimmeringtales.init.data.spell.life;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.content.engine.processor.ProcreationProcessor;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.iterator.SphereRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.selector.ApproxBallSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class HaySpell {
	public static final NatureSpellBuilder BUILDER = GTRegistries.LIFE
			.build(GlimmeringTales.loc("procreation")).focusAndCost(100, 400)
			.block(ctx -> procreation(ctx, 4, 1.5, 1), GTItems.RUNE_HAYBALE, RuneBlock::offset,
					(b, e) -> b.add(Blocks.HAY_BLOCK, BlockSpell.cost(e)))
			.lang("Procreation").desc(
					"[Block] Breed nearby animals",
					"Feed all nearby animals",
					SpellTooltipData.of()
			);

	private static ConfiguredEngine<?> procreation(NatureSpellBuilder ctx, double r, double y, double size) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.PLAYER_LEVELUP,
						DoubleVariable.of("1"),
						DoubleVariable.of("5+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new ProcessorEngine(SelectionType.ALL,
						new ApproxBallSelector(
								DoubleVariable.of(r + "")
						),
						List.of(
								new ProcreationProcessor(
										IntVariable.of("4")
								)
						)),
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("2"),
						new SphereRandomIterator(
								DoubleVariable.of("5"),
								IntVariable.of("20"),
								new SimpleParticleInstance(
										ParticleTypes.HEART,
										DoubleVariable.of("0.5")
								),
								null
						), null
				)
		));
	}

	private static ConfiguredEngine<?> affectProjectile(DataGenContext ctx, double r, double y, double size) {
		return new ListLogic(List.of(
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("2"),
						new RingRandomIterator(
								DoubleVariable.of((r - size) + ""),
								DoubleVariable.of((r + size) + ""),
								DoubleVariable.of("-180"),
								DoubleVariable.of("180"),
								IntVariable.of("5"),
								new MoveEngine(List.of(
										RotationModifier.of("75"),
										OffsetModifier.of("0", "rand(" + (y - size) + "," + (y + size) + ")", "0")),
										new SimpleParticleInstance(
												ParticleTypes.BUBBLE,
												DoubleVariable.of("0.5")
										)
								), null
						), null
				)
		));
	}


}
