import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth-home/login/login.component';
import { RegisterComponent } from './components/auth-home/register/register.component';
import { AuthHomeComponent } from './components/auth-home/auth-home.component';

const routes: Routes = [
  { path: '', component: AuthHomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }