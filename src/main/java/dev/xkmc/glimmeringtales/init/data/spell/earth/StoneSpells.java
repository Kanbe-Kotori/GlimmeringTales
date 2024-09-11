package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.block.ScheduleTick;
import dev.xkmc.l2magic.content.engine.block.SetBlock;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class StoneSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH
			.build(GlimmeringTales.loc("stone")).focusAndCost(40, 120)
			.block(StoneSpells::gen, GTItems.RUNE_STONE, RuneBlock::self,
					(b, e) -> b.add(Tags.Blocks.STONES, BlockSpell.of(e)),
					(b, e) -> b.add(GTItems.FAKE_STONE, BlockSpell.of(e))
			).lang("Stone Cliff").desc(
					"[Block] Create temporary stone floor",
					"Create a circular stone floor lasting 10 seconds",
					SpellTooltipData.of()
			);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.STONE_PLACE,
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new SetBlock(GTItems.FAKE_STONE.getDefaultState()),
				new ScheduleTick(IntVariable.of("rand(180,220)"), GTItems.FAKE_STONE.get())
		)).circular(
				DoubleVariable.of("3"), DoubleVariable.ZERO, true, null,
				BlockTestCondition.Type.REPLACEABLE.get());
	}


}
