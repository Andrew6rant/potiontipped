package com.github.Crupette.potiontipped.client.resources;

import com.github.Crupette.potiontipped.PotionTipped;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.resource.ResourcePackProfile;
import org.apache.logging.log4j.Level;

import java.io.*;

public class ResourcePackCreator {
    private final File base;

    public ResourcePackCreator(String name){
        base = new File(FabricLoader.INSTANCE.getGameDirectory(), "resourcepacks/" + name);
        base.mkdir();
        File mcmeta = new File(base, "pack.mcmeta");

        try {
            OutputStream mcmetaOut = new FileOutputStream(mcmeta);
            mcmetaOut.write(" { \"pack\": { \"pack_format\": 5, \"description\": \"Adds models to all potion tipped tools / weapons (this needs to be enabled for the mod to look decent)\" } }".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addResource(String parent, String child, String data){
        File dir = new File(base, parent);
        dir.mkdirs();
        File newResource = new File(dir, child);

        try {
            OutputStream resourceOut = new FileOutputStream(newResource);
            resourceOut.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
