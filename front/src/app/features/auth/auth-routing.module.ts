import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth-home/login/login.component';
import { RegisterComponent } from './components/auth-home/register/register.component';
import { AuthHomeComponent } from './components/auth-home/auth-home.component';
import { UnauthGuard } from '../../guards/unauth.guard';

const routes: Routes = [
  { path: '', component: AuthHomeComponent, canActivate: [UnauthGuard] },
  { path: 'login', component: LoginComponent, canActivate: [UnauthGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [UnauthGuard] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }