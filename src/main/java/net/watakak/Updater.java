package net.watakak;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Updater {

    private static final String API_URL = "https://api.modrinth.com/v2/project/mixsets/version";
    private static String latestVersion = null;

    public static void checkForUpdates() {
        if (!Config.getBool("Updater")) {
            return;
        }

        try {
            latestVersion = fetchLatestVersion();
            String currentVersion = Utils.getModVersion();

            if (latestVersion != null && isNewerVersion(latestVersion, currentVersion)) {
                ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
                    if (client != null && MinecraftClient.getInstance().inGameHud != null) {
                        String message;

                        if (Objects.equals(Utils.getLanguage(), "ru_ru")) {
                            message = "Новая версия MixSets " + latestVersion + " уже доступна! (Ваша версия MixSets: " + currentVersion + ").\nОбновиться можно на сайте https://modrinth.com/mod/mixsets";
                        }
                        else {
                            message = "New MixSets " + latestVersion + " version available! (Current: " + currentVersion + ").\nUpdate at https://modrinth.com/mod/mixsets";
                        }

                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(message));
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String fetchLatestVersion() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            JsonArray versions = JsonParser.parseReader(reader).getAsJsonArray();
            if (versions.size() > 0) {
                JsonElement versionElement = versions.get(0).getAsJsonObject().get("version_number");
                return versionElement.getAsString();
            }
        }
        return null;
    }

    private static boolean isNewerVersion(String latestVersion, String currentVersion) {
        String[] latestParts = latestVersion.split("\\.");
        String[] currentParts = currentVersion.split("\\.");

        for (int i = 0; i < Math.min(latestParts.length, currentParts.length); i++) {
            int latestPart = Integer.parseInt(latestParts[i]);
            int currentPart = Integer.parseInt(currentParts[i]);

            if (latestPart > currentPart) {
                return true;
            } else if (latestPart < currentPart) {
                return false;
            }
        }
        return latestParts.length > currentParts.length;
    }
}