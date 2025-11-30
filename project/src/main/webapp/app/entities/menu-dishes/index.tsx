import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MenuDishes from './menu-dishes';
import MenuDishesDetail from './menu-dishes-detail';
import MenuDishesUpdate from './menu-dishes-update';
import MenuDishesDeleteDialog from './menu-dishes-delete-dialog';

const MenuDishesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MenuDishes />} />
    <Route path="new" element={<MenuDishesUpdate />} />
    <Route path=":id">
      <Route index element={<MenuDishesDetail />} />
      <Route path="edit" element={<MenuDishesUpdate />} />
      <Route path="delete" element={<MenuDishesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MenuDishesRoutes;
