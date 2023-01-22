package com.dioswilson.minecraft;//package witch.minecraft;
//
//public class AnvilCunkLoader {
//
//
//
//    /**
//     * Reads the data stored in the passed NBTTagCompound and creates a Chunk with that data in the passed World.
//     * Returns the created Chunk.
//     */
//    private Chunk readChunkFromNBT(World worldIn, NBTTagCompound compound)
//    {
//        int i = compound.getInteger("xPos");
//        int j = compound.getInteger("zPos");
//        Chunk chunk = new Chunk(worldIn, i, j);
//        chunk.setHeightMap(compound.getIntArray("HeightMap"));
//        chunk.setTerrainPopulated(compound.getBoolean("TerrainPopulated"));
//        chunk.setLightPopulated(compound.getBoolean("LightPopulated"));
//        chunk.setInhabitedTime(compound.getLong("InhabitedTime"));
//        NBTTagList nbttaglist = compound.getTagList("Sections", 10);
//        int k = 16;
//        ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[16];
//        boolean flag = worldIn.provider.hasSkyLight();
//
//        for (int l = 0; l < nbttaglist.tagCount(); ++l)
//        {
//            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(l);
//            int i1 = nbttagcompound.getByte("Y");
//            ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(i1 << 4, flag);
//            byte[] abyte = nbttagcompound.getByteArray("Blocks");
//            NibbleArray nibblearray = new NibbleArray(nbttagcompound.getByteArray("Data"));
//            NibbleArray nibblearray1 = nbttagcompound.hasKey("Add", 7) ? new NibbleArray(nbttagcompound.getByteArray("Add")) : null;
//            extendedblockstorage.getData().setDataFromNBT(abyte, nibblearray, nibblearray1);
//            extendedblockstorage.setBlockLight(new NibbleArray(nbttagcompound.getByteArray("BlockLight")));
//
//            if (flag)
//            {
//                extendedblockstorage.setSkyLight(new NibbleArray(nbttagcompound.getByteArray("SkyLight")));
//            }
//
//            extendedblockstorage.recalculateRefCounts();
//            aextendedblockstorage[i1] = extendedblockstorage;
//        }
//
//        chunk.setStorageArrays(aextendedblockstorage);
//
//        // NewLight PHIPRO-CARPET
//        //if(CarpetSettings.newLight){
//        //	carpet.helpers.LightingHooks.readLightData(chunk, compound);
//        //}
//
//        if (compound.hasKey("Biomes", 7))
//        {
//            chunk.setBiomeArray(compound.getByteArray("Biomes"));
//        }
//
//        NBTTagList nbttaglist1 = compound.getTagList("Entities", 10);
//
//        for (int j1 = 0; j1 < nbttaglist1.tagCount(); ++j1)
//        {
//            NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j1);
//            readChunkEntity(nbttagcompound1, worldIn, chunk);
//            chunk.setHasEntities(true);
//        }
//
//        NBTTagList nbttaglist2 = compound.getTagList("TileEntities", 10);
//
//        for (int k1 = 0; k1 < nbttaglist2.tagCount(); ++k1)
//        {
//            NBTTagCompound nbttagcompound2 = nbttaglist2.getCompoundTagAt(k1);
//            TileEntity tileentity = TileEntity.create(worldIn, nbttagcompound2);
//
//            if (tileentity != null)
//            {
//                chunk.addTileEntity(tileentity);
//            }
//        }
//
//        if (compound.hasKey("TileTicks", 9))
//        {
//            NBTTagList nbttaglist3 = compound.getTagList("TileTicks", 10);
//
//            for (int l1 = 0; l1 < nbttaglist3.tagCount(); ++l1)
//            {
//                NBTTagCompound nbttagcompound3 = nbttaglist3.getCompoundTagAt(l1);
//                Block block;
//
//                if (nbttagcompound3.hasKey("i", 8))
//                {
//                    block = Block.getBlockFromName(nbttagcompound3.getString("i"));
//                }
//                else
//                {
//                    block = Block.getBlockById(nbttagcompound3.getInteger("i"));
//                }
//
//                worldIn.scheduleBlockUpdate(new BlockPos(nbttagcompound3.getInteger("x"), nbttagcompound3.getInteger("y"), nbttagcompound3.getInteger("z")), block, nbttagcompound3.getInteger("t"), nbttagcompound3.getInteger("p"));
//            }
//        }
//
//        return chunk;
//    }
//
//}
