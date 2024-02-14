import dayjs from 'dayjs';
import { IFichierAdmin } from 'app/shared/model/fichier-admin.model';
import { INiveau } from 'app/shared/model/niveau.model';
import { IClub } from 'app/shared/model/club.model';
import { IUser } from 'app/shared/model/user.model';

export interface IEtudiant {
  id?: number;
  prenom?: string | null;
  nom?: string | null;
  email?: string | null;
  numEtudiant?: number | null;
  numTel?: string | null;
  dateDeNaissance?: dayjs.Dayjs | null;
  photoDeProfileContentType?: string | null;
  photoDeProfile?: string | null;
  fichierAdmins?: IFichierAdmin[] | null;
  niveau?: INiveau | null;
  clubs?: IClub[] | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IEtudiant> = {};
