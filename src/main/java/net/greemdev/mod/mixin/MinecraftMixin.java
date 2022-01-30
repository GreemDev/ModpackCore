package net.greemdev.mod.mixin;

import net.greemdev.mod.GreemMod;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow protected abstract boolean isMultiplayerServer();

    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
    private void injectTitle(CallbackInfoReturnable<String> cir) {
        var titleValue = GreemMod.config().get("minecraft.windowTitle").getAsString();

        if (titleValue.equalsIgnoreCase("mod")) {
            var sb = new StringBuilder("GreemPack ");
            if (isMultiplayerServer() && this.player != null)
                sb.append("Multiplayer ");
            sb.append("- ").append(SharedConstants.getCurrentVersion().getName());
            cir.setReturnValue(sb.toString());
        } else
            cir.setReturnValue(titleValue);
    }
}
