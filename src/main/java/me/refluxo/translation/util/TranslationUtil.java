package me.refluxo.translation.util;

import me.refluxo.moduleloader.util.files.FileBuilder;
import me.refluxo.moduleloader.util.files.YamlConfiguration;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TranslationUtil {

    private final YamlConfiguration yml;
    private final FileBuilder builder;

    public TranslationUtil() {
        builder = new FileBuilder("plugins/ModuleLoader/modules/TranslationModule/translation.keys");
        yml = builder.getYaml();
        if(!builder.getFile().exists()) {
            try {
                builder.getFile().createNewFile();
                if(!yml.isSet("keys")) {
                    yml.set("keys", new ArrayList<String>());
                    builder.save();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTranslation(String key, String defaultGermanTranslation, Lang lang) {
        String rKey = key.replaceAll("\\.", "_");
        if(!yml.getStringList("keys").contains(rKey)) {
            List<String> list = new ArrayList<>(yml.getStringList("keys"));
            list.add(key);
            yml.set("keys", list);
            for(Lang l : Lang.values()) {
                JSONObject obj = null;
                try {
                    obj = readJsonFromUrl("https://api-free.deepl.com/v2/translate?auth_key=5f0cf57e-1314-c2ed-5f64-94047ccde4e7:fx&text=" + defaultGermanTranslation.replaceAll(" ", "%20") + "&target_lang=" + l.name());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert obj != null;
                if(!obj.isEmpty()) {
                    String text = obj.getString("text");
                    yml.set("translation." + l.name() + "." + rKey, text);
                }
            }
            builder.save();
        }
        return yml.getString("translation." + lang.name() + "." + rKey);
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
