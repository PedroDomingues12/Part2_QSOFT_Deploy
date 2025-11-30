package com.jhipster.smartdine.domain;

import static com.jhipster.smartdine.domain.DishTestSamples.*;
import static com.jhipster.smartdine.domain.MenuTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.smartdine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Menu.class);
        Menu menu1 = getMenuSample1();
        Menu menu2 = new Menu();
        assertThat(menu1).isNotEqualTo(menu2);

        menu2.setId(menu1.getId());
        assertThat(menu1).isEqualTo(menu2);

        menu2 = getMenuSample2();
        assertThat(menu1).isNotEqualTo(menu2);
    }

    @Test
    void dishNamesTest() {
        Menu menu = getMenuRandomSampleGenerator();
        Dish dishBack = getDishRandomSampleGenerator();

        menu.setDishNames(dishBack);
        assertThat(menu.getDishNames()).isEqualTo(dishBack);

        menu.dishNames(null);
        assertThat(menu.getDishNames()).isNull();
    }
}
