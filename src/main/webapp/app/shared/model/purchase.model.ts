import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IDish } from 'app/shared/model/dish.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { PaymentMethods } from 'app/shared/model/enumerations/payment-methods.model';

export interface IPurchase {
  id?: number;
  date?: dayjs.Dayjs;
  ammount?: number | null;
  status?: keyof typeof Status;
  paymentMethod?: keyof typeof PaymentMethods;
  user?: IUser;
  dishes?: IDish[];
}

export const defaultValue: Readonly<IPurchase> = {};
