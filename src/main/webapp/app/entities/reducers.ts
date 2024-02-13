import etudiant from 'app/entities/etudiant/etudiant.reducer';
import administrateur from 'app/entities/administrateur/administrateur.reducer';
import professeur from 'app/entities/professeur/professeur.reducer';
import niveau from 'app/entities/niveau/niveau.reducer';
import fichierAdmin from 'app/entities/fichier-admin/fichier-admin.reducer';
import actulaite from 'app/entities/actulaite/actulaite.reducer';
import note from 'app/entities/note/note.reducer';
import club from 'app/entities/club/club.reducer';
import seance from 'app/entities/seance/seance.reducer';
import matiere from 'app/entities/matiere/matiere.reducer';
import cours from 'app/entities/cours/cours.reducer';
import semestre from 'app/entities/semestre/semestre.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  etudiant,
  administrateur,
  professeur,
  niveau,
  fichierAdmin,
  actulaite,
  note,
  club,
  seance,
  matiere,
  cours,
  semestre,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
