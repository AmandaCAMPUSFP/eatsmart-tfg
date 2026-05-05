export interface Comida {
  idComida: number;
  idUsuario: number;
  fecha: string;
  tipoComida: string;
  fechaCreacion: string;
  alimentos?: any[];
  recetas?: any[];
}

export interface ComidaDTO {
  idUsuario: number;
  fecha: string;
  tipoComida: string;
  idAlimentos?: number[];
  idRecetas?: number[];
}

export interface ResumenDiario {
  totalKcal: number;
  totalProteinas: number;
  totalCarbohidratos: number;
  totalGrasas: number;
  objetivoKcal: number;
}
