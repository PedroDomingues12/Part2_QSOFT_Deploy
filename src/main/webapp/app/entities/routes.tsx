import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ingredient from './ingredient';
import Dish from './dish';
import Menu from './menu';
import MenuDishes from './menu-dishes';
import Purchase from './purchase';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="ingredient/*" element={<Ingredient />} />
        <Route path="dish/*" element={<Dish />} />
        <Route path="menu/*" element={<Menu />} />
        <Route path="menu-dishes/*" element={<MenuDishes />} />
        <Route path="purchase/*" element={<Purchase />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
