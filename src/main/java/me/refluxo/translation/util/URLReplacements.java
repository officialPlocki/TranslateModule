package me.refluxo.translation.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLReplacements {

    public String format(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, "UTF-8");
    }

}
