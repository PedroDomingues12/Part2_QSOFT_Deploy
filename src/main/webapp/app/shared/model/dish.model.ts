import { IIngredient } from 'app/shared/model/ingredient.model';
import { IPurchase } from 'app/shared/model/purchase.model';

export interface IDish {
  id?: number;
  name?: string;
  price?: number;
  ingridientName?: IIngredient;
  purchases?: IPurchase[] | null;
}

export const defaultValue: Readonly<IDish> = {};
