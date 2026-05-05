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
    nivelActividad: 'moderado',
    objetivo: 'mantenimiento'
  };

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
          console.error('Error cargando perfil:', err);
          this.error = 'No se pudo cargar el perfil';
        }
      });
    }
  }

  guardar(): void {
    this.loading = true;
    this.error = '';
    this.success = false;

    const usuario = this.authService.getUsuario();
    if (usuario) {
      this.perfilService.actualizar(usuario.idUsuario, this.formData).subscribe({
        next: (perfil) => {
          this.perfil = perfil;
          this.success = true;
          this.loading = false;
          this.calcularEstadisticas();
          setTimeout(() => this.success = false, 3000);
        },
        error: (err) => {
          this.error = 'Error guardando perfil: ' + err.message;
          this.loading = false;
        }
      });
    }
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
