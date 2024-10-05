package com.ryu.rlogin;

import org.json.JSONObject;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MojangAPIUtil {

    // Checks if a player is a premium Mojang account
    public static boolean isPremiumPlayer(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == 200) {
                // If the response is 200, the player is a premium player
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                Scanner scanner = new Scanner(reader);
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.next());
                }
                scanner.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.has("id");  // Premium players have an ID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
