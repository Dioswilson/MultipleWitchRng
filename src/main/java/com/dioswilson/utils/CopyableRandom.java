package com.dioswilson.utils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CopyableRandom extends Random {
    private final AtomicLong seed = new AtomicLong(0L);

    private final static long multiplier = 0x5DEECE66DL;
    private final static long addend = 0xBL;
    private final static long mask = (1L << 48) - 1;

    public CopyableRandom() {
        this(++seedUniquifier + System.nanoTime());
    }

    private static volatile long seedUniquifier = 8682522807148012L;

    public CopyableRandom(long seed) {
        this.seed.set((seed ^ multiplier) & mask);
    }

    public CopyableRandom(CopyableRandom random) {
        this.seed.set(random.getCurrentSeed());
    }

    public long getCurrentSeed() {
        return seed.get();
    }

    public void setCurrentSeed(long seed) {
        this.seed.set(seed);
    }

    protected int next(int bits) {
        long oldseed, nextseed;
        AtomicLong seed_ = this.seed;
        do {
            oldseed = seed_.get();
            nextseed = (oldseed * multiplier + addend) & mask;
        } while (!seed_.compareAndSet(oldseed, nextseed));
        return (int) (nextseed >>> (48 - bits));
    }

    /* necessary to prevent changes to seed that are made in constructor */

    public CopyableRandom copy() {
        return new CopyableRandom((seed.get() ^ multiplier) & mask);
    }

}
