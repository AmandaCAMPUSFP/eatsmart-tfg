import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Meal, MealLog } from '../models/meal.model';

@Injectable({
  providedIn: 'root'
})
export class MealService {
  private apiUrl = 'http://localhost:3000/api/meals';

  constructor(private http: HttpClient) { }

  getAllMeals(): Observable<Meal[]> {
    return this.http.get<Meal[]>(this.apiUrl);
  }

  getMealById(id: string): Observable<Meal> {
    return this.http.get<Meal>(`${this.apiUrl}/${id}`);
  }

  createMeal(meal: Omit<Meal, 'id' | 'createdAt'>): Observable<Meal> {
    return this.http.post<Meal>(this.apiUrl, meal);
  }

  updateMeal(id: string, meal: Partial<Meal>): Observable<Meal> {
    return this.http.put<Meal>(`${this.apiUrl}/${id}`, meal);
  }

  deleteMeal(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  logMeal(log: Omit<MealLog, 'id'>): Observable<MealLog> {
    return this.http.post<MealLog>(`${this.apiUrl}/log`, log);
  }

  getMealLogs(userId: string): Observable<MealLog[]> {
    return this.http.get<MealLog[]>(`${this.apiUrl}/logs/${userId}`);
  }
}
