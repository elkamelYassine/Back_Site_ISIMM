import { IMatiere } from 'app/shared/model/matiere.model';

export interface ICours {
  id?: number;
  titre?: string | null;
  fileContentType?: string | null;
  file?: string | null;
  matiere?: IMatiere | null;
}

export const defaultValue: Readonly<ICours> = {};
