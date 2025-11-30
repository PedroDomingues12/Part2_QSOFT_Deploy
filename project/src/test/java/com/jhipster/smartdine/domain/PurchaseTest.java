package com.jhipster.smartdine.domain;

import static com.jhipster.smartdine.domain.DishTestSamples.*;
import static com.jhipster.smartdine.domain.PurchaseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.smartdine.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PurchaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Purchase.class);
        Purchase purchase1 = getPurchaseSample1();
        Purchase purchase2 = new Purchase();
        assertThat(purchase1).isNotEqualTo(purchase2);

        purchase2.setId(purchase1.getId());
        assertThat(purchase1).isEqualTo(purchase2);

        purchase2 = getPurchaseSample2();
        assertThat(purchase1).isNotEqualTo(purchase2);
    }

    @Test
    void dishTest() {
        Purchase purchase = getPurchaseRandomSampleGenerator();
        Dish dishBack = getDishRandomSampleGenerator();

        purchase.addDish(dishBack);
        assertThat(purchase.getDishes()).containsOnly(dishBack);

        purchase.removeDish(dishBack);
        assertThat(purchase.getDishes()).doesNotContain(dishBack);

        purchase.dishes(new HashSet<>(Set.of(dishBack)));
        assertThat(purchase.getDishes()).containsOnly(dishBack);

        purchase.setDishes(new HashSet<>());
        assertThat(purchase.getDishes()).doesNotContain(dishBack);
    }
}
