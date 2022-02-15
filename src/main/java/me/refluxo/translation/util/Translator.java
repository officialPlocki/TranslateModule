package me.refluxo.translation.util;

import me.refluxo.moduleloader.service.Service;
import org.bukkit.entity.Player;

public interface Translator extends Service {

    String getTranslation(Player player, String key, String defaultGermanTranslation);

    void updateLanguage(Player player, Lang lang);

    Lang getLanguage(Player player);
}
