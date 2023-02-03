package com.dioswilson;

import com.dioswilson.gui.GenerateLitematicPanel;
import com.dioswilson.gui.InputValuesPanel;
import com.dioswilson.gui.ResultsPanel;
import com.dioswilson.minecraft.Chunk;

import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.*;

public class Main {

    final Random randy = new Random();

    static final int konst = 10387319;

    static final int radius = 23437;//30M

    public static long seed;

    private Set<Chunk> chunksForSpawning = new HashSet<>();


//    private static final int WIDTH = 400;

//    private static final int HEIGHT = 500;

    static JFrame guiFrame = new JFrame();

    static JButton button = new JButton("Search");

    public static boolean running = false;

    public static boolean stop = true;

    public static JTextField seedTextField = new JTextField(20);

    public static JTextArea textArea = new JTextArea(1, 40);

    public static JScrollPane scroll = new JScrollPane(textArea);
    private Semaphore semaphore = new Semaphore(1);
    private List<Chunk> witchChunks = new ArrayList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(675, 500);
        frame.setDefaultCloseOperation(3);
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