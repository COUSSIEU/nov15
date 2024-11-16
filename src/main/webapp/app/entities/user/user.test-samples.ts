import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 920,
  login: 'Z4aeAC',
};

export const sampleWithPartialData: IUser = {
  id: 6602,
  login: 'qA52d@H1\\1s\\7JCRTY\\Q0fRay',
};

export const sampleWithFullData: IUser = {
  id: 26188,
  login: 'Y@hhFRcn\\%JlkKB\\{aR\\Oa\\FMI-K',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
