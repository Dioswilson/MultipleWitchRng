package com.dioswilson;

import com.dioswilson.gui.InputValuesPanel;
import com.dioswilson.gui.ResultsPanel;
import com.dioswilson.minecraft.Chunk;
import com.seedfinding.mcbiome.source.OverworldBiomeSource;
import com.seedfinding.mccore.version.MCVersion;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
//        frame.setSize(550, 500);
        frame.setDefaultCloseOperation(3);


        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Input Values", new InputValuesPanel());
        tabbedPane.add("Results", new ResultsPanel());

        tabbedPane.setVisible(true);
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);


//        guiFrame.setDefaultCloseOperation(3);
//        guiFrame.setTitle("Quad Whitch Hut Finder");
//        guiFrame.setSize(750, 500);
//        seedTextField.setText("Enter Seed Here");
//        textArea.setEditable(false);
//        guiFrame.add(seedTextField, "North");
//        guiFrame.add(button, "South");
//        scroll = new JScrollPane(textArea);
//        scroll.setVerticalScrollBarPolicy(22);
//        guiFrame.add(scroll, "Center");
//        button.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent event) {
//                if (!Main.running) {
//                    (new Main()).Main();
//                    Main.stop = false;
//                    Main.running = true;
//                    Main.button.setText("Stop");
//                    try {
//                        Main.textArea.setText("");
//                        String seedField = Main.seedTextField.getText();
//                        Main.seed = Long.valueOf(Long.parseLong(seedField)).longValue();
//                    } catch (Exception e) {
//                        Main.button.setText("Search");
//                        Main.running = false;
//                        Main.textArea.setText("That is not a seed you mongrel.");
//                    }
//                }
//                else {
//                    Main.stop = true;
//                    Main.button.setText("Search");
//                }
//            }
//        });
//        guiFrame.setVisible(true);
    }




    /*public void Main() {
        (new SeedFinder()).start();
    }*/

    public void findAt(int x, int z) {
        setRandomSeed(x, z, konst, seed);
//        setRandomSeed(176640 / 1280, -147200 / 1280, konst, seed); //2 Witch 0.75 call with 1  and 2 in perfect  0 advancers TEST /rng setseed 142585912046496
//        setRandomSeed(-427520 / 1280, 268800 / 1280, konst, seed); //3 Witch 0.75 call with 1  and 2 in perfect  1392 advancers /rng setseed 280202168971435
//        setRandomSeed(-12800/1280, 6400/1280, konst, seed); //4 Witch 0.55 call with 3  and 4 in perfect  1969 advancers /rng setseed 12905031800008
//        setRandomSeed(-1036800/1280, -673280/1280, konst, seed); //3 Witch 0.55 call with 3  and 4 in perfect   /rng setseed 142569256215019
//        setRandomSeed(-12896000/1280, -1647360/1280, konst, seed); //2 Witch 0.55 call and 4 in perfect   /rng setseed 246038337465996
//        setRandomSeed(-401920/1280, 369920/1280, konst, seed); //2 Witch 0.15 call with 3 as multiplier and 5?4? in perfect
//        setRandomSeed(318720/1280, -320000/1280, konst, seed); //1 Witch hut >=3 all the time
//        setRandomSeed(59, -456, konst, seed);
//        setRandomSeed(12, -5, konst, seed);
//		  setRandomSeed(25, 74, konst, seed);
//		  setRandomSeed(-5822, 445, konst, seed);
//        Main.stop = true;
//        Main.button.setText("Search");
    }

    public void setRandomSeed(int x, int z, int konstant, long seed) {
//        long i = x * 341873128712L + z * 132897987541L + seed + konstant;
//        OverworldBiomeSource biomeSource = new OverworldBiomeSource(MCVersion.v1_12_2, seed);
//        Witch witch = new Witch(x, z, i, biomeSource, this.semaphore, this.witchChunks, this.chunksForSpawning);
//        witch.initialize();
    }

    public void printSeed() {
        try {
            Field field = Random.class.getDeclaredField("seed");
            field.setAccessible(true);
            AtomicLong scrambledSeed = (AtomicLong) field.get(this.randy);
            long theSeed = scrambledSeed.get();
            System.out.println(theSeed);
        } catch (Exception exception) {
        }
    }


//    public class SeedFinder extends Thread {
//        public void run() {
//            try {
//                getChunksForSpawning();
//                Main.this.seedLoop();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            Main.running = false;
//        }
//    }

}