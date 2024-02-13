import { IEtudiant } from 'app/shared/model/etudiant.model';

export interface IClub {
  id?: number;
  nom?: string | null;
  pageFB?: string | null;
  pageIg?: string | null;
  email?: string | null;
  etudiants?: IEtudiant[] | null;
}

export const defaultValue: Readonly<IClub> = {};
