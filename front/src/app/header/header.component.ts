import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Subject } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  // Propriétés pour la gestion de l'affichage
  showHeader = false;
  showNav = false;
  showHamburger = false;
  isMenuOpen = false;

  private destroy$ = new Subject<void>();

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Écouter les changements d'authentification
    this.authService.isLoggedIn$
      .pipe(takeUntil(this.destroy$))
      .subscribe(isLoggedIn => {
        this.showNav = isLoggedIn;
        this.showHamburger = isLoggedIn;
        this.cdr.detectChanges();
      });

    // Initialiser l'état du header au chargement
    this.updateHeaderState(this.router.url);

    // Écouter les changements de navigation
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd),
      takeUntil(this.destroy$)
    ).subscribe((event: NavigationEnd) => {
      this.updateHeaderState(event.url);
      this.isMenuOpen = false;
      this.cdr.detectChanges();
    });
  }

  private updateHeaderState(url: string): void {
    const isAuthHomePage = url === '/auth' || url === '/';
    this.showHeader = !isAuthHomePage;
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(): void {
    this.isMenuOpen = false;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  logout(): void {
    this.authService.logout()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.router.navigate(['/auth']);
        },
        error: (error) => {
          console.error('Logout error:', error);
          // Rediriger quand même en cas d'erreur
          this.router.navigate(['/auth']);
        }
      });
  }
}
