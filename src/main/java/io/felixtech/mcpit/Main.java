package io.felixtech.mcpit;

import javax.swing.*;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        try {
            JFrame window = new PlayerInfoWindow();

            SwingUtilities.invokeAndWait(() -> {
                window.setVisible(true);
            });
        } catch(Throwable t) {
            JOptionPane.showMessageDialog(null, "An unknown error occured!\n" + t, "Unknown Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
