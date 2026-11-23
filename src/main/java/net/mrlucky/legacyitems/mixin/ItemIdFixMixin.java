package net.mrlucky.legacyitems.mixin;

import net.mrlucky.legacyitems.LegacyForwardPort;
import net.minecraft.util.datafix.fixes.ItemIdFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemIdFix.class)
public abstract class ItemIdFixMixin {
    @Inject(method = "getItem", at = @At("HEAD"), cancellable = true)
    private static void legacyitems$nameLegacyBlockIds(int id, CallbackInfoReturnable<String> cir) {
        String name = LegacyForwardPort.numericName(id);
        if (name != null) {
            cir.setReturnValue(name);
        }
    }
}
