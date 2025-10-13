import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError, switchMap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
import { PostService } from '../services/post.service';

@Injectable({
  providedIn: 'root'
})
export class PostAccessGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private postService: PostService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> {

    const postId = Number(route.paramMap.get('id'));

    // Vérifier l'authentification en appelant directement checkAuth()
    return this.authService.checkAuth().pipe(
      switchMap(isLoggedIn => {
        // Si non connecté, rediriger vers /auth
        if (!isLoggedIn) {
          return of(this.router.createUrlTree(['/auth']));
        }

        // Si connecté, récupérer le post pour obtenir le topicId
        return this.postService.getPostById(postId).pipe(
          switchMap(post => {
            // Récupérer les détails de l'utilisateur avec ses abonnements
            return this.userService.getCurrentUserDetails().pipe(
              map(userDetail => {
                // Vérifier si l'utilisateur est abonné au topic du post
                const isSubscribed = userDetail.subscribedTopics.some(
                  topic => topic.id === post.topicId
                );

                if (isSubscribed) {
                  return true;
                } else {
                  // Rediriger vers la liste des posts si pas abonné
                  return this.router.createUrlTree(['/posts']);
                }
              })
            );
          }),
          catchError((error) => {
            // Si erreur 401/403, l'utilisateur n'est pas authentifié
            if (error.status === 401 || error.status === 403) {
              return of(this.router.createUrlTree(['/auth']));
            }
            // En cas d'autre erreur (post inexistant, etc.), rediriger vers la liste
            return of(this.router.createUrlTree(['/posts']));
          })
        );
      })
    );
  }
}
