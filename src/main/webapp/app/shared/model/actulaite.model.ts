import dayjs from 'dayjs';

export interface IActulaite {
  id?: number;
  date?: dayjs.Dayjs | null;
  data?: string | null;
}

export const defaultValue: Readonly<IActulaite> = {};
