package com.dioswilson;

import com.dioswilson.gui.GenerateLitematicPanel;
import com.dioswilson.gui.InputValuesPanel;
import com.dioswilson.gui.ResultsPanel;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(675, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("MultipleWitchHutFinder");
//            frame.setLayout(new FlowLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
//        tabbedPane.setLayout(new FlowLayout());
        InputValuesPanel inputValuesPanel = new InputValuesPanel();

        tabbedPane.add("Input Values", inputValuesPanel);
        tabbedPane.add("Results", new ResultsPanel(inputValuesPanel));
        tabbedPane.add("Generate Litematic", new GenerateLitematicPanel(inputValuesPanel));

        tabbedPane.setVisible(true);
        frame.add(tabbedPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


}