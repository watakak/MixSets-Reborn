package net.watakak;

import java.util.HashMap;
import java.util.Map;

public class Placeholders {

    // Метод для обработки строки с плейсхолдерами
    public static String parse(String input) {
        Map<String, String> placeholders = new HashMap<>();

        // Получаем значения для различных плейсхолдеров
        placeholders.put("loader", Utils.getLoader());
        placeholders.put("player", Utils.getPlayerNickname());
        placeholders.put("minecraft_version", Utils.getMinecraftVersion());
        placeholders.put("mod_version", Utils.getModVersion());
        placeholders.put("language", Utils.getLanguage());

        // Заменяем каждый плейсхолдер значением
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            input = input.replace("{" + entry.getKey() + "}", entry.getValue() != null ? entry.getValue() : "");
        }

        return input;
    }
}
