import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Alimento } from '../models/alimento.model';

@Injectable({
  providedIn: 'root'
})
export class AlimentosService {
  private apiUrl = 'http://localhost:8080/api/alimentos';

  constructor(private http: HttpClient) { }

  obtenerTodos(): Observable<Alimento[]> {
    return this.http.get<Alimento[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Alimento> {
    return this.http.get<Alimento>(`${this.apiUrl}/${id}`);
  }

  buscar(nombre: string): Observable<Alimento[]> {
    return this.http.get<Alimento[]>(`${this.apiUrl}/buscar?nombre=${nombre}`);
  }
}
