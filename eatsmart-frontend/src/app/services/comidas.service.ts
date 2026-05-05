import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comida, ComidaDTO, ResumenDiario } from '../models/comida.model';

@Injectable({
  providedIn: 'root'
})
export class ComidasService {
  private apiUrl = 'http://localhost:8080/api/comidas';

  constructor(private http: HttpClient) { }

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

  // Cálculo de resumen diario
  calcularResumenDiario(comidas: Comida[], objetivoKcal: number): ResumenDiario {
    let totalKcal = 0;
    let totalProteinas = 0;
    let totalCarbohidratos = 0;
    let totalGrasas = 0;

    comidas.forEach(comida => {
      if (comida.alimentos) {
        comida.alimentos.forEach(alimento => {
          totalKcal += (alimento.kcal100g / 100) * alimento.cantidad;
          totalProteinas += (alimento.proteinas100g / 100) * alimento.cantidad;
          totalCarbohidratos += (alimento.carbohidratos100g / 100) * alimento.cantidad;
          totalGrasas += (alimento.grasas100g / 100) * alimento.cantidad;
        });
      }
    });

    return {
      totalKcal: Math.round(totalKcal),
      totalProteinas: Math.round(totalProteinas * 10) / 10,
      totalCarbohidratos: Math.round(totalCarbohidratos * 10) / 10,
      totalGrasas: Math.round(totalGrasas * 10) / 10,
      objetivoKcal
    };
  }
}
