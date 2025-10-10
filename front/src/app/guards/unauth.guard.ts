import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class UnauthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    if (this.authService.isLoggedIn()) {
      // Si l'utilisateur est déjà connecté, rediriger vers /posts
      this.router.navigate(['/posts']);
      return false;
    }

    // Si l'utilisateur n'est pas connecté, permettre l'accès aux pages d'auth
    return true;
  }
}
