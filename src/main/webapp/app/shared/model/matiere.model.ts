import { ISemestre } from 'app/shared/model/semestre.model';
import { INote } from 'app/shared/model/note.model';
import { ICours } from 'app/shared/model/cours.model';
import { ISeance } from 'app/shared/model/seance.model';
import { IProfesseur } from 'app/shared/model/professeur.model';

export interface IMatiere {
  id?: number;
  nomMatiere?: string | null;
  semestre?: ISemestre | null;
  note?: INote | null;
  cours?: ICours[] | null;
  seance?: ISeance | null;
  professeurs?: IProfesseur[] | null;
}

export const defaultValue: Readonly<IMatiere> = {};
