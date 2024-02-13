import { IEtudiant } from 'app/shared/model/etudiant.model';
import { TypeFichierAdmin } from 'app/shared/model/enumerations/type-fichier-admin.model';

export interface IFichierAdmin {
  id?: number;
  titre?: string | null;
  type?: keyof typeof TypeFichierAdmin | null;
  fileContentType?: string | null;
  file?: string | null;
  demandeValide?: boolean | null;
  etudiant?: IEtudiant | null;
}

export const defaultValue: Readonly<IFichierAdmin> = {
  demandeValide: false,
};
