import dayjs from 'dayjs';

export interface IActulaite {
  id?: number;
  date?: dayjs.Dayjs | null;
  data?: string | null;
  imageContentType?: string | null;
  image?: string | null;
}

export const defaultValue: Readonly<IActulaite> = {};
