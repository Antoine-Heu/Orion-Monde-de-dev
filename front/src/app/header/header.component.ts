import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  isLoggedIn$: Observable<boolean>;
  isAuthHomePage = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.isLoggedIn$ = this.authService.isLoggedIn$;

    // Détecter si on est sur la page auth-home
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.isAuthHomePage = event.url === '/auth' || event.url === '/';
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth']);
  }

  navigateToArticles(): void {
    this.router.navigate(['/posts']);
  }

  navigateToTopics(): void {
    this.router.navigate(['/topics']);
  }

  navigateToProfile(): void {
    this.router.navigate(['/profile']);
  }
}
