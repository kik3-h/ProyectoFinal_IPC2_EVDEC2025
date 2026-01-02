export type Role = 'ADMIN' | 'EMPRESA' | 'GAMER';

export interface TokenUser {
  idUser: number;
  nickname?: string;
  correo?: string;
  rol: Role;
}

export interface SessionResponse {
  authenticated: boolean;
  user?: TokenUser;
}

export interface LoginResponse {
  token: string;
  idUser: number;
  nickname: string;
  email: string; 
  rol: string;
}

export interface LoginRequest {
  identifier: string;
  password: string;
}
