package com.dioswilson;

import com.dioswilson.Litematica.LitematicStructureBuilder;
import com.dioswilson.gui.ResultsPanel;
import com.dioswilson.minecraft.BlockPos;
import com.dioswilson.minecraft.Chunk;
import com.seedfinding.mcbiome.source.OverworldBiomeSource;

import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Witch {

    private final int[] heightMap = {80, 96, 112, 128, 144, 160, 176, 192, 208, 224, 240, 256, 272};
    //	public int[] heightMap = {176, 256};
    private final int[] mobsPerPack = {1, 2, 3, 4};
    private long seed;
    //    public Chunk[] witchChunks;
    private HashSet<Chunk> witchChunks = new HashSet<>();
    private HashSet<Chunk> neighbourChunks = new HashSet<>();


    //    public List<BlockPos> positions = new ArrayList<>();
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
    private Set<Chunk> eligibleChunksForSpawning = new HashSet<>();

    private int succesfulSpawns;
    private int succesCalls;

    //TODO: Add thing to see which one is better
    public Witch(int areaMansionX, int areaMansionZ, long seed, OverworldBiomeSource biomeSource, Semaphore semaphore, List<Chunk> witchChunks, Set<Chunk> neighbourChunks, Set<Chunk> chunksForSpawning, int maxAdvancers) {

//        System.out.println("Free memory init = "+(Runtime.getRuntime().freeMemory()));
        this(seed, biomeSource, witchChunks, neighbourChunks, chunksForSpawning);
        this.semaphore = semaphore;
        this.maxAdvancers = maxAdvancers;
        this.areaMansionX = areaMansionX;
        this.areaMansionZ = areaMansionZ;

    }

    public Witch(long seed, OverworldBiomeSource biomeSource, List<Chunk> witchChunks, Set<Chunk> neighbourChunks, Set<Chunk> chunksForSpawning/*, int maxAdvancers*/) {
        this.seed = seed;
        this.biomeSource = biomeSource;
//        this.maxAdvancers = maxAdvancers;
        this.eligibleChunksForSpawning.addAll(chunksForSpawning);
        this.witchChunks.addAll(witchChunks);
        this.neighbourChunks.addAll(neighbourChunks);

        finalHeightMap = new int[this.witchChunks.size()];
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
        String quality="S:"+this.succesfulSpawns+"|C:"+this.succesCalls;

        try {
            this.semaphore.acquire();
            String formattedResultText = String.format("%-28s %-30s %-30s %-15s %s", from, to, iter, advancers, tp);
//            Main.textArea.append(formattedResultText);
            System.out.println(formattedResultText);
            System.out.println("Date: " + new Date().getTime());
            SwingUtilities.invokeLater(() -> {
                ResultsPanel.model.addRow(new Object[]{from, to, iter, advancers,quality, "Download"});
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            System.out.println("Semaphore released");
            this.semaphore.release();
        }
    }

//    public void print() {
//        System.out.println("Start");
//        StringBuilder stringBuilder = new StringBuilder();
//
//        for (BlockPos pos : positions) {
//            stringBuilder.append("listBlockPos.add(new BlockPos(").append(pos.getX()).append(", ").append(pos.getY()).append(", ").append(pos.getZ()).append("));\n");
////            System.out.println("listBlockPos.add(new BlockPos(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "));");
//        }
//        StringSelection stringSelection = new StringSelection(stringBuilder.toString());
//        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//        clipboard.setContents(stringSelection, null);
//        System.out.println("End");
//        printBlocksForCommand("solidBlocks",blocksToFill);
////        System.out.println("Free memory final = "+(Runtime.getRuntime().freeMemory()));
//
//
//        this.positions.clear();
//    }

    public void initialize() {

        for (int i = 0; i <= this.maxAdvancers; i++) {
            this.advancers = 0;
            Arrays.fill(this.finalHeightMap, 0);
            this.positions.clear();
            this.blocksToFill.clear();
//            calculateRandomChunkPositions(witchChunks.size(), 4 + i);
            calculateRandomChunkPositionsFull(4 + i);
        }
//        System.gc();

    }

    private void calculateRandomChunkPositionsFull(int calls) {
        int validChunks = 0;
        HashSet<Integer> differentCalls = new HashSet<>();
        HashSet<Integer> differentCallsTemp = new HashSet<>();

        differentCalls.add(calls);

        int i = 0;
        //Falta tener en cuenta los chunks que no sirven
        int extraCalls = 0;

        this.succesfulSpawns = 0;
        this.succesCalls = 0;
        for (Chunk chunk : this.eligibleChunksForSpawning) {

            int callsAmount = differentCalls.size();//If ==0 then can optimize skipping, it means previous chunk was useless

            if (callsAmount > 0) {

                if (this.witchChunks.contains(chunk)) {

                    for (int height : heightMap) {


                        int validSpawns = 0;
                        int spawns=0;
                        for (int specificCall : differentCalls) {

                            BlockPos blockpos = getRandomChunkPosition(specificCall + extraCalls, chunk.getX(), chunk.getZ(), height);
                            int moreCalls = 3;
                            int staticY = blockpos.getY();

                            if (staticY >= witchHutMinHeight && staticY <= witchHutMaxHeight) {

                                int staticX = blockpos.getX();
                                int staticZ = blockpos.getZ();

                                spawns = simulatePackSpawns(staticX, staticY, staticZ, chunk, height, specificCall + moreCalls + extraCalls, differentCallsTemp);
//                                for (int packSize1 : mobsPerPack) {
//                                    int mobsSpawned = 0;
//                                    HashMap<String, Object> strike1 = performSpawning(packSize1/*, new Random(seed)*/, specificCall + moreCalls + extraCalls, staticX, staticY, staticZ, chunk, mobsSpawned, height);
//                                    mobsSpawned = (int) strike1.get("mobs");
//                                    if (mobsSpawned < 4) {
//                                        for (int packSize2 : mobsPerPack) {
//                                            HashMap<String, Object> strike2 = performSpawning(packSize2/*, new Random(seed)*/, (int) strike1.get("calls"), staticX, staticY, staticZ, chunk, (int) strike1.get("mobs"), height);
//                                            mobsSpawned = (int) strike2.get("mobs");
//                                            if (mobsSpawned < 4) {
//                                                for (int packSize3 : mobsPerPack) {
//                                                    HashMap<String, Object> strike3 = performSpawning(packSize3/*, new Random(seed)*/, (int) strike2.get("calls"), staticX, staticY, staticZ, chunk, (int) strike2.get("mobs"), height);
//                                                    mobsSpawned = (int) strike3.get("mobs");
//                                                    differentCallsTemp.add((int) strike3.get("calls"));
//                                                    if (mobsSpawned >= 3) {//Perfect is 4
//                                                        perfect++;
//                                                    }
//                                                }
//                                            }
//                                            else {
//                                                differentCallsTemp.add((int) strike2.get("calls"));
//                                                perfect += 4;
//                                            }
//                                        }
//                                    }
//                                    else {
//                                        differentCallsTemp.add((int) strike1.get("calls"));
//                                        perfect += 16;
//                                    }
//
//                                }
                                if (spawns >= (54 / (i * 2 + 1))) {//max 64
                                    validSpawns++;
//                            System.out.println("valid");
                                }

                            }
                        }

                        if (validSpawns >= callsAmount * 0.75 / (2 * i + 1)) {
//                        System.out.println("chunks++");
                            this.finalHeightMap[i] = height;
//                    System.out.println("Height: "+height+" i: "+i);
                            validChunks++;
                            succesfulSpawns += spawns;
                            succesCalls += callsAmount;
                            break;//TODO: It only uses the first height it finds for a chunk
                        }
                        differentCallsTemp.clear();

                    }
                    differentCalls.clear();
                    differentCalls.addAll(differentCallsTemp);
                    differentCallsTemp.clear();
                    extraCalls = 0;
                    i++;
                    if (i > this.witchChunks.size()) {
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

        if (validChunks >= this.witchChunks.size()) {
            this.advancers = calls - 4;
            print();
        }


    }

    public int simulatePackSpawns(int staticX, int staticY, int staticZ, Chunk chunk, int height, int prevCalls, HashSet<Integer> differentCallsTemp) {
        int spawns = 0;
        for (int packSize1 : mobsPerPack) {
            int mobsSpawned = 0;
            HashMap<String, Object> strike1 = performSpawning(packSize1/*, new Random(seed)*/, prevCalls, staticX, staticY, staticZ, chunk, mobsSpawned, height);
            mobsSpawned = (int) strike1.get("mobs");
            if (mobsSpawned < 4) {
                for (int packSize2 : mobsPerPack) {
                    HashMap<String, Object> strike2 = performSpawning(packSize2/*, new Random(seed)*/, (int) strike1.get("calls"), staticX, staticY, staticZ, chunk, (int) strike1.get("mobs"), height);
                    mobsSpawned = (int) strike2.get("mobs");
                    if (mobsSpawned < 4) {
                        for (int packSize3 : mobsPerPack) {
                            HashMap<String, Object> strike3 = performSpawning(packSize3/*, new Random(seed)*/, (int) strike2.get("calls"), staticX, staticY, staticZ, chunk, (int) strike2.get("mobs"), height);
                            mobsSpawned = (int) strike3.get("mobs");
                            differentCallsTemp.add((int) strike3.get("calls"));
                            if (mobsSpawned >= 3) {//Perfect is 4
                                spawns++;
                            }
                        }
                    }
                    else {
                        differentCallsTemp.add((int) strike2.get("calls"));
                        spawns += 4;
                    }
                }
            }
            else {
                differentCallsTemp.add((int) strike1.get("calls"));
                spawns += 16;
            }

        }
        return spawns;
    }

    public HashMap<String, Object> performSpawning(int value/*, Random rand*/, int calls, int staticX, int staticY, int staticZ, Chunk chunk, int spawnedMobs, int height) {
        Random rand = new Random(seed);

        int x = staticX;
        int z = staticZ;

        boolean list = false;
        boolean canSpawn = true;

        for (int c = 0; c < calls; c++) {
            rand.nextInt();
        }
        int moreCalls = 0;
        HashMap<String, Object> data = new HashMap<>();

        for (int i4 = 0; i4 < value; ++i4) {

            x += rand.nextInt(6) - rand.nextInt(6);
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
                positions.add(new BlockPos(x, staticY, z));//It will add regardless of the height, therefore it might block some

            }

//            if (canSpawn) {
//                rand.nextInt();//nextFloat() on code
//                moreCalls++;
//                spawnedMobs++;
//                positions.add(new BlockPos(x, staticY, z));
//
//            }

            if (spawnedMobs == 4) {
                break;
            }

        }
        data.put("height", height);
        data.put("calls", calls + moreCalls);
        data.put("mobs", spawnedMobs);
//        data.put("rand", rand);

        return data;


    }


    public void getBlocksPositions(int calls, int playerX, int playerZ) {

        int validChunks = 0;
        HashSet<Integer> differentCalls = new HashSet<>();
        HashSet<Integer> differentCallsTemp = new HashSet<>();

        differentCalls.add(calls);

        int i = 0;
        for (Chunk chunk : this.eligibleChunksForSpawning) {
            int callsAmount = differentCalls.size();//If ==0 then can optimize skipping, it means previous chunk was useless

            if (this.witchChunks.contains(chunk)) {

                for (int height : heightMap) {

                    int validSpawns = 0;

                    for (int specificCall : differentCalls) {

                        BlockPos blockpos = getRandomChunkPosition(specificCall /*+ extraCalls*/, chunk.getX(), chunk.getZ(), height);
                        int moreCalls = 3;
                        int staticY = blockpos.getY();

                        if (staticY >= witchHutMinHeight && staticY <= witchHutMaxHeight) {

                            int staticX = blockpos.getX();
                            int staticZ = blockpos.getZ();

                            int spawns = simulatePackSpawns(staticX, staticY, staticZ, chunk, height, specificCall + moreCalls /*+ extraCalls*/, differentCallsTemp);
//
                            if (spawns >= (54 / (i * 2 + 1))) {//max 64
                                validSpawns++;
                            }
                        }
                    }

                    if (validSpawns >= callsAmount * 0.75 / (2*i + 1)) {
//                        System.out.println("chunks++");
                        this.finalHeightMap[i] = height;
//                    System.out.println("Height: "+height+" i: "+i);
                        validChunks++;
                        break;//TODO: It only uses the first height it finds for a chunk
                    }
                    differentCallsTemp.clear();

                }
                differentCalls.clear();
                differentCalls.addAll(differentCallsTemp);
                differentCallsTemp.clear();
//                extraCalls = 0;
                i++;
                if (i > this.witchChunks.size()) {
                    break;
                }
            }
            else {
                int height = 0;
                for (int specificCall : differentCalls) {
                    if (!this.neighbourChunks.contains(chunk)) {
                        height = 16;
                    }
                    else {
                        height = 80;
                    }
                    boolean gettingHeight = true;
                    while (gettingHeight) {
                        BlockPos blockpos = getRandomChunkPosition(specificCall, chunk.getX(), chunk.getZ(), height);
                        int yValue = blockpos.getY();
                        if (((yValue + 1) % 16) != 0) {
                            gettingHeight = false;
                        }
                        else {
                            height += 16;
                        }
                    }
                }

                for (int specificCall : differentCalls) {
                    BlockPos blockpos = getRandomChunkPosition(specificCall, chunk.getX(), chunk.getZ(), height);
                    this.blocksToFill.add(blockpos);
                    differentCallsTemp.add(specificCall + 3);
                }
                differentCalls.clear();
                differentCalls.addAll(differentCallsTemp);
                differentCallsTemp.clear();
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


    private void printBlocksForCommand(String listName, Set<BlockPos> blocksToFill) {

//        System.out.println("Start");
//        StringBuilder stringBuilder = new StringBuilder();

        for (BlockPos pos : blocksToFill) {

//            stringBuilder.append("listBlockPos.add(new BlockPos(").append(pos.getX()).append(", ").append(pos.getY()).append(", ").append(pos.getZ()).append("));\n");
            System.out.println(listName + ".add(new BlockPos(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "));");
        }
    }


    private void createLitematic(int playerX, int playerZ) {
        LitematicStructureBuilder structure = new LitematicStructureBuilder();
        for (BlockPos blockPos : this.positions) {
            structure.setblock(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ(), "hopper");
        }
        for (BlockPos blockPos : this.blocksToFill) {
            structure.setblock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), "bedrock");
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

        structure.setblock(playerX, 63, playerZ, "glass");

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

}





