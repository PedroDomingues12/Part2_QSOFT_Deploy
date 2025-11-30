package com.jhipster.smartdine.domain;

import static com.jhipster.smartdine.domain.DishTestSamples.*;
import static com.jhipster.smartdine.domain.IngredientTestSamples.*;
import static com.jhipster.smartdine.domain.PurchaseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.smartdine.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DishTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dish.class);
        Dish dish1 = getDishSample1();
        Dish dish2 = new Dish();
        assertThat(dish1).isNotEqualTo(dish2);

        dish2.setId(dish1.getId());
        assertThat(dish1).isEqualTo(dish2);

        dish2 = getDishSample2();
        assertThat(dish1).isNotEqualTo(dish2);
    }

    @Test
    void ingridientNameTest() {
        Dish dish = getDishRandomSampleGenerator();
        Ingredient ingredientBack = getIngredientRandomSampleGenerator();

        dish.setIngridientName(ingredientBack);
        assertThat(dish.getIngridientName()).isEqualTo(ingredientBack);

        dish.ingridientName(null);
        assertThat(dish.getIngridientName()).isNull();
    }

    @Test
    void purchaseTest() {
        Dish dish = getDishRandomSampleGenerator();
        Purchase purchaseBack = getPurchaseRandomSampleGenerator();

        dish.addPurchase(purchaseBack);
        assertThat(dish.getPurchases()).containsOnly(purchaseBack);
        assertThat(purchaseBack.getDishes()).containsOnly(dish);

        dish.removePurchase(purchaseBack);
        assertThat(dish.getPurchases()).doesNotContain(purchaseBack);
        assertThat(purchaseBack.getDishes()).doesNotContain(dish);

        dish.purchases(new HashSet<>(Set.of(purchaseBack)));
        assertThat(dish.getPurchases()).containsOnly(purchaseBack);
        assertThat(purchaseBack.getDishes()).containsOnly(dish);

        dish.setPurchases(new HashSet<>());
        assertThat(dish.getPurchases()).doesNotContain(purchaseBack);
        assertThat(purchaseBack.getDishes()).doesNotContain(dish);
    }
}
