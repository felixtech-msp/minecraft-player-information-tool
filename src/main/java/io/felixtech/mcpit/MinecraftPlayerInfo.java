package io.felixtech.mcpit;

import io.felixtech.mcpit.util.NoSuchPlayerException;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import io.felixtech.mcpit.util.URLReader;
import org.json.*;

/**
 * Java-Interface for the Minecraft Mojang API
 */
public final class MinecraftPlayerInfo {
    private MinecraftPlayerInfo() {}

    /**
     * Gets the current player name of a user.
     * @param name the name of the user
     * @return the current player name
     * @throws IOException thrown if the query failed
     * @throws NoSuchPlayerException thrown if the user doesn't exist
     */
    public static String getCurrentName(String name) throws IOException {
        try {
            JSONObject uuid_at_time = new JSONObject(new URLReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name)).readAll());
            return uuid_at_time.getString("name");
        } catch(JSONException e) {
            throw new NoSuchPlayerException();
        }
    }

    /**
     * Gets the UUID of a user.
     * @param name the name of the user
     * @return the UUID of the user
     * @throws IOException thrown if the query failed
     * @throws NoSuchPlayerException thrown if the user doesn't exist
     */
    public static String getUUID(String name) throws IOException {
        try {
            JSONObject uuid_at_time = new JSONObject(new URLReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name)).readAll());
            return uuid_at_time.getString("id");
        } catch(JSONException e) {
            throw new NoSuchPlayerException();
        }
    }

    /**
     * Checks if the user uses a legacy account.
     * @param name the name of the user
     * @return true if the user has a legacy account, false if the user has a Mojang account or the user doesn't exist
     * @throws IOException thrown if the query failed
     */
    public static boolean getLegacy(String name) throws IOException {
        try {
            JSONObject uuid_at_time = new JSONObject(new URLReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name)).readAll());
            return uuid_at_time.getBoolean("legacy");
        } catch(JSONException e) {
            return false;
        }
    }

    /**
     * Checks if a user has demo status.
     * @param name the name of the user
     * @return true if the user has demo status, false if the user has premium status or the user doesn't exist
     * @throws IOException thrown if the query failed
     */
    public static boolean getDemo(String name) throws IOException {
        try {
            JSONObject uuid_at_time = new JSONObject(new URLReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name)).readAll());
            return uuid_at_time.getBoolean("demo");
        } catch(JSONException e) {
            return false;
        }
    }

    /**
     * Gets all names the user ever had and the change-time.
     * @param uuid the uuid of the user
     * @return a map with the username as key and the time when this username was activated as value. The username with value 0 is the registration name.
     * @throws IOException thrown if the user doesn't exist or the query failed
     */
    public static Map<String, Long> getAllNames(String uuid) throws IOException {
        Map<String, Long> names = new HashMap<>();

        JSONArray all_names = new JSONArray(new URLReader(new URL("https://api.mojang.com/user/profiles/" + uuid + "/names")).readAll());

        for (Object object : all_names) {
            JSONObject jso = (JSONObject) object;
            long changedToAt;

            try {
                changedToAt = jso.getLong("changedToAt");
            } catch(JSONException e) {
                changedToAt = 0;
            }

            names.put(jso.getString("name"), changedToAt);
        }

        return names;
    }

    /**
     * Gets the user-information in the JSON format.
     * @param uuid the uuid of the user
     * @return the user information in the JSON format
     * @throws IOException thrown if the query failed
     * @throws NoSuchPlayerException thrown if the user doesn't exist
     */
    public static JSONObject getInfoJSON(String uuid) throws IOException {
        try {
            JSONObject player_info = new JSONObject(new URLReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid)).readAll());
            JSONArray properties = player_info.getJSONArray("properties");

            for (Object property_object : properties) {
                JSONObject property = (JSONObject) property_object;
                String raw = property.getString("value");
                String data = new String(Base64.getDecoder().decode(raw));
                JSONObject data_json = new JSONObject(data);
                property.put("value", data_json);
            }

            return player_info;
        } catch(JSONException e) {
            throw new NoSuchPlayerException();
        }
    }

    /**
     * Gets information about a users skin and cape.
     * @param uuid the UUID of the user
     * @param property the property (timestamp, profileId, profileName, isPublic, textures(as JSON))
     * @return the value of the property
     * @throws IOException thrown if the property or the user doesn't exist or the query failed
     */
    @Deprecated
    public static JSONObject getTextureInfo(String uuid, String property) throws IOException {
        JSONObject player_info = getInfoJSON(uuid);
        JSONArray player_properties = player_info.getJSONArray("properties");
        JSONObject value;

        int i;
        for (i = 0; i < player_properties.length(); i++) {
            value = player_properties.getJSONObject(i);
            if (value.getString("name").equals("textures")) break;
        }

        value = player_properties.getJSONObject(i).getJSONObject("value");
        return value.getJSONObject(property);
    }
}
