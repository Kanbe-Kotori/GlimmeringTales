package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.block.ScheduleTick;
import dev.xkmc.l2magic.content.engine.block.SetBlock;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LinearIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class StoneBridge {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH
			.build(GlimmeringTales.loc("stone_bridge")).focusAndCost(80, 400)
			.spell(ctx -> new SpellAction(gen(ctx), GTItems.STONE_BRIDGE.get(),
					2000, SpellCastType.INSTANT, SpellTriggerType.HORIZONTAL_FACING)
			).lang("Stone Bridge").desc(
					"[Forward] Create a temporary stone bridge",
					"Create a stone bridge extending forward lasting for 10 seconds",
					SpellTooltipData.of()
			);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new LoopIterator(
				IntVariable.of("7"),
				new LinearIterator(
						DoubleVariable.of("0.7"),
						Vec3.ZERO,
						DoubleVariable.ZERO,
						IntVariable.of("16"),
						true,
						new PredicateLogic(
								BlockTestCondition.Type.REPLACEABLE.get(),
								new ListLogic(List.of(
										new SetBlock(GTItems.FAKE_STONE.getDefaultState()),
										new ScheduleTick(IntVariable.of("rand(180,220)"), GTItems.FAKE_STONE.get())
								)),
								null
						).move(
								new RotationModifier(DoubleVariable.of("90"), DoubleVariable.ZERO),
								ForwardOffsetModifier.of("(i-3)*0.7")
						).delay(IntVariable.of("abs(i-3)+j")),
						"j"
				), "i"
		).move(OffsetModifier.of("0", "-1", "0"));

	}


}
