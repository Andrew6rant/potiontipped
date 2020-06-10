package com.github.Crupette.potiontipped.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {

    @Shadow public List<String> resourcePacks;

    @Inject(method = "<init>(Lnet/minecraft/client/MinecraftClient;Ljava/io/File;)V",
            at = @At("TAIL"))
    private void init(MinecraftClient client, File optionsFile, CallbackInfo ci){
        if(!this.resourcePacks.contains("file/potiontipped")){
            this.resourcePacks.add("file/potiontipped");
        }
    }
}
