import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Receta } from '../models/receta.model';

@Injectable({
  providedIn: 'root'
})
export class RecetasService {
  private apiUrl = 'http://localhost:8080/api/recetas';

  constructor(private http: HttpClient) { }

  obtenerTodas(): Observable<Receta[]> {
    return this.http.get<Receta[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Receta> {
    return this.http.get<Receta>(`${this.apiUrl}/${id}`);
  }

  buscar(nombre: string): Observable<Receta[]> {
    return this.http.get<Receta[]>(`${this.apiUrl}/buscar?nombre=${nombre}`);
  }
}
