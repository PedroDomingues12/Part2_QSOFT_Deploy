package com.jhipster.smartdine.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MenuDishesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MenuDishes getMenuDishesSample1() {
        return new MenuDishes().id(1L).name("name1");
    }

    public static MenuDishes getMenuDishesSample2() {
        return new MenuDishes().id(2L).name("name2");
    }

    public static MenuDishes getMenuDishesRandomSampleGenerator() {
        return new MenuDishes().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
