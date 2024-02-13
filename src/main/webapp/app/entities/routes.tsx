import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Etudiant from './etudiant';
import Administrateur from './administrateur';
import Professeur from './professeur';
import Niveau from './niveau';
import FichierAdmin from './fichier-admin';
import Actulaite from './actulaite';
import Note from './note';
import Club from './club';
import Seance from './seance';
import Matiere from './matiere';
import Cours from './cours';
import Semestre from './semestre';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="etudiant/*" element={<Etudiant />} />
        <Route path="administrateur/*" element={<Administrateur />} />
        <Route path="professeur/*" element={<Professeur />} />
        <Route path="niveau/*" element={<Niveau />} />
        <Route path="fichier-admin/*" element={<FichierAdmin />} />
        <Route path="actulaite/*" element={<Actulaite />} />
        <Route path="note/*" element={<Note />} />
        <Route path="club/*" element={<Club />} />
        <Route path="seance/*" element={<Seance />} />
        <Route path="matiere/*" element={<Matiere />} />
        <Route path="cours/*" element={<Cours />} />
        <Route path="semestre/*" element={<Semestre />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
