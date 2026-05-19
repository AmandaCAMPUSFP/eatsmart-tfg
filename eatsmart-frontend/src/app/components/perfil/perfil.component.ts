import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PerfilService } from '../../services/perfil.service';
import { PerfilNutricional, PerfilNutricionalDTO } from '../../models/perfil-nutricional.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent implements OnInit {
  perfil: PerfilNutricional | null = null;
  formData: PerfilNutricionalDTO = {
    sexo: 'Hombre',
    fechaNacimiento: '',
    alturaCm: 0,
    pesoKg: 0,
    nivelActividad: 'Moderado',
    objetivo: 'Mantenimiento'
  };

  // Indica si el perfil ya existe (para decidir entre crear o actualizar)
  perfilExiste = false;

  loading = false;
  error = '';
  success = false;
  estadisticas: any = {};

  constructor(
    private perfilService: PerfilService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    const usuario = this.authService.getUsuario();
    if (usuario) {
      this.perfilService.obtenerPorId(usuario.idUsuario).subscribe({
        next: (perfil) => {
          this.perfil = perfil;
          this.perfilExiste = true;
          this.formData = {
            sexo: perfil.sexo,
            fechaNacimiento: perfil.fechaNacimiento,
            alturaCm: perfil.alturaCm,
            pesoKg: perfil.pesoKg,
            nivelActividad: perfil.nivelActividad,
            objetivo: perfil.objetivo
          };
          this.calcularEstadisticas();
        },
        error: (err) => {
          // 404 = el usuario aún no tiene perfil. No es un error real:
          // simplemente todavía no lo ha creado.
          if (err.status === 404) {
            this.perfilExiste = false;
          } else {
            console.error('Error cargando perfil:', err);
            this.error = 'No se pudo cargar el perfil';
          }
        }
      });
    }
  }

  guardar(): void {
    this.loading = true;
    this.error = '';
    this.success = false;

    const usuario = this.authService.getUsuario();
    if (!usuario) {
      this.loading = false;
      return;
    }

    // Si el perfil ya existe -> actualizar (PUT). Si no -> crear (POST).
    const peticion = this.perfilExiste
      ? this.perfilService.actualizar(usuario.idUsuario, this.formData)
      : this.perfilService.crear(usuario.idUsuario, this.formData);

    peticion.subscribe({
      next: (perfil) => {
        this.perfil = perfil;
        this.perfilExiste = true;
        this.success = true;
        this.loading = false;
        this.calcularEstadisticas();
        setTimeout(() => this.success = false, 3000);
      },
      error: (err) => {
        const msg = err?.error?.message || err?.message || 'Error desconocido';
        this.error = 'Error guardando perfil: ' + msg;
        this.loading = false;
      }
    });
  }

  calcularEstadisticas(): void {
    if (this.perfil) {
      const objetivoKcal = this.perfilService.calcularObjetivoCalorico(this.perfil);
      this.estadisticas = {
        objetivoKcal,
        imc: (this.perfil.pesoKg / ((this.perfil.alturaCm / 100) ** 2)).toFixed(1)
      };
    }
  }
}
