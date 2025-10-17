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
    const currentUrl$ = this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd),
      map((event: NavigationEnd) => event.urlAfterRedirects),
      startWith(this.router.url)
    );

    this.showAuthHeader$ = currentUrl$.pipe(
      map(url => url === '/auth/login' || url === '/auth/register')
    );

    this.showAppHeader$ = currentUrl$.pipe(
      map(url => url !== '/auth' && url !== '/' && url !== '/auth/login' && url !== '/auth/register')
    );
  }
}
