package com.dioswilson.gui;

import com.dioswilson.SeedFinder;
import com.dioswilson.minecraft.Chunk;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class InputValuesPanel extends JPanel {

    private JFormattedTextField seedTextField;
    private JFormattedTextField wh1X;
    private JFormattedTextField wh1Z;
    private JFormattedTextField wh2X;
    private JFormattedTextField wh2Z;
    private JFormattedTextField wh3X;
    private JFormattedTextField wh3Z;
    private JFormattedTextField wh4X;
    private JFormattedTextField wh4Z;
    private JFormattedTextField playerX;
    private JFormattedTextField playerZ;
    public static JButton searchButton;
    private List<Chunk> witchChunks = new ArrayList<>();

    //Todo checkboxes seleccionando CPU usage
    public InputValuesPanel() throws HeadlessException {

        this.setLayout(new BorderLayout());

//        Toolkit.getDefaultToolkit().getScreenSize();
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);

        JPanel topPanel = new JPanel();
        this.seedTextField = new JFormattedTextField(numberFormat);
        this.seedTextField.setColumns(15);
        this.seedTextField.setText("Enter seed here");

        topPanel.add(this.seedTextField);
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

        wh1X = new JFormattedTextField(numberFormat);
        wh1X.setColumns(5);
        wh1X.setHorizontalAlignment(JTextField.CENTER);

        witchInject.add(wh1X, c);


        c.gridx = 1;
        c.gridy = 1;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 2;
        wh1Z = new JFormattedTextField(numberFormat);
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
        wh2X = new JFormattedTextField(numberFormat);
        wh2X.setColumns(5);
        wh2X.setHorizontalAlignment(JTextField.CENTER);


        witchInject.add(wh2X, c);

//
        c.gridx = 4;
        c.gridy = 1;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 2;

        wh2Z = new JFormattedTextField(numberFormat);
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

        wh3X = new JFormattedTextField(numberFormat);
        wh3X.setColumns(5);
        wh3X.setHorizontalAlignment(JTextField.CENTER);

        witchInject.add(wh3X, c);


        c.gridx = 1;
        c.gridy = 4;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 5;

        wh3Z = new JFormattedTextField(numberFormat);
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

        wh4X = new JFormattedTextField(numberFormat);
        wh4X.setColumns(5);
        wh4X.setHorizontalAlignment(JTextField.CENTER);


        witchInject.add(wh4X, c);

//
        c.gridx = 4;
        c.gridy = 4;

        witchInject.add(new JLabel("PosZ", SwingConstants.CENTER), c);

        c.gridy = 5;

        wh4Z = new JFormattedTextField(numberFormat);
        wh4Z.setColumns(5);
        wh4Z.setHorizontalAlignment(JTextField.CENTER);

        witchInject.add(wh4Z, c);

        add(witchInject, BorderLayout.CENTER);


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

        this.playerX = new JFormattedTextField(numberFormat);
        this.playerX.setColumns(5);
        this.playerX.setHorizontalAlignment(JTextField.CENTER);

        bottom.add(playerX, c);

        c.gridx = 2;

        bottom.add(new JLabel("Z:"), c);

        c.gridx = 3;

        this.playerZ = new JFormattedTextField(numberFormat);
        this.playerZ.setColumns(5);
        this.playerZ.setHorizontalAlignment(JTextField.CENTER);

        bottom.add(playerZ, c);

        c.gridx = 5;

        searchButton = new JButton("Start");

        AtomicReference<Double> performace = new AtomicReference<>((double) 1);

        searchButton.addActionListener((e -> {

            if (!SeedFinder.running) {
                if (seedTextField.getValue() != null) {
                    retireveWithChunks();
                    if (playerX.getValue() != null && playerZ.getValue() != null) {
                        if (witchChunks.size() > 0) {
                            SeedFinder.running = true;
                            SeedFinder.stop = false;
                            searchButton.setText("Stop");
                            ResultsPanel.model.setRowCount(0);
                            new SeedFinder((int) (long) playerX.getValue(), (int) (long) playerZ.getValue(), (int) (long) maxAdvancers.getValue(), witchChunks, (long) seedTextField.getValue(), performace).start();
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
            }
            else {
                SeedFinder.stop = true;
                SeedFinder.running = false;
                searchButton.setText("Search");
            }
        }));

        bottom.add(searchButton, c);
        add(bottom, BorderLayout.SOUTH);


        JPanel performancePanel = new JPanel();
        performancePanel.setLayout(new BoxLayout(performancePanel, BoxLayout.Y_AXIS));
        performancePanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        performancePanel.add(new JLabel("CPU Usage"));

        JRadioButton veryLow = new JRadioButton();
        JRadioButton low = new JRadioButton();
        JRadioButton mid = new JRadioButton();
        JRadioButton high = new JRadioButton();
        JRadioButton veryHigh = new JRadioButton();


        veryLow.addActionListener(e -> {
            performace.set(0.25);
        });

        low.addActionListener(e -> {
            performace.set(1.0);
        });

        mid.addActionListener(e -> {
            performace.set(2.0);
        });

        mid.addActionListener(e -> {
            performace.set(20.0);
        });

        mid.addActionListener(e -> {
            performace.set(140.0);
        });


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

        performancePanel.add(veryLowPanel);
        performancePanel.add(lowPanel);
        performancePanel.add(midPanel);
        performancePanel.add(highPanel);
        performancePanel.add(veryHighPanel);

        add(performancePanel, BorderLayout.WEST);

        JPanel dummyPanel = new JPanel();
        dummyPanel.setBorder(BorderFactory.createEmptyBorder(0,75,0,0));
        add(dummyPanel,BorderLayout.EAST);
        this.setVisible(true);
    }

    private void retireveWithChunks() {
        this.witchChunks.clear();
        if (wh1X.getValue() != null && wh1Z.getValue() != null) {
            this.witchChunks.add(new Chunk((int) (long) wh1X.getValue(), (int) (long) wh1Z.getValue()));
        }
        if (wh2X.getValue() != null && wh2Z.getValue() != null) {
            this.witchChunks.add(new Chunk((int) (long) wh2X.getValue(), (int) (long) wh2Z.getValue()));
        }
        if (wh3X.getValue() != null && wh3Z.getValue() != null) {
            this.witchChunks.add(new Chunk((int) (long) wh3X.getValue(), (int) (long) wh3Z.getValue()));
        }
        if (wh4X.getValue() != null && wh4Z.getValue() != null) {
            this.witchChunks.add(new Chunk((int) (long) wh4X.getValue(), (int) (long) wh4Z.getValue()));
        }
    }


    public long getSeed() {
        return (long) seedTextField.getValue();
    }

    public int getPlayerX() {
        return (int) (long) this.playerX.getValue();
    }

    public int getPlayerZ() {
        return (int) (long) this.playerZ.getValue();
    }

    public List<Chunk> getWitchChunks() {
//        if (this.witchChunks.isEmpty()) {
            retireveWithChunks();
//        }
        return witchChunks;
    }
}
