export interface PerfilNutricional {
  idUsuario: number;
  sexo: string;
  fechaNacimiento: string;
  alturaCm: number;
  pesoKg: number;
  nivelActividad: string;
  objetivo: string;
  fechaActualizacion: string;
}

export interface PerfilNutricionalDTO {
  sexo: string;
  fechaNacimiento: string;
  alturaCm: number;
  pesoKg: number;
  nivelActividad: string;
  objetivo: string;
}
