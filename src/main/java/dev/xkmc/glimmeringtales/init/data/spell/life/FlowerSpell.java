package dev.xkmc.glimmeringtales.init.data.spell.life;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.content.engine.instance.EffectCloudInstance;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.alchemy.Potions;

import java.util.List;

public class FlowerSpell {
	public static final NatureSpellBuilder BUILDER = GTRegistries.LIFE
			.build(GlimmeringTales.loc("flower")).focusAndCost(50, 250)
			.block(FlowerSpell::flower, GTItems.RUNE_FLOWER, RuneBlock::offset,
					(b, e) -> b.add(BlockTags.FLOWERS, BlockSpell.cost(e)))
			.lang("Flower").desc(
					"[Block] Create a healing cloud",
					"Create a lingering effect cloud of instant healing",
					SpellTooltipData.of()
			);

	private static ConfiguredEngine<?> flower(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.BEACON_ACTIVATE,
						DoubleVariable.of("1"),
						DoubleVariable.of("5+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new EffectCloudInstance(
						Potions.HEALING,
						DoubleVariable.of("3"),
						IntVariable.of("10")
				)
		));

	}

}