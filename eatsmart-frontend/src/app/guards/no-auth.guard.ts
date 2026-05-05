import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class NoAuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    // Si ya está autenticado, redirigir a home
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/home']);
      return false;
    }

    // No autenticado, permitir acceso a login/registro
    return true;
  }
}
