import dayjs from 'dayjs';

export interface IAdministrateur {
  id?: number;
  prenom?: string | null;
  nom?: string | null;
  email?: string | null;
  matricule?: string | null;
  titre?: string | null;
  numTel?: string | null;
  dateDeNaissance?: dayjs.Dayjs | null;
  photoDeProfileContentType?: string | null;
  photoDeProfile?: string | null;
}

export const defaultValue: Readonly<IAdministrateur> = {};
