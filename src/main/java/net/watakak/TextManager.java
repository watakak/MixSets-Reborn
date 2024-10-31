package net.watakak;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;

public class TextManager {

    private static boolean oneTime = false;

    public static void registerEvents() {
        // Перехватчик для одиночной игры
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client != null && MinecraftClient.getInstance().inGameHud != null) {
                displayWelcomeMessage(client);
            }
        });

        // Перехватчик для серверов
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            oneTime = false;
        });
    }

    // Метод для показа сообщения приветствия
    private static void displayWelcomeMessage(MinecraftClient client) {
        if (Config.getBool("Welcome") && client != null) {
            if (!oneTime && Config.getBool("Welcome.OneTime")) {
                sendLocalMessage(Config.getString("Welcome.Message"));
                oneTime = true;
            } else if (!Config.getBool("Welcome.OneTime")) {
                sendLocalMessage(Config.getString("Welcome.Message"));
            }
        }
    }

    // Отправка локального сообщения только игроку
    public static void sendLocalMessage(String text) {
        if (MinecraftClient.getInstance().inGameHud != null) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(text));
        }
    }

    // Отправка сообщения в чат всем игрокам на сервере или в мире
    public static void sendMessage(String text) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getServer() != null) { // Серверный мир
            for (ServerPlayerEntity player : client.getServer().getPlayerManager().getPlayerList()) {
                player.sendMessage(Text.literal(text), false);
            }
        } else { // Одиночный мир
            sendLocalMessage(text);
        }
    }

    // Показывает заголовок (Title) для игрока
    public static void showTitle(String text) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(text), true);
            client.inGameHud.setTitle(Text.literal(text));
        }
    }

    // Показывает подзаголовок (Subtitle) для игрока
    public static void showSubtitle(String text) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.inGameHud.setSubtitle(Text.literal(text));
        }
    }

    // Показывает текст над Hotbar (как "uptitle")
    public static void showUptitle(String text) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.inGameHud.setOverlayMessage(Text.literal(text), false);
        }
    }

    // Дополнительные функции

    // Сообщение об ошибке, видимое только игроку
    public static void sendErrorMessage(String text) {
        sendLocalMessage("§c" + text); // Красный цвет сообщения для ошибки
    }

    // Сообщение об успешном выполнении, видимое только игроку
    public static void sendSuccessMessage(String text) {
        sendLocalMessage("§a" + text); // Зеленый цвет для успешного сообщения
    }

    // Отображение настраиваемого сообщения в заголовке и подзаголовке
    public static void showTitleAndSubtitle(String title, String subtitle) {
        showTitle(title);
        showSubtitle(subtitle);
    }

    // Method to create and render text on screen with a specific background style
    public static void createText(DrawContext context, String text, String bg, int x, int y, int size) {
        MinecraftClient client = MinecraftClient.getInstance();
        int color = 0xFFFFFFFF; // White color for text
        int backgroundColor = 0x88000000; // Semi-transparent black for background (ARGB format)

        if ("fill".equals(bg)) {
            // Calculate the text width and height for the background box
            int textWidth = client.textRenderer.getWidth(text);
            int textHeight = client.textRenderer.fontHeight;

            // Draw the background rectangle
            context.fill(x - 2, y - 2, x + textWidth + 2, y + textHeight + 2, backgroundColor);

            // Draw the text without shadow on top of the background
            context.drawText(client.textRenderer, text, x, y, color, false);
        } else if ("shadow".equals(bg)) {
            // Draw text with shadow
            context.drawText(client.textRenderer, text, x, y, color, true);
        } else {
            // Default behavior (simple text without shadow)
            context.drawText(client.textRenderer, text, x, y, color, false);
        }
    }

}
