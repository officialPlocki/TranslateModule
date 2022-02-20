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

    // A singleton.
    private final TranslationModule translationModule;

   // It creates a new TranslationModule and connects to the database.
     public TranslationUtil() {
        this.translationModule = new TranslationModule();
        translationModule.getMySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS languageKeys(langKeys TEXT, language TEXT, translation TEXT);");
        translationModule.getMySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS playerLang(uuid TEXT, language TEXT);");
    }

    /**
     * If the key is not in the dictionary, add it with the default translation
     *
     * @param player The player who's language we're getting
     * @param key The key to look up.
     * @param defaultGermanTranslation The default translation if the key is not found.
     * @return The translation of the key.
     */
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

    /**
     * Inserts a key and its translation into the database
     *
     * @param key The key to be inserted into the database.
     * @param germanTranslation The German translation of the key.
     */
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

    /**
     * It updates the player's language in the database
     *
     * @param player The player that is being updated.
     * @param lang The language you want to set the player to.
     */
    public void updateLanguage(Player player, Lang lang) {
        translationModule.getMySQLService().executeUpdate("UPDATE playerLang SET language = '" + lang.name() + "' WHERE uuid = '" + player.getUniqueId() + "';");
    }

    /**
     * It takes a key and a language and returns the translation of the key in that language
     *
     * @param key The key of the translation you want to get.
     * @param lang The language you want to translate to.
     * @return A String.
     */
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

    /**
     * This function checks if the key is in the database
     *
     * @param key The key to check for.
     * @return A boolean value.
     */
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

    /**
     * It gets the language of the player from the database
     *
     * @param player The player who's language is being changed.
     * @return The language of the player.
     */
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

    /**
     * It takes a string and a language, and returns the translation of the string into the language
     *
     * @param string The string to translate.
     * @param lang The language to translate to.
     * @return The translated string.
     */
    public String translateSingleTime(String string, Lang lang) {
        try {
            return new AzureTranslate().getTranslation(lang.name().toLowerCase(), string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
