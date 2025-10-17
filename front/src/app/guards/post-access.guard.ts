import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
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

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> {
    if (!this.authService.isLoggedIn()) {
      return of(this.router.createUrlTree(['/auth']));
    }

    const postId = Number(route.paramMap.get('id'));

    return this.postService.getPostById(postId).pipe(
      switchMap(post => {
        return this.userService.getCurrentUserDetails().pipe(
          map(userDetail => {
            const isSubscribed = userDetail.subscribedTopics.some(
              topic => topic.id === post.topicId
            );

            if (isSubscribed) {
              return true;
            } else {
              return this.router.createUrlTree(['/posts']);
            }
          })
        );
      }),
      catchError((error) => {
        if (error.status === 401 || error.status === 403) {
          return of(this.router.createUrlTree(['/auth']));
        }
        return of(this.router.createUrlTree(['/posts']));
      })
    );
  }
}
