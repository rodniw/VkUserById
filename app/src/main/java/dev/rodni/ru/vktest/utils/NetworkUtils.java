package dev.rodni.ru.vktest.utils;

import android.net.Uri;

import java.net.URL;

public class NetworkUtils {

    public static URL generateURL(String userId) {
        Uri builtURI = Uri.parse("https://api.vk.com" + "/method/users.get");

        return URL;
    }
}
