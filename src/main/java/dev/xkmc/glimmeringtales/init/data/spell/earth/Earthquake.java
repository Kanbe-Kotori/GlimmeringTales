package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.engine.instance.GTKnockBlock;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxBallSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;

public class Earthquake {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH
			.build(GlimmeringTales.loc("earthquake")).focusAndCost(160, 640).mob(5, 1)
			.damageCustom(e -> new DamageType(e, 0.1f),
					"%s is killed by earthquake", "%s is killed by %s using earthquake",
					DamageTypeTags.IS_EXPLOSION
			).spell(ctx -> new SpellAction(gen(ctx), GTItems.EARTHQUAKE.get(),
					2000, SpellCastType.INSTANT, SpellTriggerType.SELF_POS)
			).lang("Earthquake").desc(
					"[Surrounding] Shake the ground and throw blocks into air",
					"Create earthquake dealing dealing %s, then throw blocks around you into the air that deals %s on fall",
					SpellTooltipData.damageAndFalling()
			);

	private static final DoubleVariable DMG = DoubleVariable.of("2");
	private static final DoubleVariable INIT = DoubleVariable.of("10");
	private static final DoubleVariable MAX = DoubleVariable.of("30");

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.GENERIC_EXPLODE.value(),
						DoubleVariable.of("2"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new DelayedIterator(
						IntVariable.of("6"),
						IntVariable.of("2"),
						new ProcessorEngine(
								SelectionType.ENEMY_NO_FAMILY,
								new ApproxBallSelector(DoubleVariable.of("i+1")),
								List.of(new DamageProcessor(ctx.damage(), INIT, true, false))
						), "i"
				),
				new GTKnockBlock(DoubleVariable.of("1"), DMG, MAX).delay(IntVariable.of("i_r*2")).circular(
						DoubleVariable.of("6"), DoubleVariable.of("0"), false, "i",
						BooleanVariable.of("i_r>1.5"),
						BlockTestCondition.Type.BLOCKS_MOTION.get(),
						BlockTestCondition.Type.PUSHABLE.get(),
						BlockTestCondition.Type.REPLACEABLE.get().move(OffsetModifier.ABOVE))
		));

	}


}
