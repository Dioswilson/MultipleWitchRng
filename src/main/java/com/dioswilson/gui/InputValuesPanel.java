package com.dioswilson.gui;

import com.dioswilson.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class InputValuesPanel extends JPanel {


    private JTextField seedField;
    private JTextField playerX;
    private JTextField playerZ;

    private JTextField witch1X;
    private JTextField witch1Z;

    private JTextField witch2X;
    private JTextField witch2Z;

    private JTextField witch3X;
    private JTextField witch3Z;

    private JTextField witch4X;
    private JTextField witch4Z;

    public InputValuesPanel() throws HeadlessException {

        this.setLayout(new BorderLayout());

//        Toolkit.getDefaultToolkit().getScreenSize();

        JPanel topPanel = new JPanel();
        JTextField seedTextField = new JTextField("Enter Seed Here", 15);
        topPanel.add(seedTextField);
        topPanel.add(new JLabel("Max advancers: "));
        JTextField maxAdvancers = new JTextField("0");
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

        JTextField wh1X = new JTextField("0", 4);
        wh1X.setHorizontalAlignment(JTextField.CENTER);

        witchInject.add(wh1X, c);


        c.gridx = 1;
        c.gridy = 1;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 2;
        JTextField wh1Z = new JTextField("0", 4);
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
        JTextField wh2X = new JTextField("0", 4);
        wh2X.setHorizontalAlignment(JTextField.CENTER);


        witchInject.add(wh2X, c);

//
        c.gridx = 4;
        c.gridy = 1;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 2;

        JTextField wh2Z = new JTextField("0", 4);
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

        JTextField wh3X = new JTextField("0", 4);
        wh3X.setHorizontalAlignment(JTextField.CENTER);

        witchInject.add(wh3X, c);


        c.gridx = 1;
        c.gridy = 4;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 5;
        JTextField wh3Z = new JTextField("0", 4);
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
        JTextField wh4X = new JTextField("0", 4);
        wh4X.setHorizontalAlignment(JTextField.CENTER);


        witchInject.add(wh4X, c);

//
        c.gridx = 4;
        c.gridy = 4;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 5;

        JTextField wh4Z = new JTextField("0", 4);
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

        JTextField playerX = new JTextField("0", 5);
        playerX.setHorizontalAlignment(JTextField.CENTER);

        bottom.add(playerX, c);

        c.gridx = 2;

        bottom.add(new JLabel("Z:"), c);

        c.gridx = 3;

        JTextField playerZ = new JTextField("0", 5);
        playerZ.setHorizontalAlignment(JTextField.CENTER);

        bottom.add(playerZ, c);

        c.gridx = 5;

        JButton searchButton = new JButton("Start");

        searchButton.addActionListener((e -> {

            if (!Main.running) {

                List<String> errors = new ArrayList<>();


                try {
                    String seedField = seedTextField.getText();
                    Main.seed = Long.valueOf(Long.parseLong(seedField)).longValue();
                } catch (Exception exception) {
                    errors.add("seed");
                }
                try {
                    String advancersField = maxAdvancers.getText();

                } catch (Exception exception) {
                }


                if (errors.size() > 0) {
                    new ErrorWindow(errors);
                }


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
                Main.stop = true;
                searchButton.setText("Search");
            }
        }));

        bottom.add(searchButton, c);
        add(bottom, BorderLayout.SOUTH);

        this.setVisible(true);
    }
}
