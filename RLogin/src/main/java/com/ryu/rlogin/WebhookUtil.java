package com.ryu.rlogin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebhookUtil {

    public static void sendWebhook(String webhookUrl, String content) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonPayload = "{\"content\": \"" + content + "\"}";

            OutputStream os = connection.getOutputStream();
            os.write(jsonPayload.getBytes());
            os.flush();
            os.close();

            connection.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
