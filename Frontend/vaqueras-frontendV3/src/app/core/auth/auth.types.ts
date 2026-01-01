export type Role = 'ADMIN' | 'EMPRESA' | 'GAMER';

export interface TokenUser {
  idUser: number;
  rol: Role;
  nickname?: string;
  correo?: string;
}

export interface SessionResponse {
  authenticated: boolean;
  user?: TokenUser;
}

export interface LoginResponse {
  token: string;
  user?: TokenUser;
}

export interface LoginRequest {
  identifier: string;
  password: string;
}
