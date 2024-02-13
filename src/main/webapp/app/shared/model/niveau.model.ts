import { ISemestre } from 'app/shared/model/semestre.model';
import { ISeance } from 'app/shared/model/seance.model';
import { IEtudiant } from 'app/shared/model/etudiant.model';

export interface INiveau {
  id?: number;
  classe?: string | null;
  tp?: string | null;
  td?: string | null;
  semestre?: ISemestre | null;
  seances?: ISeance[] | null;
  etudiants?: IEtudiant[] | null;
}

export const defaultValue: Readonly<INiveau> = {};
