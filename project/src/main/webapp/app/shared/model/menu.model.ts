import { IDish } from 'app/shared/model/dish.model';

export interface IMenu {
  id?: number;
  name?: string;
  dishNames?: IDish;
}

export const defaultValue: Readonly<IMenu> = {};
