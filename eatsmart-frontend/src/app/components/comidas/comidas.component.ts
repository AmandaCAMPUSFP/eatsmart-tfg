import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ComidasService } from '../../services/comidas.service';
import { AlimentosService } from '../../services/alimentos.service';
import { PerfilService } from '../../services/perfil.service';
import { AuthService } from '../../services/auth.service';
import { Comida, ComidaDTO, ResumenDiario } from '../../models/comida.model';
import { Alimento } from '../../models/alimento.model';
import { PerfilNutricional } from '../../models/perfil-nutricional.model';

@Component({
  selector: 'app-comidas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './comidas.component.html',
  styleUrls: ['./comidas.component.scss']
})
export class ComidasComponent implements OnInit {
  comidas: Comida[] = [];
  alimentos: Alimento[] = [];
  perfil: PerfilNutricional | null = null;
  resumenDiario: ResumenDiario | null = null;

  loading = false;
  error = '';
  success = false;

  formularioComida: ComidaDTO = {
    idUsuario: 0,
    fecha: new Date().toISOString().split('T')[0],
    tipoComida: 'Desayuno',
    idAlimentos: []
  };

  alimentoSeleccionadoId: number | null = null;
  cantidadGramos = 100;

  constructor(
    private comidasService: ComidasService,
    private alimentosService: AlimentosService,
    private perfilService: PerfilService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    const usuario = this.authService.getUsuario();
    if (usuario) {
      this.formularioComida.idUsuario = usuario.idUsuario;

      // Cargar alimentos
      this.alimentosService.obtenerTodos().subscribe({
        next: (alimentos) => this.alimentos = alimentos,
        error: (err) => console.error('Error cargando alimentos:', err)
      });

      // Cargar comidas del día
      this.comidasService.obtenerPorUsuarioYFecha(
        usuario.idUsuario,
        this.formularioComida.fecha
      ).subscribe({
        next: (comidas) => this.comidas = comidas,
        error: (err) => console.error('Error cargando comidas:', err)
      });

      // Cargar perfil para objetivo calórico
      this.perfilService.obtenerPorId(usuario.idUsuario).subscribe({
        next: (perfil) => {
          this.perfil = perfil;
          this.calcularResumen();
        },
        error: (err) => console.error('Error cargando perfil:', err)
      });
    }
  }

  agregarAlimento(): void {
    if (this.alimentoSeleccionadoId != null && this.formularioComida.idAlimentos) {
      this.formularioComida.idAlimentos.push(this.alimentoSeleccionadoId);
      this.alimentoSeleccionadoId = null;
    } else {
      this.error = 'Selecciona un alimento antes de agregarlo';
    }
  }

  guardarComida(): void {
    if (!this.formularioComida.idAlimentos || this.formularioComida.idAlimentos.length === 0) {
      this.error = 'Debe agregar al menos un alimento';
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = false;

    this.comidasService.crear(this.formularioComida).subscribe({
      next: () => {
        this.success = true;
        this.loading = false;
        this.cargarDatos();
        this.formularioComida.idAlimentos = [];
        setTimeout(() => this.success = false, 3000);
      },
      error: (err) => {
        this.error = 'Error guardando comida: ' + err.message;
        this.loading = false;
      }
    });
  }

  eliminarComida(id: number): void {
    if (confirm('¿Eliminar esta comida?')) {
      this.comidasService.eliminar(id).subscribe({
        next: () => this.cargarDatos(),
        error: (err) => console.error('Error eliminando comida:', err)
      });
    }
  }

  calcularResumen(): void {
    if (this.perfil && this.comidas.length > 0) {
      const objetivoKcal = this.perfilService.calcularObjetivoCalorico(this.perfil);
      this.resumenDiario = this.comidasService.calcularResumenDiario(this.comidas, objetivoKcal);
    }
  }

  cambiarFecha(): void {
    const usuario = this.authService.getUsuario();
    if (usuario) {
      this.comidasService.obtenerPorUsuarioYFecha(
        usuario.idUsuario,
        this.formularioComida.fecha
      ).subscribe({
        next: (comidas) => this.comidas = comidas,
        error: (err) => console.error('Error cargando comidas:', err)
      });
    }
  }
}
