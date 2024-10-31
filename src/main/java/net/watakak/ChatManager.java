package net.watakak;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatManager {

    private static boolean oneTime = false;

    public static void registerEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            oneTime = false;

            ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
                if (client != null && MinecraftClient.getInstance().inGameHud != null) {
                    if (!oneTime && Config.getBool("Welcome.OneTime")) {
                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(Config.getString("Welcome.Message")));
                        oneTime = true;
                    } else if (!Config.getBool("Welcome.OneTime")) {
                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(Config.getString("Welcome.Message")));
                    }
                }
            });
        });
    }
}
