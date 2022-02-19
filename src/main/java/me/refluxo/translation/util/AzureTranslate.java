package me.refluxo.translation.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class AzureTranslate {

    public String getTranslation(String lang, String text) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/translate")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("to", lang)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \"" + text + "\"}]");
        Request request = new Request.Builder().url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", "10e1e5f480ad4616885787b1a039b34d")
                .addHeader("Ocp-Apim-Subscription-Region", "germanywestcentral")
                .addHeader("Content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String s = prettify(replaceLast(response.body().string().replaceFirst("\\[", "")));
        System.out.println(s);
        JSONArray array = new JSONObject(s).getJSONArray("translations");
        return array.getJSONObject(0).getString("text");
    }

    public HashMap<Lang, String> getTranslations(String text) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/translate")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("to", "de")
                .addQueryParameter("to", "en")
                .addQueryParameter("to", "fr")
                .addQueryParameter("to", "nl")
                .addQueryParameter("to", "es")
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \"" + text + "\"}]");
        Request request = new Request.Builder().url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", "10e1e5f480ad4616885787b1a039b34d")
                .addHeader("Ocp-Apim-Subscription-Region", "germanywestcentral")
                .addHeader("Content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String s = prettify(replaceLast(response.body().string().replaceFirst("\\[", "")));
        System.out.println(s);
        JSONArray array = new JSONObject(s).getJSONArray("translations");
        HashMap<Lang, String> map = new HashMap<>();
        map.put(Lang.DE, array.getJSONObject(0).getString("text"));
        map.put(Lang.EN, array.getJSONObject(1).getString("text"));
        map.put(Lang.FR, array.getJSONObject(2).getString("text"));
        map.put(Lang.NL, array.getJSONObject(3).getString("text"));
        map.put(Lang.ES, array.getJSONObject(4).getString("text"));
        return map;
    }

    private String replaceLast(String text) {
        return text.replaceFirst("(?s)"+ "\\]" +"(?!.*?"+ "\\]" +")", "");
    }

    private String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

}