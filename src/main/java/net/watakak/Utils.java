package net.watakak;
import net.fabricmc.loader.api.FabricLoader;

public class Utils {
    static String getLoader() {
        if (FabricLoader.getInstance().isModLoaded("fabricloader")) {
            return "Fabric";
        }
        else if (FabricLoader.getInstance().isModLoaded("quilt")) {
            return "Quilt";
        }
        else if (FabricLoader.getInstance().isModLoaded("forge")) {
            return "Forge";
        }
        else if (FabricLoader.getInstance().isModLoaded("neoforge")) {
            return "NeoForge";
        }
        else {
            return "MixSets";
        }
    }
}
