import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Niveau from './niveau';
import NiveauDetail from './niveau-detail';
import NiveauUpdate from './niveau-update';
import NiveauDeleteDialog from './niveau-delete-dialog';

const NiveauRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Niveau />} />
    <Route path="new" element={<NiveauUpdate />} />
    <Route path=":id">
      <Route index element={<NiveauDetail />} />
      <Route path="edit" element={<NiveauUpdate />} />
      <Route path="delete" element={<NiveauDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default NiveauRoutes;
