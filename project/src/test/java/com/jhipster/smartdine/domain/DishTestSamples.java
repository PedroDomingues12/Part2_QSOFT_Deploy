package com.jhipster.smartdine.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DishTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Dish getDishSample1() {
        return new Dish().id(1L).name("name1");
    }

    public static Dish getDishSample2() {
        return new Dish().id(2L).name("name2");
    }

    public static Dish getDishRandomSampleGenerator() {
        return new Dish().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
