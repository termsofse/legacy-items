package net.mrlucky.legacyitems.mixin;

import net.mrlucky.legacyitems.LegacyItems;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public abstract class BuiltInRegistriesMixin {
    @Inject(
            method = "bootStrap",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/registries/BuiltInRegistries;freeze()V",
                    shift = At.Shift.BEFORE))
    private static void legacyitems$registerLegacyItems(CallbackInfo ci) {
        LegacyItems.registerAll();
    }
}
