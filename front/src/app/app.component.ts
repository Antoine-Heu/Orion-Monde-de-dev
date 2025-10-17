import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front';
  showAuthHeader$: Observable<boolean>;
  showAppHeader$: Observable<boolean>;

  constructor(private router: Router) {
    // Observable qui émet à chaque changement de route
    // IMPORTANT : utiliser urlAfterRedirects au lieu de url pour capturer l'URL finale après redirections
    const currentUrl$ = this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd),
      map((event: NavigationEnd) => {
        console.log('NavigationEnd - url:', event.url, '- urlAfterRedirects:', event.urlAfterRedirects);
        return event.urlAfterRedirects;
      }),
      startWith(this.router.url)
    );

    // Observable pour auth-header (logo seul sur /auth/login et /auth/register)
    this.showAuthHeader$ = currentUrl$.pipe(
      map(url => {
        const show = url === '/auth/login' || url === '/auth/register';
        console.log('showAuthHeader:', show, 'for URL:', url);
        return show;
      })
    );

    // Observable pour app-header (header complet sur toutes les autres routes sauf /auth)
    this.showAppHeader$ = currentUrl$.pipe(
      map(url => {
        const show = url !== '/auth' && url !== '/' && url !== '/auth/login' && url !== '/auth/register';
        console.log('showAppHeader:', show, 'for URL:', url);
        return show;
      })
    );
  }
}
