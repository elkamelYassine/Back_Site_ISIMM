import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Professeur from './professeur';
import ProfesseurDetail from './professeur-detail';
import ProfesseurUpdate from './professeur-update';
import ProfesseurDeleteDialog from './professeur-delete-dialog';

const ProfesseurRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Professeur />} />
    <Route path="new" element={<ProfesseurUpdate />} />
    <Route path=":id">
      <Route index element={<ProfesseurDetail />} />
      <Route path="edit" element={<ProfesseurUpdate />} />
      <Route path="delete" element={<ProfesseurDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProfesseurRoutes;
