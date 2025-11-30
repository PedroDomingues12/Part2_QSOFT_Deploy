package com.jhipster.smartdine.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PurchaseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Purchase getPurchaseSample1() {
        return new Purchase().id(1L);
    }

    public static Purchase getPurchaseSample2() {
        return new Purchase().id(2L);
    }

    public static Purchase getPurchaseRandomSampleGenerator() {
        return new Purchase().id(longCount.incrementAndGet());
    }
}
