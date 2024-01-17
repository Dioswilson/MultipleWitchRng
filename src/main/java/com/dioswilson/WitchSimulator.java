package com.dioswilson;

import com.dioswilson.Litematica.LitematicStructureBuilder;
import com.dioswilson.gui.ResultsPanel;
import com.dioswilson.minecraft.BlockPos;
import com.dioswilson.minecraft.Chunk;
import com.dioswilson.utils.CopyableRandom;
import com.dioswilson.utils.SpawnPackNode;
import com.seedfinding.mcbiome.source.OverworldBiomeSource;

import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.Semaphore;

public class WitchSimulator {

    private final int[] heightMap = {80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240, 256, 272};
    private final int[] mobsPerPack = {1, 2, 3, 4};
    private long seed;
    //    public Chunk[] witchChunks;
    private HashSet<Chunk> witchChunks = new HashSet<>();
    private HashSet<Chunk> neighbourChunks = new HashSet<>();


    private Set<BlockPos> positions = new HashSet<>();
    Set<BlockPos> blocksToFill = new HashSet<>();


    private int[] finalHeightMap;

    // vanilla
    private final int offSetX = 6;
    private final int offSetZ = 8;

    private final int witchHutMinHeight = 64;
    private final int witchHutMaxHeight = 70;
    private int areaMansionX;
    private int areaMansionZ;

    private final OverworldBiomeSource biomeSource;
    private Semaphore semaphore;
    private int advancers;
    private int maxAdvancers;
    private Set<Chunk> eligibleChunksForSpawning;

    private double succesfulSpawns;
    SpawnPackNode[] roots;

    public WitchSimulator(int areaMansionX, int areaMansionZ, long seed, OverworldBiomeSource biomeSource, Semaphore semaphore, List<Chunk> witchChunks, Set<Chunk> neighbourChunks, Set<Chunk> chunksForSpawning, int maxAdvancers, int maxPlayers) {

        this(seed, biomeSource, witchChunks, neighbourChunks, chunksForSpawning, maxPlayers);
        this.semaphore = semaphore;
        this.maxAdvancers = maxAdvancers;
        this.areaMansionX = areaMansionX;
        this.areaMansionZ = areaMansionZ;

    }

    public WitchSimulator(long seed, OverworldBiomeSource biomeSource, List<Chunk> witchChunks, Set<Chunk> neighbourChunks, Set<Chunk> chunksForSpawning,/*, int maxAdvancers*/int maxPlayers) {

        int totalChunks = 255 * maxPlayers;

        int bucketSize = 16;

        while (bucketSize * 0.75 < totalChunks) {
            bucketSize *= 2;
        }

        this.eligibleChunksForSpawning = new HashSet<>(bucketSize);

        this.seed = seed;
        this.biomeSource = biomeSource;
        this.eligibleChunksForSpawning.addAll(chunksForSpawning);
        this.witchChunks.addAll(witchChunks);
        this.neighbourChunks.addAll(neighbourChunks);

        finalHeightMap = new int[this.witchChunks.size()];

        //Maybe improve this initialization
        roots = new SpawnPackNode[]{
                new SpawnPackNode(0, 1, this.biomeSource),
                new SpawnPackNode(0, 2, this.biomeSource),
                new SpawnPackNode(0, 3, this.biomeSource),
                new SpawnPackNode(0, 4, this.biomeSource),
        };

        for (int i = 0; i < 4; i++) {
            roots[i].addChild(new SpawnPackNode(1, 1, this.biomeSource));
            roots[i].addChild(new SpawnPackNode(1, 2, this.biomeSource));
            roots[i].addChild(new SpawnPackNode(1, 3, this.biomeSource));
            roots[i].addChild(new SpawnPackNode(1, 4, this.biomeSource));

            for (int j = 0; j < 4; j++) {
                roots[i].getChildren().get(j).addChild(new SpawnPackNode(2, 1, this.biomeSource));
                roots[i].getChildren().get(j).addChild(new SpawnPackNode(2, 2, this.biomeSource));
                roots[i].getChildren().get(j).addChild(new SpawnPackNode(2, 3, this.biomeSource));
                roots[i].getChildren().get(j).addChild(new SpawnPackNode(2, 4, this.biomeSource));
            }
        }

    }

    public void print() {
        int fromX = this.areaMansionX * 1280;
        int fromZ = this.areaMansionZ * 1280;
        int toX = fromX + 1280;
        int toZ = fromZ + 1280;
        String from = "X:" + fromX + ", Z:" + fromZ;
        String to = "X:" + toX + ", Z:" + toZ;
        String iter = Arrays.toString(this.finalHeightMap);
        String advancers = "" + this.advancers;
        String tp = " /tp " + fromX + " 150 " + fromZ + "\n";
        String quality = "S:" + String.format("%.4f", this.succesfulSpawns / 64D);

        try {
            this.semaphore.acquire();
            System.out.printf("%-28s %-30s %-30s %-15s %-15s %s%n", from, to, iter, advancers, quality, tp);
            System.out.println("Date: " + new Date().getTime());
            SwingUtilities.invokeLater(() -> {
                ResultsPanel.model.addRow(new Object[]{from, to, iter, advancers, quality, "Download"});
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            System.out.println("Semaphore released");
            this.semaphore.release();
        }
    }

    public void initialize() {

        if (this.areaMansionX * 1280 == 949760 && this.areaMansionZ * 1280 == 1944320) {
            System.out.println("TEST");
        }
        for (int i = 0; i <= this.maxAdvancers; i++) {
            this.advancers = 0;
            Arrays.fill(this.finalHeightMap, 0);
            this.positions.clear();
            this.blocksToFill.clear();
            calculateRandomChunkPositionsFull(4 + i);
        }

    }

    private void calculateRandomChunkPositionsFull(int calls) {
        int validChunks = 0;
        HashMap<Integer, Integer> differentCalls = new HashMap<>(); //TODO: naming
        HashMap<Integer, Integer> differentCallsTemp = new HashMap<>();

//        differentCalls.add(calls);
        differentCalls.put(calls, 1);
        int i = 0;
        int extraCalls = 0;

        this.succesfulSpawns = 0;

        Set<BlockPos> positionsTemp = new HashSet<>();

        for (Chunk chunk : this.eligibleChunksForSpawning) {

            int callsAmount = differentCalls.size();//If ==0 then can optimize skipping, it means previous chunk was useless

            if (callsAmount > 0) {

                if (this.witchChunks.contains(chunk)) {
                    int completeCallsAmount = differentCalls.values().stream().mapToInt(Integer::intValue).sum();

                    double maxQuality = 0;
                    int finalHeightmap = 0;
                    HashMap<Integer, Integer> defDifferentCalls = new HashMap<>();
                    Set<BlockPos> defPositionsTemp = new HashSet<>();

                    for (int height : heightMap) {

                        double quality = 0;
                        for (int specificCall : differentCalls.keySet()) {
                            int specificCallAmount = differentCalls.get(specificCall);

                            BlockPos blockpos = getRandomChunkPosition(specificCall + extraCalls, chunk.getX(), chunk.getZ(), height);
                            int moreCalls = 3;
                            int staticY = blockpos.getY();

                            if (staticY >= witchHutMinHeight && staticY <= witchHutMaxHeight) {

                                int staticX = blockpos.getX();
                                int staticZ = blockpos.getZ();

                                int spawns = simulatePackSpawns(staticX, staticY, staticZ, chunk, specificCall + moreCalls + extraCalls, differentCallsTemp, positionsTemp, specificCallAmount);

                                quality += (double) (spawns * specificCallAmount) / completeCallsAmount;

                            }
                            else {
                                int currCalls = specificCall + extraCalls + moreCalls;
                                if (differentCallsTemp.containsKey(currCalls)) {
                                    differentCallsTemp.merge(currCalls, specificCallAmount * 64, Integer::sum);
                                }
                                else {
                                    differentCallsTemp.put(currCalls, specificCallAmount * 64);
                                }
//                                this.blocksToFill.add(blockpos);
                            }
                        }

//                        if (validSpawns >= completeCallsAmount * 0.95 / (2 * i + 1)) {
//                        if (this.succesfulSpawns + quality >= -(15 * i * i) + 155 * i + 255 ) {
                        if (quality > maxQuality) {//If it is equal, might be worth checking the different outcomes
                            maxQuality = quality;
                            defDifferentCalls.clear();
                            defPositionsTemp.clear();
                            defDifferentCalls.putAll(differentCallsTemp);
                            defPositionsTemp.addAll(positionsTemp);
                            finalHeightmap = height;
                        }
                        differentCallsTemp.clear();
                        positionsTemp.clear();
                    }

                    if (this.succesfulSpawns + maxQuality >= -(25 * i * i) + 145 * i + 255) {
                        this.finalHeightMap[i] = finalHeightmap;
                        validChunks++;
                        this.succesfulSpawns += maxQuality;
                        this.positions.addAll(defPositionsTemp);
                        positionsTemp.clear();
                        differentCalls.clear();
                        differentCalls.putAll(defDifferentCalls);
                        differentCallsTemp.clear();
                    }
                    extraCalls = 0;
                    i++;
                    if (i == this.witchChunks.size()) {
                        break;
                    }
                }
                else {
                    extraCalls += 3;
                }

            }
            else {
                break;
            }

        }

        if (validChunks == this.witchChunks.size()) {
            this.advancers = calls - 4;
            print();
        }


    }

    public int simulatePackSpawns(int staticX, int staticY, int staticZ, Chunk chunk, int prevCalls, HashMap<Integer, Integer> calls, Set<BlockPos> positionsTemp, int repetition) {
        int spawns = 0;
        int currCalls;

        CopyableRandom rand = new CopyableRandom(this.seed);

        List<Long> randomSeeds = new ArrayList<>();//Maybe use array

        for (int i = 0; i < prevCalls; i++) {
            rand.nextInt();
        }

        for (int packSize1 : mobsPerPack) {
            if (randomSeeds.size() > 0) {
                rand.setCurrentSeed(randomSeeds.get(0));
            }
            else {
                randomSeeds.add(0, rand.getCurrentSeed());
            }
            int mobsSpawned = 0;

            HashMap<String, Integer> strike1 = performSpawning(packSize1, rand, prevCalls, staticX, staticY, staticZ, chunk, mobsSpawned, positionsTemp);
            mobsSpawned = strike1.get("mobs");
            if (mobsSpawned < 4) {
                for (int packSize2 : mobsPerPack) {
                    if (randomSeeds.size() > 1) {
                        rand.setCurrentSeed(randomSeeds.get(1));
                    }
                    else {
                        randomSeeds.add(1, rand.getCurrentSeed());
                    }

                    HashMap<String, Integer> strike2 = performSpawning(packSize2, rand, strike1.get("calls"), staticX, staticY, staticZ, chunk, strike1.get("mobs"), positionsTemp);
                    mobsSpawned = strike2.get("mobs");
                    if (mobsSpawned < 4) {
                        for (int packSize3 : mobsPerPack) {
                            if (randomSeeds.size() > 2) {
                                rand.setCurrentSeed(randomSeeds.get(2));
                            }
                            else {
                                randomSeeds.add(2, rand.getCurrentSeed());
                            }

                            HashMap<String, Integer> strike3 = performSpawning(packSize3, rand, strike2.get("calls"), staticX, staticY, staticZ, chunk, strike2.get("mobs"), positionsTemp);
                            spawns += strike3.get("mobs");
                            currCalls = strike3.get("calls");
                            if (calls.containsKey(currCalls)) {
                                calls.merge(currCalls, repetition, Integer::sum);
                            }
                            else {
                                calls.put(currCalls, repetition);
                            }

                        }
                    }
                    else {
                        currCalls = strike2.get("calls");
                        if (calls.containsKey(currCalls)) {
                            calls.merge(currCalls, 4 * repetition, Integer::sum);
                        }
                        else {
                            calls.put(currCalls, 4 * repetition);
                        }

                        spawns += 16;
                    }

                    if (randomSeeds.size() > 2) {
                        randomSeeds.remove(2);
                    }
                }
            }
            else {
                currCalls = strike1.get("calls");
                if (calls.containsKey(currCalls)) {
                    calls.merge(currCalls, 16 * repetition, Integer::sum);
                }
                else {
                    calls.put(currCalls, 16 * repetition);
                }
                spawns += 64;
            }
            if (randomSeeds.size() > 1) {
                randomSeeds.remove(1);
            }
        }
        return spawns;
    }

    public HashMap<String, Integer> performSpawning(int packSize, Random rand, int calls, int staticX, int staticY, int staticZ, Chunk chunk, int spawnedMobs, Set<BlockPos> positionsTemp) {

        int x = staticX;
        int z = staticZ;

        boolean list = false;
        boolean canSpawn = true;
        int moreCalls = 0;
        HashMap<String, Integer> data = new HashMap<>();

        for (int i4 = 0; i4 < packSize; ++i4) {

            x += rand.nextInt(6) - rand.nextInt(6);//6 is 2 calls
            rand.nextInt(1);//These two are from Y, (0-0)
            rand.nextInt(1);
            z += rand.nextInt(6) - rand.nextInt(6);
            moreCalls += 6;

            //<<4==*16
            int getX = chunk.getX() << 4;
            int getZ = chunk.getZ() << 4;

            if (!list) {
                list = true;
                if ((x > (getX + offSetX)) || (z > (getZ + offSetZ)) || (x < (getX)) || (z < (getZ))) {//Can ignore height

                    int biomeId = biomeSource.getBiome(x, staticY, z).getId();
                    int weight;
                    if (biomeId == 2 || biomeId == 17 || biomeId == 130) {//2,17,130=desert;21,22,23=jungla;6,134= swamp, ignoring snow,end,hell,void and mooshroom island
                        weight = rand.nextInt(515);
                        if (!(weight - 415 >= -5 && weight - 415 < 0)) {//removes zombies and zombie villagers and places them at the end
                            canSpawn = false;
                        }
                    }
                    else if (biomeId > 20 && biomeId < 24) {//Adds ocelot at the end with weight 2
                        weight = rand.nextInt(517);
                        if (!(weight - 515 >= -5 && weight - 515 < 0)) {
                            canSpawn = false;
                        }
                    }
                    else if (biomeId == 6 || biomeId == 134) {//Adds slime at the end
                        weight = rand.nextInt(516);
                        if (!(weight - 515 >= -5 && weight - 515 < 0)) {
                            canSpawn = false;
                        }
                    }
                    else {
                        weight = rand.nextInt(515);
                        if (!(weight - 515 >= -5)) {//Always <0
                            canSpawn = false;
                        }
                    }

                }
                else {
                    rand.nextInt();
                }
                moreCalls += 1;

            }

            if ((x <= ((getX) + offSetX)) && (z <= ((getZ) + offSetZ)) && (x >= (getX)) && (z >= (getZ)) && canSpawn) {//Can ignore height
                rand.nextInt();//nextFloat() on code
                moreCalls++;
                spawnedMobs++;
                positionsTemp.add(new BlockPos(x, staticY, z));
            }

            if (spawnedMobs == 4) {
                break;
            }

        }
        data.put("calls", calls + moreCalls);
        data.put("mobs", spawnedMobs);
//        data.put("rand", rand);

        return data;

    }

    public void getBlocksPositions(int calls, int playerX, int playerZ) {
        int validChunks = 0;
        HashMap<Integer, Integer> differentCalls = new HashMap<>();
        HashMap<Integer, Integer> differentCallsTemp = new HashMap<>();

        differentCalls.put(calls, 1);

        Set<BlockPos> positionsTemp = new HashSet<>();
        Set<BlockPos> fillBlocksTemp = new HashSet<>();

        int i = 0;
        for (Chunk chunk : this.eligibleChunksForSpawning) {

            int callsAmount = differentCalls.size();//If ==0 then can optimize skipping, it means previous chunk was useless

            if (callsAmount > 0) {

                if (this.witchChunks.contains(chunk)) {
                    int completeCallsAmount = differentCalls.values().stream().mapToInt(Integer::intValue).sum();

                    double maxQuality = 0;
                    int finalHeightmap = 0;
                    HashMap<Integer, Integer> defDifferentCalls = new HashMap<>();
                    Set<BlockPos> defPositionsTemp = new HashSet<>();
                    Set<BlockPos> fillBlocksDef = new HashSet<>();

                    for (int height : heightMap) {
                        double quality = 0;

                        for (int specificCall : differentCalls.keySet()) {
                            int specificCallAmount = differentCalls.get(specificCall);

                            BlockPos blockpos = getRandomChunkPosition(specificCall /*+ extraCalls*/, chunk.getX(), chunk.getZ(), height);
                            int moreCalls = 3;
                            int staticY = blockpos.getY();

                            if (staticY >= witchHutMinHeight && staticY <= witchHutMaxHeight) {

                                int staticX = blockpos.getX();
                                int staticZ = blockpos.getZ();

                                int spawns = simulatePackSpawns(staticX, staticY, staticZ, chunk, specificCall + moreCalls /*+ extraCalls*/, differentCallsTemp, positionsTemp, specificCallAmount);

                                quality += (double) (spawns * specificCallAmount) / completeCallsAmount;

                            }
                            else {
                                int currCalls = specificCall + moreCalls;
                                if (differentCallsTemp.containsKey(currCalls)) {
                                    differentCallsTemp.merge(currCalls, specificCallAmount * 64, Integer::sum);
                                }
                                else {
                                    differentCallsTemp.put(currCalls, specificCallAmount * 64);
                                }

                                fillBlocksTemp.add(blockpos);
                            }
                        }

                        if (quality > maxQuality) {//If it is equal, might be worth checking the different outcomes
                            maxQuality = quality;
                            defDifferentCalls.clear();
                            defPositionsTemp.clear();
                            defDifferentCalls.putAll(differentCallsTemp);
                            defPositionsTemp.addAll(positionsTemp);
                            fillBlocksDef.clear();
                            fillBlocksDef.addAll(fillBlocksTemp);
                            finalHeightmap = height;
                        }
                        differentCallsTemp.clear();
                        positionsTemp.clear();
                        fillBlocksTemp.clear();
                    }
                    if (this.succesfulSpawns + maxQuality >= -(25 * i * i) + 145 * i + 255) {
                        this.finalHeightMap[i] = finalHeightmap;
                        validChunks++;
                        this.succesfulSpawns += maxQuality;
                        this.positions.addAll(defPositionsTemp);
                        positionsTemp.clear();
                        differentCalls.clear();
                        differentCalls.putAll(defDifferentCalls);
                        differentCallsTemp.clear();
                        this.blocksToFill.addAll(fillBlocksDef);
                        fillBlocksDef.clear();
                    }
                    i++;
                    if (i == this.witchChunks.size()) {
                        break;
                    }
                }
                else {
                    int height;
                    if (!this.neighbourChunks.contains(chunk)) {
                        height = 16;
                    }
                    else {
                        height = 80;
                    }
                    boolean gettingHeight = true;
                    while (gettingHeight) {
                        boolean repeat = false;
                        for (int specificCall : differentCalls.keySet()) {

                            BlockPos blockpos = getRandomChunkPosition(specificCall, chunk.getX(), chunk.getZ(), height);
                            int yValue = blockpos.getY();
                            if ((yValue == (height - 1))) {
                                height += 16;
                                repeat = true;
                                break;
                            }

                        }
                        if (!repeat) {
                            gettingHeight = false;
                        }
                    }

                    for (int specificCall : differentCalls.keySet()) {
                        BlockPos blockpos = getRandomChunkPosition(specificCall, chunk.getX(), chunk.getZ(), height);
                        this.blocksToFill.add(blockpos);
                        if (blockpos.getY() < (height - 17)) {
                            BlockPos blockpos1 = new BlockPos(blockpos.getX(), height - 17, blockpos.getZ());
                            this.blocksToFill.add(blockpos1);
                        }
                        differentCallsTemp.put(specificCall + 3, differentCalls.get(specificCall));
                    }
                    differentCalls.clear();
                    differentCalls.putAll(differentCallsTemp);
                    differentCallsTemp.clear();
                }
            }
            else {
                break;
            }
        }
        if (validChunks >= this.witchChunks.size()) {

            createLitematic(playerX, playerZ);
        }


    }


    public BlockPos getRandomChunkPosition(int calls, int x, int z, int height) {
        Random rand = new Random(seed);
        for (int c = 0; c < calls; c++) {
            rand.nextInt();
        }
        int otherX = rand.nextInt(16);
        int otherZ = rand.nextInt(16);
        int i = (x << 4) + otherX;
        int j = (z << 4) + otherZ;
        int k = roundUp(height, 16);
        int l = rand.nextInt(k);

        return new BlockPos(i, l, j);
    }


    public int roundUp(int number, int interval) {
        if (interval == 0) {
            return 0;
        }
        else if (number == 0) {
            return interval;
        }
        else {
            if (number < 0) {
                interval *= -1;
            }
            int i = number % interval;
            return i == 0 ? number : number + interval - i;
        }
    }


    private void createLitematic(int playerX, int playerZ) {
        LitematicStructureBuilder structure = new LitematicStructureBuilder();
        structure.setblock(playerX, 63, playerZ, "glass");
        for (BlockPos blockPos : this.positions) {
            structure.setblock(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ(), "hopper");
        }

        int i = 0;
        for (Chunk chunk : this.eligibleChunksForSpawning) {
            if (this.witchChunks.contains(chunk)) {
                int chunkX = chunk.getX() << 4;
                int chunkZ = chunk.getZ() << 4;
                int height = this.finalHeightMap[i];
                structure.fill(chunkX, 78, chunkZ, chunkX + 15, 78, chunkZ + 15, "stone_slab");
                if (height > 80) {
                    structure.fill(chunkX, height - 17, chunkZ, chunkX + 15, height - 17, chunkZ + 15, "stone_slab");
                }
                i++;
            }
            else if (this.neighbourChunks.contains(chunk)) {
                int chunkX = chunk.getX() << 4;
                int chunkZ = chunk.getZ() << 4;
                structure.fill(chunkX, 78, chunkZ, chunkX + 15, 78, chunkZ + 15, "stone_slab");
            }
        }

        for (BlockPos blockPos : this.blocksToFill) {
            structure.setblock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), "bedrock");
        }


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("witchHutFinder.litematic"));
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            structure.save(fileChooser.getSelectedFile());
        }
    }


   /* public int simulatePackSpawnsTreeApproach(int staticX, int staticY, int staticZ, Chunk chunk, int prevCalls, HashMap<Integer, Integer> calls, Set<BlockPos> positionsTemp, int repetition) {
        int spawns = 0;

        CopyableRandom rand = new CopyableRandom(seed);

        for (int i = 0; i < prevCalls; i++) {
            rand.nextInt();
        }
        try {
            CopyableRandom currRand;
            List<Long> randomSeeds = new ArrayList<>();//Maybe use array
            List<Integer> mobsSpawnedList = new ArrayList<>();//Maybe use array
            List<Integer> callsList = new ArrayList<>();//Maybe use array

            int depth = -1;

            for (SpawnPackNode root : roots) {
                Stack<SpawnPackNode> queue = new Stack<>();
                queue.add(root);

                int mobsSpawned = 0;
                int currCalls = prevCalls;

                HashMap<String, Integer> data = null;
                currRand = rand.copy();

                while (!queue.empty()) {
                    SpawnPackNode node = queue.pop();
                    for (SpawnPackNode child : node.getChildren()) {
                        queue.push(child);
                    }

//                    for (int i = node.getChildren().size() - 1; i >= 0; i--) {
//                        queue.push(node.getChildren().get(i));
//                    }

                    if (depth > node.getDepth()) {
                        for (int i = depth; i > node.getDepth(); i--) {
                            randomSeeds.remove(i);
                            mobsSpawnedList.remove(i);
                            callsList.remove(i);
                        }
                    }
                    depth = node.getDepth();
                    if (randomSeeds.size() >= depth + 1) {
                        currRand.setCurrentSeed(randomSeeds.get(depth));
                        mobsSpawned = mobsSpawnedList.get(depth);
                        currCalls = callsList.get(depth);
                    }
                    else {
                        randomSeeds.add(depth, currRand.getCurrentSeed());
                        mobsSpawnedList.add(depth, mobsSpawned);
                        callsList.add(depth, currCalls);
                    }

                    if (mobsSpawned < 4) {
                        data = node.testSpawns(currRand, staticX, staticZ, staticY, chunk, mobsSpawned, positionsTemp);
                        mobsSpawned = data.get("mobs");
                        currCalls += data.get("newCalls");
                    }
                    else {
                        queue.clear();
                    }

                    if (mobsSpawned >= 4 || depth == 2) {
                        if (calls.containsKey(currCalls)) {
                            calls.merge(currCalls, (int) Math.pow(4, 2 - depth) * repetition, Integer::sum);
                        }
                        else {
                            calls.put(currCalls, (int) Math.pow(4, 2 - depth) * repetition); //Maybe optimize by using own pow
                        }

                        if (depth != 2) {
                            spawns += (int) Math.pow(4, 3 - depth);
                            for (int i = 0; i < node.getChildren().size(); i++) {
                                queue.pop();
                            }
                        }
                        else {
                            spawns += data.get("mobs");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return spawns;
    }*/



}





