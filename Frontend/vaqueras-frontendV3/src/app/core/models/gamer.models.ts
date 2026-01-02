export interface BibliotecaItem {
  idVideojuego: number;
  titulo: string;
  portadaUrl?: string | null;
  instalado?: boolean;
  fechaCompra?: string;
  precio?: number;
}

export interface CarteraResumen {
  saldo?: number;
  saldoActual?: number; // por si tu backend lo nombra así
}

export interface RecargaItem {
  idRecarga?: number;
  monto: number;
  fecha?: string;
}

export interface Grupo {
  idGrupo: number;
  nombre: string;
  creadorId?: number;
}

export interface Miembro {
  idUsuario: number;
  nickname?: string;
  email?: string;
  rol?: string;
  // si el backend incluye préstamos, lo dibujamos
  prestamos?: any[];
}

export interface GamerPerfil {
  idUsuario?: number;
  nickname?: string;
  email?: string;
  nombre?: string;
  fechaNacimiento?: string;
  avatarUrl?: string | null;
}
