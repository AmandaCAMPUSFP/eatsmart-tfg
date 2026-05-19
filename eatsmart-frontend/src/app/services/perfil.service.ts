import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PerfilNutricional, PerfilNutricionalDTO } from '../models/perfil-nutricional.model';

@Injectable({
  providedIn: 'root'
})
export class PerfilService {
  private apiUrl = 'http://localhost:8080/api/perfiles-nutricionales';

  constructor(private http: HttpClient) { }

  obtenerPorId(id: number): Observable<PerfilNutricional> {
    return this.http.get<PerfilNutricional>(`${this.apiUrl}/${id}`);
  }

  crear(id: number, perfil: PerfilNutricionalDTO): Observable<PerfilNutricional> {
    return this.http.post<PerfilNutricional>(`${this.apiUrl}/${id}`, perfil);
  }

  actualizar(id: number, perfil: PerfilNutricionalDTO): Observable<PerfilNutricional> {
    return this.http.put<PerfilNutricional>(`${this.apiUrl}/${id}`, perfil);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Cálculos nutricionales
  calcularObjetivoCalorico(perfil: PerfilNutricional): number {
    // TMB (Tasa Metabólica Basal)
    const edad = new Date().getFullYear() - new Date(perfil.fechaNacimiento).getFullYear();
    let tmb: number;

    if (perfil.sexo.toLowerCase() === 'hombre') {
      tmb = 88.362 + (13.397 * perfil.pesoKg) + (4.799 * perfil.alturaCm) - (5.677 * edad);
    } else {
      tmb = 447.593 + (9.247 * perfil.pesoKg) + (3.098 * perfil.alturaCm) - (4.330 * edad);
    }

    // Factor de actividad
    let factorActividad = 1.2;
    switch (perfil.nivelActividad?.toLowerCase()) {
      case 'ligero':
        factorActividad = 1.375;
        break;
      case 'moderado':
        factorActividad = 1.55;
        break;
      case 'intenso':
        factorActividad = 1.725;
        break;
      case 'muy intenso':
        factorActividad = 1.9;
        break;
    }

    let tdee = tmb * factorActividad;

    // Ajustar según objetivo
    if (perfil.objetivo?.toLowerCase() === 'pérdida de peso') {
      tdee *= 0.85; // 15% déficit
    } else if (perfil.objetivo?.toLowerCase() === 'ganancia muscular') {
      tdee *= 1.1; // 10% superávit
    }

    return Math.round(tdee);
  }
}
