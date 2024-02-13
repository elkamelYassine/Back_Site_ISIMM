import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FichierAdmin from './fichier-admin';
import FichierAdminDetail from './fichier-admin-detail';
import FichierAdminUpdate from './fichier-admin-update';
import FichierAdminDeleteDialog from './fichier-admin-delete-dialog';

const FichierAdminRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FichierAdmin />} />
    <Route path="new" element={<FichierAdminUpdate />} />
    <Route path=":id">
      <Route index element={<FichierAdminDetail />} />
      <Route path="edit" element={<FichierAdminUpdate />} />
      <Route path="delete" element={<FichierAdminDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FichierAdminRoutes;
