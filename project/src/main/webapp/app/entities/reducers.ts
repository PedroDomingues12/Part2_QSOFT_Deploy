import ingredient from 'app/entities/ingredient/ingredient.reducer';
import dish from 'app/entities/dish/dish.reducer';
import menu from 'app/entities/menu/menu.reducer';
import menuDishes from 'app/entities/menu-dishes/menu-dishes.reducer';
import purchase from 'app/entities/purchase/purchase.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  ingredient,
  dish,
  menu,
  menuDishes,
  purchase,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
