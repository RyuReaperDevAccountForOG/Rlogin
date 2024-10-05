package com.ryu.Rlogin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscordWebhook {

    private final String url;
    private final List<EmbedObject> embeds = new ArrayList<>();

    public DiscordWebhook(String url) {
        this.url = url;
    }

    public void addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
    }

    public void execute() {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String payload = constructPayload();
            OutputStream os = connection.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
            os.close();

            connection.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String constructPayload() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"embeds\": [");

        for (EmbedObject embed : embeds) {
            sb.append(embed.toJson()).append(",");
        }

        sb.deleteCharAt(sb.length() - 1);  // Remove the last comma
        sb.append("]}");

        return sb.toString();
    }

    public static class EmbedObject {
        private String title;
        private String description;
        private int color;
        private final List<FieldObject> fields = new ArrayList<>();

        public EmbedObject setTitle(String title) {
            this.title = title;
            return this;
        }

        public EmbedObject setDescription(String description) {
            this.description = description;
            return this;
        }

        public EmbedObject setColor(int color) {
            this.color = color;
            return this;
        }

        public EmbedObject addField(String name, String value, boolean inline) {
            this.fields.add(new FieldObject(name, value, inline));
            return this;
        }

        public String toJson() {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"title\": \"").append(title).append("\",");
            sb.append("\"description\": \"").append(description).append("\",");
            sb.append("\"color\": ").append(color).append(",");
            sb.append("\"fields\": [");

            for (FieldObject field : fields) {
                sb.append(field.toJson()).append(",");
            }

            sb.deleteCharAt(sb.length() - 1);  // Remove the last comma
            sb.append("]}");

            return sb.toString();
        }

        private static class FieldObject {
            private final String name;
            private final String value;
            private final boolean inline;

            public FieldObject(String name, String value, boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }

            public String toJson() {
                return "{\"name\": \"" + name + "\", \"value\": \"" + value + "\", \"inline\": " + inline + "}";
            }
        }
    }
}
