package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.glimmeringtales.content.block.crop.LifeCrystalCrop;
import dev.xkmc.glimmeringtales.content.block.misc.ClayCarpetImpl;
import dev.xkmc.glimmeringtales.content.block.misc.SelfDestroyImpl;
import dev.xkmc.glimmeringtales.content.block.misc.SelfDestroyTransparent;
import dev.xkmc.glimmeringtales.content.block.misc.StuckEntityMethod;
import dev.xkmc.glimmeringtales.content.item.materials.DepletedItem;
import dev.xkmc.glimmeringtales.content.item.rune.BlockRuneItem;
import dev.xkmc.glimmeringtales.content.item.rune.SpellCoreItem;
import dev.xkmc.glimmeringtales.content.item.wand.RuneWandItem;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class GTItems {

	public static final SimpleEntry<CreativeModeTab> TAB = GlimmeringTales.REGISTRATE
			.buildL2CreativeTab("glimmeringtales", "Glimmering Tales", e ->
					e.icon(GTItems.CRYSTAL_EARTH::asStack));

	public static final ItemEntry<RuneWandItem> WAND;

	public static final ItemEntry<SpellCoreItem> CRYSTAL_NATURE,
			CRYSTAL_LIFE, CRYSTAL_FLAME, CRYSTAL_EARTH, CRYSTAL_WINTERSTORM,
			CRYSTAL_OCEAN, CRYSTAL_THUNDER;
	public static final ItemEntry<DepletedItem> DEPLETED_FLAME, DEPLETED_WINTERSTORM;
	public static final BlockEntry<LifeCrystalCrop> CRYSTAL_VINE;

	public static final ItemEntry<BlockRuneItem> RUNE_BAMBOO, RUNE_CACTUS, RUNE_FLOWER, RUNE_VINE,
			RUNE_SAND, RUNE_GRAVEL, RUNE_QUARTZ, RUNE_CLAY, RUNE_DRIPSTONE, RUNE_AMETHYST,
			RUNE_LAVA, RUNE_SOUL_SAND, RUNE_SNOW, RUNE_ICE, RUNE_POWDER_SNOW;

	public static final BlockEntry<DelegateBlock> CLAY_CARPET;
	public static final BlockEntry<SelfDestroyTransparent> FAKE_GLASS;
	public static final BlockEntry<SelfDestroyTransparent> FAKE_BAMBOO;

	private static final DCReg DC = DCReg.of(GlimmeringTales.REG);

	public static final DCVal<Integer> PROGRESS = DC.intVal("progress");

	static {

		{
			CRYSTAL_NATURE = GlimmeringTales.REGISTRATE.item("crystal_of_nature", SpellCoreItem::new).register();
			CRYSTAL_EARTH = GlimmeringTales.REGISTRATE.item("crystal_of_earth", SpellCoreItem::new).register();
			CRYSTAL_LIFE = GlimmeringTales.REGISTRATE.item("crystal_of_life", SpellCoreItem::new).register();
			CRYSTAL_FLAME = GlimmeringTales.REGISTRATE.item("crystal_of_flame", SpellCoreItem::new).register();
			CRYSTAL_WINTERSTORM = GlimmeringTales.REGISTRATE.item("crystal_of_winterstorm", SpellCoreItem::new).register();
			CRYSTAL_OCEAN = GlimmeringTales.REGISTRATE.item("crystal_of_ocean", SpellCoreItem::new).register();
			CRYSTAL_THUNDER = GlimmeringTales.REGISTRATE.item("crystal_of_thunder", SpellCoreItem::new).register();
			DEPLETED_FLAME = GlimmeringTales.REGISTRATE.item("depleted_crystal_of_flame", p ->
					new DepletedItem(p, () -> Blocks.LAVA, GTConfigs.SERVER.crystalOfFlameRequirement,
							CRYSTAL_FLAME::get, () -> SoundEvents.BUCKET_FILL_LAVA)
			).register();
			DEPLETED_WINTERSTORM = GlimmeringTales.REGISTRATE.item("depleted_crystal_of_winterstorm", p ->
					new DepletedItem(p, () -> Blocks.POWDER_SNOW, GTConfigs.SERVER.crystalOfWinterstormRequirement,
							CRYSTAL_WINTERSTORM::get, () -> SoundEvents.BUCKET_FILL_POWDER_SNOW)
			).register();

			CRYSTAL_VINE = GlimmeringTales.REGISTRATE.block("crystal_vine", LifeCrystalCrop::new)
					.properties(p -> p.mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak()
							.sound(SoundType.CROP).pushReaction(PushReaction.DESTROY))
					.item(ItemNameBlockItem::new).model((ctx, pvd) -> pvd.generated(ctx))
					.lang("Seed of Nature").build()
					.blockstate(LifeCrystalCrop::buildState)
					.loot(LifeCrystalCrop::builtLoot)
					.tag(BlockTags.CROPS)
					.register();
		}

		{
			WAND = GlimmeringTales.REGISTRATE.item("wand",
							p -> new RuneWandItem(p.stacksTo(1).fireResistant()))
					.register();
		}

		{

			RUNE_BAMBOO = rune("bamboo", () -> Blocks.BAMBOO, "Rune: Bamboo");
			RUNE_CACTUS = rune("cactus", () -> Blocks.CACTUS, "Rune: Cactus");
			RUNE_FLOWER = rune("flower", () -> Blocks.POPPY, "Rune: Flower");
			RUNE_VINE = rune("vine", () -> Blocks.VINE, "Rune: Vine");

			RUNE_SAND = rune("sand", () -> Blocks.SAND, "Rune: Sand");
			RUNE_GRAVEL = rune("gravel", () -> Blocks.GRAVEL, "Rune: Gravel");
			RUNE_CLAY = rune("clay", () -> Blocks.CLAY, "Rune: Clay");
			RUNE_QUARTZ = rune("quartz", () -> Blocks.QUARTZ_BLOCK, "Rune: Quartz");
			RUNE_DRIPSTONE = rune("dripstone", () -> Blocks.DRIPSTONE_BLOCK, "Rune: Stalactite");
			RUNE_AMETHYST = rune("amethyst", () -> Blocks.AMETHYST_BLOCK, "Rune: Amethyst");

			RUNE_LAVA = rune("lava", () -> Blocks.LAVA, "Rune: Lava");
			RUNE_SOUL_SAND = rune("soul_sand", () -> Blocks.SOUL_SAND, "Rune: Soul Sand");

			RUNE_SNOW = rune("snow", () -> Blocks.SNOW_BLOCK, "Rune: Snow");
			RUNE_ICE = rune("ice", () -> Blocks.ICE, "Rune: Ice");
			RUNE_POWDER_SNOW = rune("powder_snow", () -> Blocks.POWDER_SNOW, "Rune: Powder Snow");
		}

		{
			CLAY_CARPET = GlimmeringTales.REGISTRATE.block("clay_carpet",
							p -> DelegateBlock.newBaseBlock(p, new SelfDestroyImpl(), new ClayCarpetImpl(),
									new StuckEntityMethod(new Vec3(0.05, 1f, 0.05))))
					.properties(p -> p.mapColor(MapColor.CLAY).strength(0.6f).speedFactor(0.2F).jumpFactor(0.2f)
							.sound(SoundType.GRAVEL).pushReaction(PushReaction.DESTROY).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().carpet(ctx.getName(), pvd.mcLoc("block/clay"))))
					.register();

			FAKE_GLASS = GlimmeringTales.REGISTRATE.block("glass", SelfDestroyTransparent::new)
					.properties(p -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models()
							.withExistingParent(ctx.getName(), pvd.mcLoc("block/glass")).renderType("cutout")))
					.register();

			FAKE_BAMBOO = GlimmeringTales.REGISTRATE.block("bamboo", SelfDestroyTransparent::new)
					.properties(p -> BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models()
							.withExistingParent(ctx.getName(), pvd.mcLoc("block/bamboo4_age1")).renderType("cutout")))
					.register();

		}

	}

	private static ItemEntry<BlockRuneItem> rune(String id, Supplier<Block> block, String name) {
		return GlimmeringTales.REGISTRATE.item(id, p -> new BlockRuneItem(p.stacksTo(1).fireResistant(), block))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/rune/" + ctx.getName())))
				.lang(name).register();
	}

	public static void register() {

	}

}
