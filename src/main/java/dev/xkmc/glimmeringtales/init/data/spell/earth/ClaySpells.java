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
import dev.xkmc.l2magic.content.engine.predicate.SurfaceBelowCondition;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class ClaySpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH
			.build(GlimmeringTales.loc("clay")).focusAndCost(60, 240)
			.block(ClaySpells::gen, GTItems.RUNE_CLAY, RuneBlock::of,
					(b, e) -> b.add(Blocks.CLAY, BlockSpell.of(e)),
					(b, e) -> b.add(GTItems.CLAY_CARPET, BlockSpell.of(e))
			).lang("Clay Overflow").desc(
					"[Block] Form a circular carpet to trap entities",
					"Create a circular field of clay carpet lasting 5 seconds to immobilize entities",
					SpellTooltipData.of()
			);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.GRAVEL_HIT,
						DoubleVariable.of("0.5"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new SetBlock(GTItems.CLAY_CARPET.getDefaultState()),
				new ScheduleTick(IntVariable.of("rand(80,120)"), GTItems.CLAY_CARPET.get())
		)).circular(
				DoubleVariable.of("4"),
				DoubleVariable.of("2"),
				false, null,
				SurfaceBelowCondition.full(),
				BlockTestCondition.Type.REPLACEABLE.get()
		);
	}

}
