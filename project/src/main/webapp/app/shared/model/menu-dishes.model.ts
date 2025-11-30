import { IDish } from 'app/shared/model/dish.model';

export interface IMenuDishes {
  id?: number;
  name?: string;
  dishNames?: IDish;
}

export const defaultValue: Readonly<IMenuDishes> = {};
