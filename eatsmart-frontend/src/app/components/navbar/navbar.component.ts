import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LanguageService } from '../../services/language.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  usuarioEmail: string | null = null;
  menuAbierto = false;
  idiomaActual: 'es' | 'en' = 'es';

  constructor(
    private authService: AuthService,
    private languageService: LanguageService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const usuario = this.authService.getUsuario();
    if (usuario) {
      this.usuarioEmail = usuario.email;
    }

    this.idiomaActual = this.languageService.getCurrentLanguage();
  }

  toggleMenu(): void {
    this.menuAbierto = !this.menuAbierto;
  }

  cerrarMenu(): void {
    this.menuAbierto = false;
  }

  cambiarIdioma(idioma: 'es' | 'en'): void {
    this.languageService.setLanguage(idioma);
    this.idiomaActual = idioma;
  }

  /**
   * Obtiene la traducción para una clave del i18n.
   * El parámetro es de tipo string para evitar imports dinámicos
   * que rompen la compilación cuando la ruta del módulo cambia.
   */
  obtenerTexto(clave: string): string {
    return this.languageService.getTranslation(clave as any);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }
}
