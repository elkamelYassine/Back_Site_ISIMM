import dayjs from 'dayjs';
import { IMatiere } from 'app/shared/model/matiere.model';
import { Departement } from 'app/shared/model/enumerations/departement.model';

export interface IProfesseur {
  id?: number;
  prenom?: string | null;
  nom?: string | null;
  email?: string | null;
  matricule?: string | null;
  departement?: keyof typeof Departement | null;
  titre?: string | null;
  numTel?: string | null;
  dateDeNaissance?: dayjs.Dayjs | null;
  photoDeProfileContentType?: string | null;
  photoDeProfile?: string | null;
  matieres?: IMatiere[] | null;
}

export const defaultValue: Readonly<IProfesseur> = {};
