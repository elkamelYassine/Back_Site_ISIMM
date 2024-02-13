import { IMatiere } from 'app/shared/model/matiere.model';

export interface INote {
  id?: number;
  note?: number | null;
  matiere?: IMatiere | null;
}

export const defaultValue: Readonly<INote> = {};
