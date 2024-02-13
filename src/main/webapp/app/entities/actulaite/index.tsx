import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Actulaite from './actulaite';
import ActulaiteDetail from './actulaite-detail';
import ActulaiteUpdate from './actulaite-update';
import ActulaiteDeleteDialog from './actulaite-delete-dialog';

const ActulaiteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Actulaite />} />
    <Route path="new" element={<ActulaiteUpdate />} />
    <Route path=":id">
      <Route index element={<ActulaiteDetail />} />
      <Route path="edit" element={<ActulaiteUpdate />} />
      <Route path="delete" element={<ActulaiteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ActulaiteRoutes;
