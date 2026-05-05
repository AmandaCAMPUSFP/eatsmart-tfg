import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface AuthResponse {
  mensaje: string;
  token: string;
  idUsuario: number;
  email: string;
  exitoso: boolean;
  refreshToken?: string;
}

export interface Usuario {
  idUsuario: number;
  email: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  private usuarioSubject = new BehaviorSubject<Usuario | null>(null);
  private tokenSubject = new BehaviorSubject<string | null>(null);

  // true si estamos en el navegador, false si estamos en SSR (servidor)
  private isBrowser: boolean;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
    this.cargarDatosGuardados();
  }

  /**
   * Carga token y usuario del localStorage al iniciar.
   * Solo se ejecuta en el navegador (no en SSR).
   */
  private cargarDatosGuardados(): void {
    if (!this.isBrowser) return;

    const token = localStorage.getItem('token');
    const usuario = localStorage.getItem('usuario');

    if (token) {
      this.tokenSubject.next(token);
    }

    if (usuario) {
      try {
        this.usuarioSubject.next(JSON.parse(usuario));
      } catch (e) {
        console.error('Error cargando usuario guardado', e);
      }
    }
  }

  /**
   * Registrar nuevo usuario
   */
  registrar(email: string, contrasena: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/registro`, {
      email,
      contrasena
    }).pipe(
      tap(response => {
        if (response.exitoso) {
          this.guardarDatos(response);
        }
      })
    );
  }

  /**
   * Login de usuario
   */
  login(email: string, contrasena: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, {
      email,
      contrasena
    }).pipe(
      tap(response => {
        if (response.exitoso) {
          this.guardarDatos(response);
        }
      })
    );
  }

  /**
   * Refrescar token
   */
  refreshToken(refreshToken: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/refresh?refreshToken=${refreshToken}`,
      {}
    ).pipe(
      tap(response => {
        if (response.exitoso && this.isBrowser) {
          localStorage.setItem('token', response.token);
          this.tokenSubject.next(response.token);
        }
      })
    );
  }

  /**
   * Guardar token y usuario en localStorage (solo en navegador)
   */
  private guardarDatos(response: AuthResponse): void {
    if (!this.isBrowser) return;

    localStorage.setItem('token', response.token);
    if (response.refreshToken) {
      localStorage.setItem('refreshToken', response.refreshToken);
    }

    const usuario: Usuario = {
      idUsuario: response.idUsuario,
      email: response.email
    };

    localStorage.setItem('usuario', JSON.stringify(usuario));

    this.tokenSubject.next(response.token);
    this.usuarioSubject.next(usuario);
  }

  /**
   * Verificar si el usuario está autenticado
   */
  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token;
  }

  /**
   * Obtener token actual
   */
  getToken(): string | null {
    if (!this.isBrowser) return null;
    return localStorage.getItem('token');
  }

  /**
   * Obtener refresh token
   */
  getRefreshToken(): string | null {
    if (!this.isBrowser) return null;
    return localStorage.getItem('refreshToken');
  }

  /**
   * Obtener usuario actual
   */
  getUsuario(): Usuario | null {
    if (!this.isBrowser) return null;

    const usuario = localStorage.getItem('usuario');
    if (usuario) {
      try {
        return JSON.parse(usuario);
      } catch (e) {
        return null;
      }
    }
    return null;
  }

  /**
   * Observable del usuario actual
   */
  getUsuario$(): Observable<Usuario | null> {
    return this.usuarioSubject.asObservable();
  }

  /**
   * Logout - limpiar datos
   */
  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('usuario');
    }

    this.tokenSubject.next(null);
    this.usuarioSubject.next(null);
  }

  /**
   * Validar token en backend (para debug)
   */
  validarToken(token: string): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.apiUrl}/validar?token=${token}`);
  }
}
