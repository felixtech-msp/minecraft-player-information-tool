package io.felixtech.mcpit;

import io.felixtech.mcpit.util.ImagePanel;
import io.felixtech.mcpit.util.MaxSizeDocument;

import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.VERTICAL;

import javax.swing.*;

public final class PlayerInfoWindow extends JFrame {
    final JTextField currentName, uuid, legacy, demo, requestTime;
    private final JList<String> allNames;
    final DefaultListModel<String> allNamesModel;
    final JTextArea details;
    ImagePanel skin = null;
    private final JScrollPane allNamesScrollPane, detailsScrollPane;
    private final JButton ok;

    private final UpdateListener listener;

    public PlayerInfoWindow() {
        super("Minecraft Player Information Tool 2.0");

        listener = new UpdateListener(this);

        currentName = new JTextField();
        currentName.setDocument(new MaxSizeDocument(50));
        currentName.addKeyListener(listener);

        uuid = new JTextField();
        uuid.setEditable(false);

        legacy = new JTextField();
        legacy.setEditable(false);

        demo = new JTextField();
        demo.setEditable(false);

        requestTime = new JTextField("- Dump Time -");
        requestTime.setEditable(false);
        requestTime.setColumns(18);
        requestTime.setHorizontalAlignment(JTextField.CENTER);

        allNamesModel = new DefaultListModel();

        allNames = new JList<>(allNamesModel);
        allNames.setDragEnabled(false);
        allNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        allNamesScrollPane = new JScrollPane(allNames);

        details = new JTextArea(8, 30);
        details.setEditable(false);
        details.setLineWrap(true);

        detailsScrollPane = new JScrollPane(details);

        ok = new JButton("Get player information!");
        ok.addActionListener(listener);

        skin = new ImagePanel((Image) null);

        super.setResizable(false);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLocationRelativeTo(null);

        createUI();
        super.pack();
    }

    private void createUI() {
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        add(gbl, new JLabel("Player Name:"),    0, 0, 1, 1);
        add(gbl, currentName,                   1, 0, 1, 1);

        add(gbl, new JLabel("UUID:"),           0, 1, 1, 1);
        add(gbl, uuid,                          1, 1, 1, 1);

        add(gbl, new JLabel("Account-Type:"),   0, 2, 1, 1);
        add(gbl, legacy,                        1, 2, 1, 1);

        add(gbl, new JLabel("Demo-Status:"),    0, 3, 1, 1);
        add(gbl, demo,                          1, 3, 1, 1);

        add(gbl, new JLabel("All Player Names:"),   0, 4, 1, 1);
        add(gbl, allNamesScrollPane,                1, 4, 1, 3);

        add(gbl, new JLabel("Details:"),    0, 7, 1, 1);
        add(gbl, detailsScrollPane,         1, 7, 1, 3);

        add(gbl, ok, 0, 11, 2, 1);

        add(gbl, skin, 2, 0, 2, 10, BOTH, CENTER);

        add(gbl, requestTime, 2, 11, 2, 1, VERTICAL);
    }

    private void add(GridBagLayout gbl, Component component, int x, int y, int width, int height) {
        add(gbl, component, x, y, width, height, HORIZONTAL);
    }

    private void add(GridBagLayout gbl, Component component, int x, int y, int width, int height, int fill) {
        add(gbl, component, x, y, width, height, fill, NORTH);
    }

    private void add(GridBagLayout gbl, Component component, int x, int y, int width, int height, int fill, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbl.setConstraints(component, gbc);
        add(component);
    }
}
