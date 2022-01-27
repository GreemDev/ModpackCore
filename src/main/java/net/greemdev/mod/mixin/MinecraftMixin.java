package net.greemdev.mod.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.*;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    public abstract boolean hasSingleplayerServer();


    /**
     * @author GreemDev
     * @reason Custom window title
     */
    @Overwrite
    private String createTitle() {
        var sb = new StringBuilder("GreemPack ");
        if (!hasSingleplayerServer())
            sb.append("Multiplayer ");
        sb.append(SharedConstants.getCurrentVersion().getName());
        return sb.toString();
    }
}
