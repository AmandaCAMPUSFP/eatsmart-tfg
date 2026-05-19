import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comida, ComidaDTO, ResumenDiario } from '../models/comida.model';

@Injectable({
  providedIn: 'root'
})
export class ComidasService {
  private apiUrl = 'http://localhost:8080/api/comidas';

  constructor(private http: HttpClient) {
  }

  obtenerTodasMias(): Observable<Comida[]> {
    return this.http.get<Comida[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Comida> {
    return this.http.get<Comida>(`${this.apiUrl}/${id}`);
  }

  obtenerPorUsuarioYFecha(idUsuario: number, fecha: string): Observable<Comida[]> {
    return this.http.get<Comida[]>(`${this.apiUrl}/usuario/${idUsuario}/fecha/${fecha}`);
  }

  crear(comida: ComidaDTO): Observable<Comida> {
    return this.http.post<Comida>(this.apiUrl, comida);
  }

  actualizar(id: number, comida: ComidaDTO): Observable<Comida> {
    return this.http.put<Comida>(`${this.apiUrl}/${id}`, comida);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Cálculo de resumen diario.
  // NOTA: el modelo N:M actual (INCLUYE_ALIMENTO) no almacena la cantidad
  // en gramos por alimento. Como aproximación se asume una ración estándar
  // de 100g por alimento. Mejora planificada: tabla intermedia con cantidad
  // (documentado como trabajo futuro en la memoria).
  calcularResumenDiario(comidas: Comida[], objetivoKcal: number): ResumenDiario {
    let totalKcal = 0;
    let totalProteinas = 0;
    let totalCarbohidratos = 0;
    let totalGrasas = 0;

    const CANTIDAD_ESTANDAR = 100; // gramos por alimento (aproximación)

    comidas.forEach(comida => {
      if (comida.alimentos && comida.alimentos.length > 0) {
        comida.alimentos.forEach(alimento => {
          const cantidad = alimento.cantidad ?? CANTIDAD_ESTANDAR;
          const kcal = alimento.kcal100g ?? 0;
          const prot = alimento.proteinas100g ?? 0;
          const carb = alimento.carbohidratos100g ?? 0;
          const gras = alimento.grasas100g ?? 0;

          totalKcal += (kcal / 100) * cantidad;
          totalProteinas += (prot / 100) * cantidad;
          totalCarbohidratos += (carb / 100) * cantidad;
          totalGrasas += (gras / 100) * cantidad;
        });
      }
    });

    return {
      totalKcal: Math.round(totalKcal) || 0,
      totalProteinas: Math.round(totalProteinas * 10) / 10 || 0,
      totalCarbohidratos: Math.round(totalCarbohidratos * 10) / 10 || 0,
      totalGrasas: Math.round(totalGrasas * 10) / 10 || 0,
      objetivoKcal: objetivoKcal || 0
    };
  }
}
