package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.block.ScheduleTick;
import dev.xkmc.l2magic.content.engine.block.SetBlock;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.sounds.SoundEvents;

import java.util.List;

public class QuartzSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH
			.build(GlimmeringTales.loc("quartz")).focusAndCost(100, 400)
			.block(QuartzSpells::gen, GTItems.RUNE_QUARTZ, RuneBlock::of,
					(b, e) -> b.add(GTTagGen.QUARTZ, BlockSpell.of(e)))
			.lang("Crystalization").desc(
					"[Block] Create a temorary glass shell",
					"Create a spherical glass shell lasting for 10 seconds",
					SpellTooltipData.of()
			);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.GLASS_PLACE,
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new SetBlock(GTItems.FAKE_GLASS.getDefaultState()),
				new ScheduleTick(IntVariable.of("rand(180,220)"), GTItems.FAKE_GLASS.get())
		)).circular(
				DoubleVariable.of("4"),
				DoubleVariable.ZERO,
				false, "i",
				BooleanVariable.of("abs(i_r-3)<0.5"),
				BlockTestCondition.Type.REPLACEABLE.get()
		).move(OffsetModifier.ABOVE);
	}

}
