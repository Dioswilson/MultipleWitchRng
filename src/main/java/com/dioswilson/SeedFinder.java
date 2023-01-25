package com.dioswilson;

import com.dioswilson.gui.InputValuesPanel;
import com.dioswilson.minecraft.Chunk;
import com.seedfinding.mcbiome.source.OverworldBiomeSource;
import com.seedfinding.mccore.version.MCVersion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SeedFinder extends Thread {

    static final int konst = 10387319;

    static final int radius = 23437;//30M

    public long seed;
    private int playerX;
    private int playerZ;

    private int maxAdvancers;
    private Set<Chunk> chunksForSpawning = new HashSet<>();

    private List<Chunk> witchChunks = new ArrayList<>();
    private Semaphore semaphore = new Semaphore(1);
    public static boolean stop = true;
    public static boolean running;


    public SeedFinder(int playerX, int playerZ, int maxAdvancers, List<Chunk> witchChunks, long seed) {

        this.playerX = playerX;
        this.playerZ = playerZ;
        this.maxAdvancers = maxAdvancers;
        this.witchChunks = witchChunks;
        this.seed = seed;
    }

    public void run() {
        try {
            getChunksForSpawning();
            seedLoop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.running = false;
    }


    private void getChunksForSpawning() {

        int cunkPlayerPosX = playerX >> 4;
        int cunkPlayerPosZ = playerZ >> 4;

        for (int i = -7; i < 8; i++) {
            for (int j = -7; j < 8; j++) {
                this.chunksForSpawning.add(new Chunk(cunkPlayerPosX + i, cunkPlayerPosZ + j));
            }
        }
    }


    public void seedLoop() throws InterruptedException {
        int x = 0;//370
        int z = 0;//-369
        int di = 1;//0
        int dj = 0;//1
        int segmentLength = 1;//739
        int segmentPassed = 0;//0
//
//        this.witchChunks.add(new Chunk(22, 23));
//        this.witchChunks.add(new Chunk(23, 33));
//        this.witchChunks.add(new Chunk(32, 22));
//        this.witchChunks.add(new Chunk(32, 33));

//        final ExecutorService threadExecutorService = Executors.newFixedThreadPool(8);
        final ExecutorService threadExecutorService = Executors.newCachedThreadPool();

        while (x < radius && !stop) {
            if (Runtime.getRuntime().freeMemory() >= 62914560) {
                int finalX = x;
                int finalZ = z;
//                threadExecutorService.submit(() -> {
//                    findAt(finalX, finalZ);
//                    return null;
//                });
                findAt(x, z);
//                System.out.println("X: "+x+" Z: "+z);
//                Thread.sleep(10);
                x += di;
                z += dj;
                segmentPassed++;
                if (segmentPassed == segmentLength) {
                    segmentPassed = 0;
                    int buffer = di;
                    di = -dj;
                    dj = buffer;
                    if (dj == 0) {
                        segmentLength++;
                    }
                }
            }
            else {
                Thread.sleep(4000);
                System.out.println("ThreadSleep");
            }

        }
        threadExecutorService.shutdown();

        System.out.println("Done at X: " + x + " , Z: " + z);

    }


    public void findAt(int x, int z) {
//        setRandomSeed(x, z, konst, seed);
        setRandomSeed(176640 / 1280, -147200 / 1280, konst, seed); //2 Witch 0.75 call with 1  and 2 in perfect  0 advancers TEST /rng setseed 142585912046496
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
        stop = true;
        InputValuesPanel.searchButton.setText("Search");
    }


    public void setRandomSeed(int x, int z, int konstant, long seed) {
        long i = x * 341873128712L + z * 132897987541L + seed + konstant;
        OverworldBiomeSource biomeSource = new OverworldBiomeSource(MCVersion.v1_12_2, seed);
        Witch witch = new Witch(x, z, i, biomeSource, this.semaphore, this.witchChunks, this.chunksForSpawning,this.maxAdvancers);
        witch.initialize();
    }
}