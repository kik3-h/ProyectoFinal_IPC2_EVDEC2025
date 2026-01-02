export interface VideojuegoPublic {
  idVideojuego: number;
  titulo: string;
  precio: number;
  clasificacionEdad?: string;
  edadMinima?: number;
  idEmpresa?: number;
  nombreEmpresa?: string;
  portadaUrl?: string | null;
}

export interface VideojuegoDetalle {
  idVideojuego: number;
  titulo: string;
  descripcion?: string;
  precio?: number;
  clasificacionEdad?: string;
  edadMinima?: number;
  fechaPublicacion?: string;
  nombreEmpresa?: string;

  // opcionales (porque tu DTO puede traer más cosas)
  categorias?: any[];
  multimedia?: any[];
  portadaUrl?: string | null;
}

export interface Comentario {
  idComentario?: number;
  idUsuario?: number;
  nickname?: string;
  contenido?: string;
  texto?: string;
  estrellas?: number;
  puntuacion?: number;
  fecha?: string;
}
