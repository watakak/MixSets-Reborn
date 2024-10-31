package net.watakak;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;

public class Utils {
    static String getLoader() {
        return MinecraftClient.getInstance().getGameVersion();
    }

    static String getPlayerNickname() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            return client.player.getName().getString();
        }
        return "Player";
    }

    static String getModVersion() {
        return FabricLoader.getInstance().getModContainer(MixSets.MOD_ID)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");
    }

    static String getMinecraftVersion() {
        return MinecraftVersion.CURRENT.getName();
    }

    static String getLanguage() {
        return MinecraftClient.getInstance().getLanguageManager().getLanguage();
    }
}