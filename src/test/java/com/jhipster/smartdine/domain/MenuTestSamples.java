package com.jhipster.smartdine.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MenuTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Menu getMenuSample1() {
        return new Menu().id(1L).name("name1");
    }

    public static Menu getMenuSample2() {
        return new Menu().id(2L).name("name2");
    }

    public static Menu getMenuRandomSampleGenerator() {
        return new Menu().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
