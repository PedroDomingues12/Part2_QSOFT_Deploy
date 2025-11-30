package com.jhipster.smartdine.domain;

import static com.jhipster.smartdine.domain.DishTestSamples.*;
import static com.jhipster.smartdine.domain.MenuDishesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.smartdine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuDishesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuDishes.class);
        MenuDishes menuDishes1 = getMenuDishesSample1();
        MenuDishes menuDishes2 = new MenuDishes();
        assertThat(menuDishes1).isNotEqualTo(menuDishes2);

        menuDishes2.setId(menuDishes1.getId());
        assertThat(menuDishes1).isEqualTo(menuDishes2);

        menuDishes2 = getMenuDishesSample2();
        assertThat(menuDishes1).isNotEqualTo(menuDishes2);
    }

    @Test
    void dishNamesTest() {
        MenuDishes menuDishes = getMenuDishesRandomSampleGenerator();
        Dish dishBack = getDishRandomSampleGenerator();

        menuDishes.setDishNames(dishBack);
        assertThat(menuDishes.getDishNames()).isEqualTo(dishBack);

        menuDishes.dishNames(null);
        assertThat(menuDishes.getDishNames()).isNull();
    }
}
