import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AlimentosService } from '../../services/alimentos.service';
import { Alimento } from '../../models/alimento.model';

@Component({
  selector: 'app-alimentos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './alimentos.component.html',
  styleUrls: ['./alimentos.component.scss']
})
export class AlimentosComponent implements OnInit {
  alimentos: Alimento[] = [];
  alimentosFiltrados: Alimento[] = [];

  loading = false;
  busqueda = '';

  constructor(private alimentosService: AlimentosService) { }

  ngOnInit(): void {
    this.cargarAlimentos();
  }

  cargarAlimentos(): void {
    this.loading = true;
    this.alimentosService.obtenerTodos().subscribe({
      next: (alimentos) => {
        this.alimentos = alimentos;
        this.alimentosFiltrados = alimentos;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando alimentos:', err);
        this.loading = false;
      }
    });
  }

  buscar(): void {
    if (!this.busqueda.trim()) {
      this.alimentosFiltrados = this.alimentos;
    } else {
      this.alimentosFiltrados = this.alimentos.filter(alimento =>
        alimento.nombre.toLowerCase().includes(this.busqueda.toLowerCase())
      );
    }
  }

  obtenerColorCategoria(kcal: number): string {
    if (kcal < 50) return 'bajo';
    if (kcal < 150) return 'medio';
    if (kcal < 300) return 'alto';
    return 'muy-alto';
  }

  obtenerCategoriaAlimento(kcal: number): string {
    if (kcal < 50) return 'Muy Bajo en Calorías';
    if (kcal < 150) return 'Bajo en Calorías';
    if (kcal < 300) return 'Moderado en Calorías';
    return 'Alto en Calorías';
  }
}
