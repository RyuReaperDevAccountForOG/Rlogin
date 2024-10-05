package com.ryu.rlogin;

import org.mindrot.jbcrypt.BCrypt;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private static final HashMap<UUID, String> playerPasswords = new HashMap<>();

    // Hash and store a player's password
    public static void setPassword(UUID uuid, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        playerPasswords.put(uuid, hashed);
    }

    // Check if the password matches
    public static boolean checkPassword(UUID uuid, String password) {
        String hashed = playerPasswords.get(uuid);
        return hashed != null && BCrypt.checkpw(password, hashed);
    }

    // Simulate loading data (in reality, you'd load from a file or database)
    public static void loadData() {
        // Load player data from storage
    }

    // Simulate saving data (in reality, you'd save to a file or database)
    public static void saveData() {
        // Save player data to storage
    }

    // Check if player is registered
    public static boolean isRegistered(UUID uuid) {
        return playerPasswords.containsKey(uuid);
    }
}
