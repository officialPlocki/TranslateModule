package me.refluxo.translation.util;

import me.refluxo.translation.TranslationModule;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TranslationUtil {

    private TranslationModule translationModule;

    public TranslationUtil() {
        this.translationModule = new TranslationModule();
    }

    public String getTranslation(Player player, String key, String defaultGermanTranslation) {
        String rKey = key.replaceAll("\\.", "_");
        if(!containsKey(rKey)) {
            insertKey(rKey, defaultGermanTranslation);
        }
        return getTranslation(rKey, getLanguage(player));
    }

    private void insertKey(String key, String germanTranslation) {
        for(Lang l : Lang.values()) {
            JSONObject obj = null;
            try {
                obj = readJsonFromUrl("https://api-free.deepl.com/v2/translate?auth_key=5f0cf57e-1314-c2ed-5f64-94047ccde4e7:fx&text=" + new URLReplacements().format(germanTranslation) + "&target_lang=" + l.name());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert obj != null;
            if(!obj.isEmpty()) {
                String text = obj.getString("text");
                translationModule.getMySQLService().executeUpdate("INSERT INTO languageKeys(langKeys,language,translation) VALUES ('" + key + "','" + l.name() + "','" + text + "');");
            }
        }
    }

    public void updateLanguage(Player player, Lang lang) {
        translationModule.getMySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS playerLang(uuid TEXT, language TEXT);");
        translationModule.getMySQLService().executeUpdate("UPDATE playerLang SET language = '" + lang.name() + "' WHERE uuid = '" + player.getUniqueId() + "';");
    }

    private String getTranslation(@NotNull String key, @NotNull Lang lang) {
        translationModule.getMySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS languageKeys(langKeys TEXT, language TEXT, translation TEXT);");
        ResultSet rs = translationModule.getMySQLService().getResult("SELECT * FROM languageKeys WHERE langKeys = '" + key + "' AND language = '" + lang.name() + "';");
        try {
            if(rs.next()) {
                return rs.getString("translation");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean containsKey(String key) {
        translationModule.getMySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS languageKeys(langKeys TEXT, language TEXT, translation TEXT);");
        ResultSet rs = translationModule.getMySQLService().getResult("SELECT * FROM languageKeys WHERE langKeys = '" + key + "';");
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Lang getLanguage(Player player) {
        translationModule.getMySQLService().executeUpdate("CREATE TABLE IF NOT EXISTS playerLang(uuid TEXT, language TEXT);");
        ResultSet rs = translationModule.getMySQLService().getResult("SELECT * FROM playerLang WHERE uuid = '" + player.getUniqueId() + "';");
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Lang.EN;
    }

    public String translateSingleTime(String string, Lang lang) {
        JSONObject obj = null;
        try {
            obj = readJsonFromUrl("https://api-free.deepl.com/v2/translate?auth_key=5f0cf57e-1314-c2ed-5f64-94047ccde4e7:fx&text=" + new URLReplacements().format(string) + "&target_lang=" + lang.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert obj != null;
        if(!obj.isEmpty()) {
            return obj.getString("text");
        }
        return "";
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private JSONObject readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

}
