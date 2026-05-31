import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ComidasService } from '../../services/comidas.service';
import { PerfilService } from '../../services/perfil.service';
import { AuthService } from '../../services/auth.service';
import { ResumenDiario } from '../../models/comida.model';
import { PerfilNutricional } from '../../models/perfil-nutricional.model';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-estadisticas',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './estadisticas.component.html',
  styleUrls: ['./estadisticas.component.scss']
})
export class EstadisticasComponent implements OnInit {
  resumenHoy: ResumenDiario | null = null;
  resumenSemanal: any = null;
  perfil: PerfilNutricional | null = null;

  loading = false;
  fechaSeleccionada = new Date().toISOString().split('T')[0];

  constructor(
    private comidasService: ComidasService,
    private perfilService: PerfilService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {
    const usuario = this.authService.getUsuario();
    if (usuario) {
      this.loading = true;

      // Cargar perfil
      this.perfilService.obtenerPorId(usuario.idUsuario).subscribe({
        next: (perfil) => {
          this.perfil = perfil;
          this.cargarResumenDia();
        },
        error: (err) => {
          console.error('Error cargando perfil:', err);
          this.loading = false;
        }
      });
    }
  }

  cargarResumenDia(): void {
    const usuario = this.authService.getUsuario();
    if (usuario && this.perfil) {
      this.comidasService.obtenerPorUsuarioYFecha(
        usuario.idUsuario,
        this.fechaSeleccionada
      ).subscribe({
        next: (comidas) => {
          const objetivoKcal = this.perfilService.calcularObjetivoCalorico(this.perfil!);
          this.resumenHoy = this.comidasService.calcularResumenDiario(comidas, objetivoKcal);
          this.loading = false;
        },
        error: (err) => {
          console.error('Error cargando comidas:', err);
          this.loading = false;
        }
      });
    }
  }

  cambiarFecha(): void {
    this.cargarResumenDia();
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

  obtenerMensajeKcal(): string {
    const estado = this.obtenerEstadoKcal();
    const restante = this.resumenHoy ? this.resumenHoy.objetivoKcal - this.resumenHoy.totalKcal : 0;

    if (estado === 'bajo') {
      return `Te faltan ${Math.abs(restante)} kcal`;
    } else if (estado === 'alto') {
      return `Exceso de ${Math.abs(restante)} kcal`;
    } else {
      return `Te faltan ${restante} kcal`;
    }
  }

  obtenerDistribucionMacros(): any[] {
    if (!this.resumenHoy) return [];

    const caloriasPorMacro = {
      proteinas: this.resumenHoy.totalProteinas * 4,
      carbohidratos: this.resumenHoy.totalCarbohidratos * 4,
      grasas: this.resumenHoy.totalGrasas * 9
    };

    const totalCalorias = caloriasPorMacro.proteinas + caloriasPorMacro.carbohidratos + caloriasPorMacro.grasas;

    return [
      {
        nombre: 'Proteínas',
        gramos: this.resumenHoy.totalProteinas,
        porcentaje: totalCalorias > 0 ? (caloriasPorMacro.proteinas / totalCalorias) * 100 : 0,
        color: '#FF6B6B'
      },
      {
        nombre: 'Carbohidratos',
        gramos: this.resumenHoy.totalCarbohidratos,
        porcentaje: totalCalorias > 0 ? (caloriasPorMacro.carbohidratos / totalCalorias) * 100 : 0,
        color: '#4ECDC4'
      },
      {
        nombre: 'Grasas',
        gramos: this.resumenHoy.totalGrasas,
        porcentaje: totalCalorias > 0 ? (caloriasPorMacro.grasas / totalCalorias) * 100 : 0,
        color: '#FFE66D'
      }
    ];
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
