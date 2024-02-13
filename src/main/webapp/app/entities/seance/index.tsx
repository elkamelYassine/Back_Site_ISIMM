import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Seance from './seance';
import SeanceDetail from './seance-detail';
import SeanceUpdate from './seance-update';
import SeanceDeleteDialog from './seance-delete-dialog';

const SeanceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Seance />} />
    <Route path="new" element={<SeanceUpdate />} />
    <Route path=":id">
      <Route index element={<SeanceDetail />} />
      <Route path="edit" element={<SeanceUpdate />} />
      <Route path="delete" element={<SeanceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SeanceRoutes;
