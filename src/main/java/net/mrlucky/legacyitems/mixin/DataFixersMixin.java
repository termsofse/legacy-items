package net.mrlucky.legacyitems.mixin;

import com.mojang.datafixers.DataFixerBuilder;
import net.mrlucky.legacyitems.LegacyForwardPort;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.filefix.FileFixerUpper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataFixers.class)
public abstract class DataFixersMixin {
    @Inject(method = "addFixers", at = @At("TAIL"))
    private static void legacyitems$addForwardPort(DataFixerBuilder builder, FileFixerUpper.Builder fileFixerBuilder, CallbackInfo ci) {
        LegacyForwardPort.install(builder);
    }
}
