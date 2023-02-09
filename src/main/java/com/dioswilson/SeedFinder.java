package com.dioswilson;

import com.dioswilson.minecraft.Chunk;
import com.seedfinding.mcbiome.source.OverworldBiomeSource;
import com.seedfinding.mccore.version.MCVersion;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class SeedFinder extends Thread {

    private final int KONST = 10387319;

    private final int RADIUS = 23437;//30M

    private long seed;
    private int playerX;
    private int playerZ;

    private int maxAdvancers;
    private Set<Chunk> chunksForSpawning = new HashSet<>();

    private List<Chunk> witchChunks;
    private HashSet<Chunk> neighbourChunks = new HashSet<>();

    private Integer witchX;
    private Integer witchZ;
    private Semaphore semaphore = new Semaphore(1);
    public static boolean stop = true;
    public static boolean running;
    private AtomicReference<Double> performace;


    public SeedFinder(int playerX, int playerZ, int maxAdvancers, List<Chunk> witchChunks, long seed) {

        this.seed = seed;
        this.playerX = playerX;
        this.playerZ = playerZ;
        this.witchChunks = witchChunks;

        for (Chunk chunk : witchChunks) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0)) {
                        this.neighbourChunks.add(new Chunk(chunk.getX() + i, chunk.getZ() + j));
                    }
                }
            }
        }

        this.maxAdvancers = maxAdvancers;

    }

    public SeedFinder(int playerX, int playerZ, int maxAdvancers, List<Chunk> witchChunks, long seed, AtomicReference<Double> performace) {

        this(playerX, playerZ, maxAdvancers, witchChunks, seed);

        this.performace = performace;
    }

    public SeedFinder(int playerX, int playerZ, int maxAdvancers, List<Chunk> witchChunks, long seed, int witchX, int witchZ) {
        this(playerX, playerZ, maxAdvancers, witchChunks, seed);
        this.witchX = witchX;
        this.witchZ = witchZ;
    }


    public void run() {
        try {
            getChunksForSpawning();
            if (this.witchX == null) {
                seedLoop();
            }
            else {
                setRandomSeedWithAdvancer(this.witchX / 1280, this.witchZ / 1280, this.maxAdvancers);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        this.running = false;
    }


    public void getChunksForSpawning() {

        int cunkPlayerPosX = playerX >> 4;
        int cunkPlayerPosZ = playerZ >> 4;

        for (int i = -7; i < 8; i++) {
            for (int j = -7; j < 8; j++) {
                this.chunksForSpawning.add(new Chunk(cunkPlayerPosX + i, cunkPlayerPosZ + j));
            }
        }
    }


    public void seedLoop() throws InterruptedException, ExecutionException {
        int x = 0;//370
        int z = 0;//-369
        int di = 1;//0
        int dj = 0;//1
        int segmentLength = 1;//739
        int segmentPassed = 0;//0

//        this.witchChunks.add(new Chunk(22, 23));
//        this.witchChunks.add(new Chunk(23, 33));
//        this.witchChunks.add(new Chunk(32, 22));
//        this.witchChunks.add(new Chunk(32, 33));


        final int threadCount = (int) Math.ceil(Runtime.getRuntime().availableProcessors()*performace.get());
//        final int threadCount = 7;

        final ExecutorService threadExecutorService = Executors.newFixedThreadPool(threadCount);
//        final ExecutorService threadExecutorService = Executors.newCachedThreadPool();
//        System.out.println("Date initx: " + new Date().getTime());
//        System.out.println((int)Math.ceil(Runtime.getRuntime().availableProcessors()*performace.get()));
//        System.out.println(Runtime.getRuntime().totalMemory());
        Future[] futures = new Future[threadCount];
        int i = 0;
        while (x < RADIUS && !stop) {

            int finalX = x;
            int finalZ = z;
            if (Runtime.getRuntime().freeMemory() >= 62914560) {
                futures[i++] = threadExecutorService.submit(() -> {
                    setRandomSeed(finalX, finalZ);
//                    System.out.println("thread:"+Thread.currentThread()+ "Ended");
                    return null;
                });
//                int finalX = x;
//                int finalZ = z;
//                threadExecutorService.submit(() -> {
//                    setRandomSeed(finalX, finalZ);
//                    System.out.println("thread:"+Thread.currentThread()+ "Ended");
//                    return null;
//                });
//                setRandomSeed(x, z);
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
                if (i >= threadCount) {
                    for (int j = 0; j < threadCount; j++) {
                        futures[j].get();
                    }
                    i = 0;
                }
            }
            else {
                Thread.sleep(20000);
                System.gc();
//                System.gc();
                System.out.println("ManualGC");
            }

        }
        threadExecutorService.shutdown();

        System.out.println("Done at X: " + x + " , Z: " + z);

    }


//    public void findAt(int x, int z) {
//        setRandomSeed(x, z, this.KONST, this.seed);
//        stop = true;
//        InputValuesPanel.searchButton.setText("Search");
//    }

    public void setRandomSeed(int x, int z) {
        if (!stop) {
            long i = x * 341873128712L + z * 132897987541L + this.seed + this.KONST;
            OverworldBiomeSource biomeSource = new OverworldBiomeSource(MCVersion.v1_12_2, this.seed);
            Witch witch = new Witch(x, z, i, biomeSource, this.semaphore, this.witchChunks, this.neighbourChunks, this.chunksForSpawning, this.maxAdvancers);
            witch.initialize();
        }

    }

    public void setRandomSeedWithAdvancer(int x, int z, int advancers) {
        long i = x * 341873128712L + z * 132897987541L + this.seed + this.KONST;
        OverworldBiomeSource biomeSource = new OverworldBiomeSource(MCVersion.v1_12_2, this.seed);
        Witch witch = new Witch(i, biomeSource, this.witchChunks, this.neighbourChunks, this.chunksForSpawning);
        witch.getBlocksPositions(4 + advancers, playerX, playerZ);
    }

}