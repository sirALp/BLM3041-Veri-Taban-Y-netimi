export type Role = 'admin' | 'member' | 'tenant';

export type User = {
  id: number;
  username: string;
  name: string;
  role: Role;
};

export type Log = {
  id: number;
  activity: string;
  timestamp: string;
};