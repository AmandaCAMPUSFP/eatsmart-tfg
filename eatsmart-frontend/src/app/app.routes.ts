import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { PerfilComponent } from './components/perfil/perfil.component';
import { ComidasComponent } from './components/comidas/comidas.component';
import { EstadisticasComponent } from './components/estadisticas/estadisticas.component';
import { AlimentosComponent } from './components/alimentos/alimentos.component';
import { AuthGuard } from './guards/auth.guard';
import { NoAuthGuard } from './guards/no-auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [NoAuthGuard]
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [NoAuthGuard]
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'perfil',
    component: PerfilComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'comidas',
    component: ComidasComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'estadisticas',
    component: EstadisticasComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'alimentos',
    component: AlimentosComponent,
    canActivate: [AuthGuard]
  },
  {
    path: '**',
    redirectTo: 'home'
  }
];
