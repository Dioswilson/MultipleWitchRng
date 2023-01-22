package com.dioswilson.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ErrorWindow {
    public ErrorWindow(List<String> wrongFields) {

        JFrame frame = new JFrame();
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(3);

        StringBuilder sb = new StringBuilder();

        for (String field : wrongFields) {
            sb.append(field + " is not a number\n");
        }

        JLabel errorLabel = new JLabel(sb.toString());

        frame.add(errorLabel);

        JButton closeButton = new JButton("Close window");

        frame.add(closeButton, BorderLayout.SOUTH);
        closeButton.addActionListener(e -> {
            frame.dispose();

        });

        frame.setVisible(true);

    }
}
