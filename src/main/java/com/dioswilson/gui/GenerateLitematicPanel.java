package com.dioswilson.gui;

import com.dioswilson.SeedFinder;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class GenerateLitematicPanel extends JPanel {

    JFormattedTextField advancersTextField;
    JFormattedTextField fromXTextField;
    JFormattedTextField fromZTextField;

    public GenerateLitematicPanel(InputValuesPanel dataPanel) {//This class is to generate litematic/fill table

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);

        JLabel warningLabel = new JLabel("Please fill Input values tab first");
        warningLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        warningLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
        add(warningLabel);

//        add(new JLabel("Advancers"));
        //add horizontal box layout or flex to this
        JPanel advancersPanel = new JPanel();
        advancersPanel.setLayout(new BoxLayout(advancersPanel, BoxLayout.X_AXIS));
        advancersPanel.add(new JLabel("Advancers: "));
        advancersTextField = new JFormattedTextField(numberFormat);
        advancersTextField.setMaximumSize(new Dimension(200, 20));
        advancersTextField.setValue(0L);
        advancersPanel.add(advancersTextField);
        add(advancersPanel);


        JPanel fromXPanel = new JPanel();
        fromXPanel.setLayout(new BoxLayout(fromXPanel, BoxLayout.X_AXIS));
        fromXPanel.add(new JLabel("From X:        "));//ew, but lazy
        fromXTextField = new JFormattedTextField(numberFormat);
        fromXTextField.setMaximumSize(new Dimension(200, 20));
        fromXTextField.setValue(0L);
        fromXPanel.add(fromXTextField);
        add(fromXPanel);



        JPanel fromZPanel = new JPanel();
        fromZPanel.setLayout(new BoxLayout(fromZPanel, BoxLayout.X_AXIS));
        fromZPanel.add(new JLabel("From Z:        "));//ew, but lazy
        fromZTextField = new JFormattedTextField(numberFormat);
        fromZTextField.setMaximumSize(new Dimension(200, 20));
        fromZTextField.setValue(0L);
        fromZPanel.add(fromZTextField);
        add(fromZPanel);

        JPanel buttonsPanel = new JPanel();
        JButton generateLitematicButton = new JButton("Generate Litematic");

        generateLitematicButton.addActionListener(e -> {
            try {
                new SeedFinder(dataPanel.getPlayerX(), dataPanel.getPlayerZ(), (int) (long) advancersTextField.getValue(), dataPanel.getWitchChunks(), dataPanel.getSeed(), (int) (long) fromXTextField.getValue(), (int) (long) fromZTextField.getValue(), dataPanel.getMaxPlayers()).start();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(generateLitematicButton, "Insert values at \"Input Values\" tab");
            }
        });
        buttonsPanel.add(generateLitematicButton);

        JButton appendToTable = new JButton("Append To Table");

        appendToTable.addActionListener(e -> {
            try {

                SeedFinder seedFinder = new SeedFinder(dataPanel.getPlayerX(), dataPanel.getPlayerZ(), (int) (long) advancersTextField.getValue(), dataPanel.getWitchChunks(), dataPanel.getSeed(), dataPanel.getMaxPlayers());
                SeedFinder.stop = false;
                seedFinder.getChunksForSpawning();
                seedFinder.setRandomSeed((int) (long) fromXTextField.getValue() / 1280, (int) (long) fromZTextField.getValue() / 1280);
                SeedFinder.stop = true;
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(generateLitematicButton, "Insert values at \"Input Values\" tab");
            }
        });
        buttonsPanel.add(appendToTable);

        add(buttonsPanel);
    }
}
