package net.watakak;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Objects;

public class FPS {

    private static final int FPS_SIZE = 20;

    public static void register() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> renderFPS(context));
    }

    private static void renderFPS(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Check if FPS display is enabled
        if (!Config.getBool("FPS")) {
            return;
        }

        if (client.player == null) {
            return; // No player means we're not in a game world
        }

        // Get current FPS
        int fps = client.getCurrentFps();
        String fpsText = "FPS: " + fps;

        // Determine position based on config
        int x = 10; // Default X position
        int y = 10; // Default Y position

        int scaledWidth = client.getWindow().getScaledWidth();
        int scaledHeight = client.getWindow().getScaledHeight();

        switch (Config.getString("FPS.Position")) {
            case "Top-Left":
                x = 6;
                y = 6;
                break;
            case "Top-Right":
                x = scaledWidth - 6 - client.textRenderer.getWidth(fpsText);
                y = 6;
                break;
            case "Bottom-Left":
                x = 6;
                y = scaledHeight - 6 - client.textRenderer.fontHeight;
                break;
            case "Bottom-Right":
                x = scaledWidth - 6 - client.textRenderer.getWidth(fpsText);
                y = scaledHeight - 6 - client.textRenderer.fontHeight;
                break;
            default:
                x = 6;
                y = 6;
        }

        // Determine text type based on config
        String textType = Config.getString("FPS.Type");
        String bg = "default";

        if (textType.equals("Shadow")) {
            bg = "shadow";
        }
        else if (textType.equals("Fill")) {
            bg = "fill";
        }

        // Create and display the text
        TextManager.createText(context, fpsText, bg, x, y, FPS_SIZE);
    }
}
