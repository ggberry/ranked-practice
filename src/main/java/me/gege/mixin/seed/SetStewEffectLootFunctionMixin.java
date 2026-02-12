package me.gege.mixin.seed;

import com.google.common.collect.Iterables;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetStewEffectLootFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static me.gege.util.SeedUtil.isPracticing;

@Mixin(SetStewEffectLootFunction.class)
public abstract class SetStewEffectLootFunctionMixin {
    @Shadow @Final private Map<StatusEffect, UniformLootTableRange> effects;

    @Inject(at = @At("HEAD"), method = "process", cancellable = true)
    private void process(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        if (!isPracticing) {
            return;
        }

        if (stack.getItem() == Items.SUSPICIOUS_STEW && !this.effects.isEmpty()) {
            Map<StatusEffect, UniformLootTableRange> filtered = new HashMap<>(this.effects);
            filtered.remove(StatusEffects.POISON);

            Random random = context.getRandom();
            int i = random.nextInt(filtered.size());
            Map.Entry<StatusEffect, UniformLootTableRange> entry = Iterables.get(filtered.entrySet(), i);

            StatusEffect statusEffect = entry.getKey();
            int j = entry.getValue().next(random);

            if (!statusEffect.isInstant()) {
                j *= 20;
            }

            SuspiciousStewItem.addEffectToStew(stack, statusEffect, j);
            cir.setReturnValue(stack);
        } else {
            cir.setReturnValue(stack);
        }
    }
}
