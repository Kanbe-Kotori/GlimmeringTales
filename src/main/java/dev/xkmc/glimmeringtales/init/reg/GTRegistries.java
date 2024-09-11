package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.glimmeringtales.content.capability.PlayerManaCapability;
import dev.xkmc.glimmeringtales.content.core.spell.*;
import dev.xkmc.glimmeringtales.content.item.curio.AttributeData;
import dev.xkmc.glimmeringtales.content.item.wand.RuneSwapType;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.l2backpack.content.quickswap.type.MatcherSwapType;
import dev.xkmc.l2core.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.LegacyHolder;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.AttReg;
import dev.xkmc.l2core.init.reg.simple.AttVal;
import dev.xkmc.l2core.util.DataGenOnly;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GTRegistries {

	public static final L2Registrate.RegistryInstance<SpellElement> ELEMENT =
			GlimmeringTales.REGISTRATE.newRegistry("element", SpellElement.class);
	public static final ResourceKey<Registry<NatureSpell>> SPELL =
			ResourceKey.createRegistryKey(GlimmeringTales.loc("spell"));
	public static final DataMapReg<Block, BlockSpell> BLOCK =
			GlimmeringTales.REG.dataMap("block_spell", Registries.BLOCK, BlockSpell.class);
	public static final DataMapReg<Item, ElementAffinity> AFFINITY =
			GlimmeringTales.REG.dataMap("element_affinity", Registries.ITEM, ElementAffinity.class);
	public static final DataMapReg<Item, RuneBlock> RUNE_BLOCK =
			GlimmeringTales.REG.dataMap("rune_block", Registries.ITEM, RuneBlock.class);
	public static final DataMapReg<Block, BlockReplace> REPLACE =
			GlimmeringTales.REG.dataMap("block_replace", Registries.BLOCK, BlockReplace.class);
	public static final DataMapReg<Block, BlockReplace> MELT =
			GlimmeringTales.REG.dataMap("block_melt", Registries.BLOCK, BlockReplace.class);
	public static final DataMapReg<Item, AttributeData> ITEM_ATTR =
			GlimmeringTales.REG.dataMap("curio_attributes", Registries.ITEM, AttributeData.class);

	public static final SimpleEntry<Attribute> MAX_MANA = reg("max_mana", 2000, 1000000, "Max Mana");
	public static final SimpleEntry<Attribute> MANA_REGEN = reg("mana_regen", 20, 1000000, "Mana Regen");
	public static final SimpleEntry<Attribute> MAX_FOCUS = reg("max_focus", 200, 1000000, "Max Focus");

	public static final ElemEntry LIFE = reg("life", ChatFormatting.GREEN);
	public static final ElemEntry EARTH = reg("earth", ChatFormatting.GOLD);
	public static final ElemEntry FLAME = reg("flame", ChatFormatting.RED);
	public static final ElemEntry SNOW = reg("snow", ChatFormatting.AQUA);
	public static final ElemEntry OCEAN = reg("ocean", ChatFormatting.DARK_AQUA);
	public static final ElemEntry THUNDER = reg("thunder", ChatFormatting.YELLOW);

	public static final AttReg ATT = AttReg.of(GlimmeringTales.REG);
	public static final AttVal.PlayerVal<PlayerManaCapability> MANA = ATT.player("mana",
			PlayerManaCapability.class, PlayerManaCapability::new, PlayerCapabilityNetworkHandler::new);

	public static final MatcherSwapType SWAP = new RuneSwapType();

	private static ElemEntry reg(String id, ChatFormatting color) {
		var attr = reg(id + "_affinity", 0, 1000,
				RegistrateLangProvider.toEnglishName(id + "_affinity"), L2DamageTracker.PERCENTAGE);
		return new ElemEntry(GlimmeringTales.REGISTRATE.generic(ELEMENT, id, () -> new SpellElement(color, attr))
				.defaultLang().register(), attr);
	}

	@SafeVarargs
	public static SimpleEntry<Attribute> reg(String id, double def, double max, String name, TagKey<Attribute>... tags) {
		return L2DamageTracker.regWrapped(GlimmeringTales.REGISTRATE, id, def, 0, max, name, tags);
	}

	public static void register() {
	}

	public record ElemEntry(
			RegistryEntry<SpellElement, SpellElement> val,
			SimpleEntry<Attribute> attr
	) implements LegacyHolder<SpellElement> {

		public ResourceKey<SpellElement> key() {
			return val.getKey();
		}

		public SpellElement get() {
			return val.get();
		}

		@DataGenOnly
		public NatureSpellBuilder build(ResourceLocation id) {
			return new NatureSpellBuilder(id, this);
		}

		public TagKey<DamageType> damgeTag() {
			return TagKey.create(Registries.DAMAGE_TYPE, val.getId());
		}

		@Override
		public Holder<SpellElement> holder() {
			return val;
		}

		@Override
		public int hashCode() {
			return key().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			return obj instanceof Holder<?> h &&
					h.kind() == Kind.REFERENCE &&
					key().equals(h.getKey());
		}

	}


}
