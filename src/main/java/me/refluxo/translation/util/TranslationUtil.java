package me.refluxo.translation.util;

import me.refluxo.translation.TranslationModule;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class TranslationUtil {

    private final TranslationModule translationModule;

    public TranslationUtil() {
        this.translationModule = new TranslationModule();
        translationModule.getMySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS languageKeys(langKeys TEXT, language TEXT, translation TEXT);");
        translationModule.getMySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS playerLang(uuid TEXT, language TEXT);");
    }

    public String getTranslation(Player player, String key, String defaultGermanTranslation) {
        String rKey = key.replaceAll("\\.", "_");
        if(!containsKey(rKey)) {
            insertKey(rKey, defaultGermanTranslation);
        }
        return getTranslation(rKey, getLanguage(player));
    }

    /**
     *
     * @param player
     * @param key
     * @param defaultGermanTranslation
     * @param replacements do %p for placeholder in text and put the replacements for the placeholders in the replacements String List
     * @return
     */
    public String getTranslation(Player player, String key, String defaultGermanTranslation, String... replacements) {
        String rKey = key.replaceAll("\\.", "_");
        if(!containsKey(rKey)) {
            insertKey(rKey, defaultGermanTranslation);
        }
        String trans = getTranslation(rKey, getLanguage(player));
        for (String replacement : replacements) {
            trans = trans.replaceFirst("%p", replacement);
        }
        return trans;
    }

    private void insertKey(String key, String germanTranslation) {
        HashMap<Lang, String> map = null;
        try {
            map = new AzureTranslate().getTranslations(germanTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Lang l : Lang.values()) {
            assert map != null;
            translationModule.getMySQLService().executeUpdate("INSERT INTO languageKeys(langKeys,language,translation) VALUES ('" + key + "','" + l.name() + "','" + map.get(l) + "');");
        }
    }

    public void updateLanguage(Player player, Lang lang) {
        translationModule.getMySQLService().executeUpdate("UPDATE playerLang SET language = '" + lang.name() + "' WHERE uuid = '" + player.getUniqueId() + "';");
    }

    private String getTranslation(@NotNull String key, @NotNull Lang lang) {
        try (Connection connection = translationModule.getMySQLService().getConnection()){
            ResultSet rs =  connection.prepareStatement("SELECT * FROM languageKeys WHERE langKeys = '" + key + "' AND language = '" + lang.name() + "';").executeQuery();
            if(rs.next()) {
                return rs.getString("translation");
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private boolean containsKey(String key) {
        try (Connection connection = translationModule.getMySQLService().getConnection()){
            ResultSet rs =  connection.prepareStatement("SELECT * FROM languageKeys WHERE langKeys = '" + key + "';").executeQuery();
            if(rs == null) {
                return false;
            }
            try {
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Lang getLanguage(Player player) {
        try (Connection connection = translationModule.getMySQLService().getConnection()){
            ResultSet rs =  connection.prepareStatement("SELECT * FROM playerLang WHERE uuid = '" + player.getUniqueId() + "';").executeQuery();
            if(rs.next()) {
                return Lang.valueOf(rs.getString("language"));
            } else {
                player.sendMessage("§b§lTranslations §8» §7Deine Sprache wurde auf Englisch eingestellt. Konfiguriere diese mit /language.");
                player.sendMessage("§b§lTranslations §8» §7Your language has been set to English. Configure it with /language.");
                player.sendMessage("§b§lTranslations §8» §7Ta langue a été configurée en anglais. Configure-la avec /language.");
                player.sendMessage("§b§lTranslations §8» §7Uw taal is ingesteld op Engels. Configureer het met /language.");
                player.sendMessage("§b§lTranslations §8» §7Su idioma se ha establecido en inglés. Configúralo con /language.");
                translationModule.getMySQLService().executeUpdate("INSERT INTO playerLang(uuid,language) VALUES ('" + player.getUniqueId() + "','EN');");
                return Lang.EN;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Lang.EN;
    }

    public String translateSingleTime(String string, Lang lang) {
        try {
            return new AzureTranslate().getTranslation(lang.name().toLowerCase(), string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
