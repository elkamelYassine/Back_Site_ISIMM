import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Semestre from './semestre';
import SemestreDetail from './semestre-detail';
import SemestreUpdate from './semestre-update';
import SemestreDeleteDialog from './semestre-delete-dialog';

const SemestreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Semestre />} />
    <Route path="new" element={<SemestreUpdate />} />
    <Route path=":id">
      <Route index element={<SemestreDetail />} />
      <Route path="edit" element={<SemestreUpdate />} />
      <Route path="delete" element={<SemestreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SemestreRoutes;
