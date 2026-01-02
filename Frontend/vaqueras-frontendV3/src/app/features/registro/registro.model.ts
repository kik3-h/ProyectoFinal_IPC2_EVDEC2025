export type RolRegistro = 'GAMER' | 'EMPRESA' | 'ADMIN';

export interface RegistroUsuarioRequest {
  nickname: string;
  email: string;
  password: string;
  telefono: string;
  fechaNacimiento: string; // "YYYY-MM-DD"
  pais: string;            // "GT"
  rol: RolRegistro;        // normalmente GAMER
}
