import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { PerfilService } from '../../services/perfil.service';
import { ComidasService } from '../../services/comidas.service';
import { PerfilNutricional } from '../../models/perfil-nutricional.model';
import { ResumenDiario } from '../../models/comida.model';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, MatIconModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  usuarioEmail: string | null = null;
  perfil: PerfilNutricional | null = null;
  resumenHoy: ResumenDiario | null = null;
  loading = true;

  constructor(
    private authService: AuthService,
    private perfilService: PerfilService,
    private comidasService: ComidasService
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    const usuario = this.authService.getUsuario();
    this.usuarioEmail = usuario?.email || null;

    if (usuario) {
      // Cargar perfil
      this.perfilService.obtenerPorId(usuario.idUsuario).subscribe({
        next: (perfil) => {
          this.perfil = perfil;
          this.cargarResumenDia(usuario.idUsuario);
        },
        error: (err) => {
          console.error('Error cargando perfil:', err);
          this.loading = false;
        }
      });
    }
  }

  cargarResumenDia(idUsuario: number): void {
    const fechaHoy = new Date().toISOString().split('T')[0];
    this.comidasService.obtenerPorUsuarioYFecha(idUsuario, fechaHoy).subscribe({
      next: (comidas) => {
        const objetivoKcal = this.perfil ? this.perfilService.calcularObjetivoCalorico(this.perfil) : 0;
        this.resumenHoy = this.comidasService.calcularResumenDiario(comidas, objetivoKcal);
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando comidas:', err);
        this.loading = false;
      }
    });
  }

  obtenerPorcentajeKcal(): number {
    if (!this.resumenHoy) return 0;
    return (this.resumenHoy.totalKcal / this.resumenHoy.objetivoKcal) * 100;
  }

  obtenerEstadoKcal(): string {
    const porcentaje = this.obtenerPorcentajeKcal();
    if (porcentaje < 90) return 'bajo';
    if (porcentaje > 110) return 'alto';
    return 'normal';
  }

  /**
   * Calcula la edad a partir de una fecha de nacimiento.
   * Tiene en cuenta mes y día (no solo el año).
   * Si la fecha no es válida, devuelve 0.
   */
  calcularEdad(fechaNacimiento: string | Date | null | undefined): number {
    if (!fechaNacimiento) return 0;

    const nacimiento = new Date(fechaNacimiento);
    if (isNaN(nacimiento.getTime())) return 0;

    const hoy = new Date();
    let edad = hoy.getFullYear() - nacimiento.getFullYear();

    // Ajuste si aún no ha cumplido años este año
    const mesActual = hoy.getMonth();
    const mesNacimiento = nacimiento.getMonth();
    if (mesActual < mesNacimiento ||
      (mesActual === mesNacimiento && hoy.getDate() < nacimiento.getDate())) {
      edad--;
    }

    return edad;
  }
}
