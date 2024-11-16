import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '88f5e9ef-64de-45b6-88a2-8f28b4233edc',
};

export const sampleWithPartialData: IAuthority = {
  name: 'c43f5a85-d2fb-46f2-84bc-8a631669e2bb',
};

export const sampleWithFullData: IAuthority = {
  name: '0fcecce8-d96e-4708-9948-c9c9b684b23d',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
