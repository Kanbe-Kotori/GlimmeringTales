package dev.xkmc.glimmeringtales.init.data.spell;

import dev.xkmc.glimmeringtales.init.data.spell.earth.*;
import dev.xkmc.glimmeringtales.init.data.spell.flame.FlamePentagram;
import dev.xkmc.glimmeringtales.init.data.spell.flame.MagmaSpells;
import dev.xkmc.glimmeringtales.init.data.spell.flame.NetherrackSpells;
import dev.xkmc.glimmeringtales.init.data.spell.flame.SoulSandSpells;
import dev.xkmc.glimmeringtales.init.data.spell.life.*;
import dev.xkmc.glimmeringtales.init.data.spell.ocean.CoralReefSpell;
import dev.xkmc.glimmeringtales.init.data.spell.ocean.OceanShelter;
import dev.xkmc.glimmeringtales.init.data.spell.ocean.SpongeSpell;
import dev.xkmc.glimmeringtales.init.data.spell.snow.IceSpells;
import dev.xkmc.glimmeringtales.init.data.spell.snow.PowderSnowSpell;
import dev.xkmc.glimmeringtales.init.data.spell.snow.SnowSpells;
import dev.xkmc.glimmeringtales.init.data.spell.snow.SnowStorm;
import dev.xkmc.glimmeringtales.init.data.spell.thunder.ChargeBurst;
import dev.xkmc.glimmeringtales.init.data.spell.thunder.ThunderSpells;
import dev.xkmc.glimmeringtales.init.data.spell.thunder.Thunderstorm;

import java.util.ArrayList;
import java.util.List;

public class NatureSpellGenRegistry {

	public static final List<NatureSpellEntry> LIST = new ArrayList<>();

	static {

		// earth
		LIST.addAll(List.of(
				DripstoneSpells.BUILDER, // 1010
				ClaySpells.BUILDER, // 1020
				AmethystSpells.BUILDER, // 1030
				SandSpells.BUILDER, // 1040
				QuartzSpells.BUILDER, // 1050
				GravelSpells.BUILDER, // 1060
				StoneSpells.BUILDER
		));

		// vine
		LIST.addAll(List.of(
				VinesSpell.BUILDER, // 1050
				HaySpell.BUILDER,
				CactusSpell.BUILDER,
				BambooSpell.BUILDER,
				FlowerSpell.BUILDER
		));

		// others
		LIST.addAll(List.of(
				MagmaSpells.BUILDER,
				NetherrackSpells.BUILDER,
				SoulSandSpells.BUILDER,

				SnowSpells.BUILDER,
				PowderSnowSpell.BUILDER,
				IceSpells.ICE,
				IceSpells.PACK_ICE,
				IceSpells.BLUE_ICE,

				SpongeSpell.BUILDER,
				CoralReefSpell.BUILDER,

				ThunderSpells.BUILDER
		));

		// advanced
		LIST.addAll(List.of(
				FlamePentagram.HELL_MARK,
				FlamePentagram.LAVA_BURST,
				SnowStorm.WINTER_STORM,
				SnowStorm.SNOW_TORNADO,
				StoneBridge.BUILDER,
				AmethystPenetration.BUILDER,
				Earthquake.BUILDER,
				OceanShelter.BUILDER,
				Thunderstorm.BUILDER,
				ChargeBurst.BUILDER
		));
	}

}
