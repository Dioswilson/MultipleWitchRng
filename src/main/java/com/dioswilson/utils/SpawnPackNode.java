package com.dioswilson.utils;

import com.dioswilson.minecraft.BlockPos;
import com.dioswilson.minecraft.Chunk;
import com.seedfinding.mcbiome.source.OverworldBiomeSource;

import java.util.*;

public class SpawnPackNode {
    private int depth;

    private List<SpawnPackNode> children = new ArrayList<>();
    //Don't need parents

    private int packSize;

    // vanilla
    private final int offSetX = 6;
    private final int offSetZ = 8;

    private final OverworldBiomeSource biomeSource;

    public SpawnPackNode(int depth, int packSize, OverworldBiomeSource biomeSource) {//Some way to add children
        //TODO: IMPLEMENT CONSTRUCTOR

        this.depth = depth;
        this.packSize = packSize;
        this.biomeSource = biomeSource;
    }


    public void addChild(SpawnPackNode child) {
        children.add(child);
    }

    public List<SpawnPackNode> getChildren() {
        return children;
    }

    public int getDepth() {
        return depth;
    }

    public HashMap<String, Integer> testSpawns(Random rand, int initialX, int initialZ, int initialY, Chunk chunk, int spawnedMobs, Set<BlockPos> positionsTemp) {
        int x = initialX;
        int z = initialZ;


        boolean list = false;
        boolean canSpawn = true;
        int moreCalls = 0;

        HashMap<String, Integer> data = new HashMap<>();

        for (int i4 = 0; i4 < this.packSize; ++i4) {
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

                    int biomeId = biomeSource.getBiome(x, initialY, z).getId();
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
                positionsTemp.add(new BlockPos(x, initialY, z));
            }

            if (spawnedMobs == 4) {
                break;
            }

        }
        data.put("newCalls", moreCalls);
        data.put("mobs", spawnedMobs);

        return data;
    }

}
