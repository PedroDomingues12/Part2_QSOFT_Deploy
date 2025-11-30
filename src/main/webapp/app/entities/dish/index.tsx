import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Dish from './dish';
import DishDetail from './dish-detail';
import DishUpdate from './dish-update';
import DishDeleteDialog from './dish-delete-dialog';

const DishRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Dish />} />
    <Route path="new" element={<DishUpdate />} />
    <Route path=":id">
      <Route index element={<DishDetail />} />
      <Route path="edit" element={<DishUpdate />} />
      <Route path="delete" element={<DishDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DishRoutes;
