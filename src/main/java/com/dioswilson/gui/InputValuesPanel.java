package com.dioswilson.gui;

import com.dioswilson.Main;
import com.dioswilson.SeedFinder;
import com.dioswilson.minecraft.Chunk;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class InputValuesPanel extends JPanel {

    public static JButton searchButton;

    public InputValuesPanel() throws HeadlessException {

        this.setLayout(new BorderLayout());

//        Toolkit.getDefaultToolkit().getScreenSize();
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);

        JPanel topPanel = new JPanel();
        JFormattedTextField seedTextField = new JFormattedTextField(numberFormat);
        seedTextField.setColumns(15);
        seedTextField.setText("Enter seed here");

        topPanel.add(seedTextField);
        topPanel.add(new JLabel("Max advancers: "));
        JFormattedTextField maxAdvancers = new JFormattedTextField(numberFormat);
        maxAdvancers.setValue(0L);
        maxAdvancers.setPreferredSize(new Dimension(50, 20));
        maxAdvancers.setHorizontalAlignment(JTextField.CENTER);
        topPanel.add(maxAdvancers);

//        topPanel.setLayout(new FlowLayout());


        add(topPanel, BorderLayout.NORTH);


        JPanel witchInject = new JPanel();
        witchInject.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;

        witchInject.add(new JLabel("Wich Hut 1", SwingConstants.CENTER), c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;

        witchInject.add(new JLabel("PosX", SwingConstants.CENTER), c);

        c.gridy = 2;

        JFormattedTextField wh1X = new JFormattedTextField(numberFormat);
        wh1X.setColumns(5);
        wh1X.setHorizontalAlignment(JTextField.CENTER);

        witchInject.add(wh1X, c);


        c.gridx = 1;
        c.gridy = 1;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 2;
        JFormattedTextField wh1Z = new JFormattedTextField(numberFormat);
        wh1Z.setColumns(5);
        wh1Z.setHorizontalAlignment(JTextField.CENTER);


        witchInject.add(wh1Z, c);


        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 2;

        witchInject.add(new JLabel("Wich Hut 2", SwingConstants.CENTER), c);

        c.gridwidth = 1;
        c.gridy = 1;

        witchInject.add(new JLabel("PosX", SwingConstants.CENTER), c);

        c.gridy = 2;
        JFormattedTextField wh2X = new JFormattedTextField(numberFormat);
        wh2X.setColumns(5);
        wh2X.setHorizontalAlignment(JTextField.CENTER);


        witchInject.add(wh2X, c);

//
        c.gridx = 4;
        c.gridy = 1;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 2;

        JFormattedTextField wh2Z = new JFormattedTextField(numberFormat);
        wh2Z.setColumns(5);
        wh2Z.setHorizontalAlignment(JTextField.CENTER);

        witchInject.add(wh2Z, c);


        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;

        witchInject.add(new JLabel("Wich Hut 3", SwingConstants.CENTER), c);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;

        witchInject.add(new JLabel("PosX", SwingConstants.CENTER), c);

        c.gridy = 5;

        JFormattedTextField wh3X = new JFormattedTextField(numberFormat);
        wh3X.setColumns(5);
        wh3X.setHorizontalAlignment(JTextField.CENTER);

        witchInject.add(wh3X, c);


        c.gridx = 1;
        c.gridy = 4;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 5;

        JFormattedTextField wh3Z = new JFormattedTextField(numberFormat);
        wh3Z.setColumns(5);
        wh3Z.setHorizontalAlignment(JTextField.CENTER);


        witchInject.add(wh3Z, c);


        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 2;

        witchInject.add(new JLabel("Wich Hut 4", SwingConstants.CENTER), c);

        c.gridwidth = 1;
        c.gridy = 4;

        witchInject.add(new JLabel("PosX", SwingConstants.CENTER), c);

        c.gridy = 5;

        JFormattedTextField wh4X = new JFormattedTextField(numberFormat);
        wh4X.setColumns(5);
        wh4X.setHorizontalAlignment(JTextField.CENTER);


        witchInject.add(wh4X, c);

//
        c.gridx = 4;
        c.gridy = 4;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 5;

        JFormattedTextField wh4Z = new JFormattedTextField(numberFormat);
        wh4Z.setColumns(5);
        wh4Z.setHorizontalAlignment(JTextField.CENTER);

        witchInject.add(wh4Z, c);


        add(witchInject);


        JPanel bottom = new JPanel();

        bottom.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;

        bottom.add(new JLabel("Player Position: ", SwingConstants.CENTER), c);

        c.gridwidth = 1;
        c.gridy = 1;

        bottom.add(new JLabel("X:"), c);

        c.gridx = 1;

        JFormattedTextField playerX = new JFormattedTextField(numberFormat);
        playerX.setColumns(5);
        playerX.setHorizontalAlignment(JTextField.CENTER);

        bottom.add(playerX, c);

        c.gridx = 2;

        bottom.add(new JLabel("Z:"), c);

        c.gridx = 3;

        JFormattedTextField playerZ = new JFormattedTextField(numberFormat);
        playerZ.setColumns(5);
        playerZ.setHorizontalAlignment(JTextField.CENTER);

        bottom.add(playerZ, c);

        c.gridx = 5;

        searchButton = new JButton("Start");


        searchButton.addActionListener((e -> {

            if (!SeedFinder.running) {
                if (seedTextField.getValue() != null) {
                    List<Chunk> witchChunks = new ArrayList<>();
                    if (wh1X.getValue() != null && wh1Z.getValue() != null) {
                        witchChunks.add(new Chunk((int) (long) wh1X.getValue(), (int) (long) wh1Z.getValue()));
                    }
                    if (wh2X.getValue() != null && wh2Z.getValue() != null) {
                        witchChunks.add(new Chunk((int) (long) wh2X.getValue(), (int) (long) wh2Z.getValue()));
                    }
                    if (wh3X.getValue() != null && wh3Z.getValue() != null) {
                        witchChunks.add(new Chunk((int) (long) wh3X.getValue(), (int) (long) wh3Z.getValue()));
                    }
                    if (wh4X.getValue() != null && wh4Z.getValue() != null) {
                        witchChunks.add(new Chunk((int) (long) wh4X.getValue(), (int) (long) wh4Z.getValue()));
                    }
                    if (playerX.getValue() != null && playerZ.getValue() != null) {
                        if (witchChunks.size() > 0) {
                            SeedFinder.running = true;
                            SeedFinder.stop = false;
                            searchButton.setText("Stop");
                            new SeedFinder((int) (long) playerX.getValue(), (int) (long) playerZ.getValue(), (int) (long) maxAdvancers.getValue(), witchChunks, (long) seedTextField.getValue()).start();
                        }
                        else {
                            JOptionPane.showMessageDialog(searchButton, "Input witch huts' chunk coords");
                        }

                    }
                    else {
                        JOptionPane.showMessageDialog(searchButton, "Input player coords");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(searchButton, "You need to insert a seed");

                }


//                new SeedFinder(playerX, playerZ, maxAdvancers, );
//                   (new Main()).Main();
//                    Main.stop = false;
//                    Main.running = true;
//                    searchButton.setText("Stop");
//                    try {
//                        Main.textArea.setText("");
//                        String seedField = Main.seedTextField.getText();
//                        Main.seed = Long.valueOf(Long.parseLong(seedField)).longValue();
//                    } catch (Exception exception) {
//                        searchButton.setText("Search");
//                        Main.running = false;
//                        Main.textArea.setText("That is not a seed you mongrel.");
//                    }
            }
            else {
                SeedFinder.stop = true;
                SeedFinder.running = false;
                searchButton.setText("Search");
            }
        }));

        bottom.add(searchButton, c);
        add(bottom, BorderLayout.SOUTH);

        this.setVisible(true);
    }
}
