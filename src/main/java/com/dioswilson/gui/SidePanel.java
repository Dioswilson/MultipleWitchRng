package com.dioswilson.gui;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    public SidePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        add(new JLabel("CPU Usage"));

        JRadioButton veryLow = new JRadioButton();
        JRadioButton low = new JRadioButton();
        JRadioButton mid = new JRadioButton();
        JRadioButton high = new JRadioButton();
        JRadioButton veryHigh = new JRadioButton();


//        veryLow.addActionListener(e -> {
//            performace.set(0.25);
//        });
//
//        low.addActionListener(e -> {
//            performace.set(1.0);
//        });
//
//        mid.addActionListener(e -> {
//            performace.set(2.0);
//        });
//
//        mid.addActionListener(e -> {
//            performace.set(20.0);
//        });
//
//        mid.addActionListener(e -> {
//            performace.set(140.0);
//        });


        ButtonGroup group = new ButtonGroup();
        group.add(veryLow);
        group.add(low);
        group.add(mid);
        group.add(high);
        group.add(veryHigh);


        JPanel veryLowPanel = new JPanel();
        veryLowPanel.setLayout(new BoxLayout(veryLowPanel,BoxLayout.X_AXIS));

        veryLowPanel.add(veryLow);
        veryLowPanel.add(new JLabel("Very Low"));

        JPanel lowPanel = new JPanel();
        lowPanel.setLayout(new BoxLayout(lowPanel,BoxLayout.X_AXIS));

        lowPanel.add(low);
        lowPanel.add(new JLabel("Low"));


        JPanel midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel,BoxLayout.X_AXIS));

        midPanel.add(mid);
        midPanel.add(new JLabel("Mid"));


        JPanel highPanel = new JPanel();
        highPanel.setLayout(new BoxLayout(highPanel,BoxLayout.X_AXIS));

        highPanel.add(high);
        highPanel.add(new JLabel("High"));


        JPanel veryHighPanel = new JPanel();
        veryHighPanel.setLayout(new BoxLayout(veryHighPanel,BoxLayout.X_AXIS));

        veryHighPanel.add(veryHigh);
        veryHighPanel.add(new JLabel("Very High"));

        veryLowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        lowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        midPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        highPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        veryHighPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(veryLowPanel);
        add(lowPanel);
        add(midPanel);
        add(highPanel);
       add(veryHighPanel);

    }
}
