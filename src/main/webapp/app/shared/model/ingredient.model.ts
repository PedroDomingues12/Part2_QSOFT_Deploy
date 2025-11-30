export interface IIngredient {
  id?: number;
  name?: string;
  enabled?: boolean;
  category?: string | null;
}

export const defaultValue: Readonly<IIngredient> = {
  enabled: false,
};
