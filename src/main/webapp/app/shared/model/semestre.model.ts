import { INiveau } from 'app/shared/model/niveau.model';
import { IMatiere } from 'app/shared/model/matiere.model';

export interface ISemestre {
  id?: number;
  anneeScolaire?: string | null;
  s?: number | null;
  niveau?: INiveau | null;
  matiere?: IMatiere | null;
}

export const defaultValue: Readonly<ISemestre> = {};
