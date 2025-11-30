import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Purchase from './purchase';
import PurchaseDetail from './purchase-detail';
import PurchaseUpdate from './purchase-update';
import PurchaseDeleteDialog from './purchase-delete-dialog';

const PurchaseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Purchase />} />
    <Route path="new" element={<PurchaseUpdate />} />
    <Route path=":id">
      <Route index element={<PurchaseDetail />} />
      <Route path="edit" element={<PurchaseUpdate />} />
      <Route path="delete" element={<PurchaseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PurchaseRoutes;
