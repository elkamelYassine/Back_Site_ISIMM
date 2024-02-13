import { IMatiere } from 'app/shared/model/matiere.model';
import { INiveau } from 'app/shared/model/niveau.model';
import { Jours } from 'app/shared/model/enumerations/jours.model';
import { Salle } from 'app/shared/model/enumerations/salle.model';

export interface ISeance {
  id?: number;
  jour?: keyof typeof Jours | null;
  numSeance?: number | null;
  salle?: keyof typeof Salle | null;
  matiere?: IMatiere | null;
  niveau?: INiveau | null;
}

export const defaultValue: Readonly<ISeance> = {};
