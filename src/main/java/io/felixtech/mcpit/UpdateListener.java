package io.felixtech.mcpit;

import io.felixtech.mcpit.util.NoSuchPlayerException;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.json.JSONObject;

final class UpdateListener extends KeyAdapter implements ActionListener {
    private final PlayerInfoWindow piw;

    UpdateListener(PlayerInfoWindow piw) {
        this.piw = piw;
    }

    @Override public void keyPressed(KeyEvent ev) {
        if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
            clear();
            exec();
        }
    }

    @Override public void actionPerformed(ActionEvent ev) {
        clear();
        exec();
    }

    private void exec() {
        String input = piw.currentName.getText().trim();
        String uuid;
        JSONObject info_json;

        try {
            uuid = MinecraftPlayerInfo.getUUID(input);

            piw.currentName.setText(MinecraftPlayerInfo.getCurrentName(input));

            piw.uuid.setText(uuid);

            if (MinecraftPlayerInfo.getLegacy(input))
                piw.legacy.setText("Minecraft-Account");
            else
                piw.legacy.setText("Mojang-Account");

            if (MinecraftPlayerInfo.getDemo(input))
                piw.demo.setText("Demo Account");
            else
                piw.demo.setText("Paid Account");

            Map<String, Long> allNamesMap = MinecraftPlayerInfo.getAllNames(uuid);
            piw.allNamesModel.clear();

            allNamesMap.keySet().stream().forEach((name) -> {
                long timestamp = allNamesMap.get(name);

                if (timestamp == 0) {
                    piw.allNamesModel.addElement(name);
                } else {
                    Date d = new Date(timestamp);
                    piw.allNamesModel.addElement(name + " (" + d + ")");
                }
            });

            info_json = MinecraftPlayerInfo.getInfoJSON(uuid);
            piw.details.setText(info_json.toString());
            piw.requestTime.setText(getLastSkinChangeTime(info_json).toString());

            try {
                URL url = new URL("https://crafatar.com/renders/body/" + piw.uuid.getText());
                Image skin = ImageIO.read(url);
                piw.skin.setImage(skin);
            } catch (IOException ex) {
                piw.skin.setImage((Image) null);
                System.err.println(ex);
            }
        } catch (NoSuchPlayerException ex) {
            clear();
            JOptionPane.showMessageDialog(piw, "A player with this name doesn't exist!", "Player doesn't exist", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(piw, "Can't retrive user information.\n" + ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* TODO
    private static URL getSkinUrl(JSONObject info) throws IOException {
        if (info == null) throw new IOException();
        try {
            return new URL(info.getJSONArray("properties").getJSONObject(0).getJSONObject("value").getJSONObject("textures").getJSONObject("SKIN").getString("url"));
        } catch(JSONException e) {
            return null;
        }
    }
    */
    private static Date getLastSkinChangeTime(JSONObject info) throws IOException {
        if (info == null) throw new IOException();
        return new Date(info.getJSONArray("properties").getJSONObject(0).getJSONObject("value").getLong("timestamp"));
    }

    private void clear() {
        piw.uuid.setText(null);
        piw.legacy.setText(null);
        piw.demo.setText(null);
        piw.allNamesModel.clear();
        piw.details.setText(null);
        piw.requestTime.setText("- Dump Time -");
        piw.skin.setImage((Image) null);
    }
}
