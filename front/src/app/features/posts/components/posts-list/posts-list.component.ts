import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../../../services/post.service';
import { SubscriptionService } from '../../../../services/subscription.service';
import { Post } from '../../../../interfaces/post';
import { Observable } from 'rxjs';
import { map, tap, switchMap, startWith } from 'rxjs/operators';

type SortOption = 'date-desc' | 'date-asc' | 'post-asc';

@Component({
  selector: 'app-posts-list',
  templateUrl: './posts-list.component.html',
  styleUrls: ['./posts-list.component.scss']
})
export class PostsListComponent implements OnInit {
  posts$!: Observable<Post[]>;
  currentSort: SortOption = 'date-desc';
  sortLabel = 'Date (plus récent)';

  constructor(
    private postService: PostService,
    private subscriptionService: SubscriptionService,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Charger les abonnements au démarrage
    this.subscriptionService.loadUserSubscriptions();

    // Pattern réactif : écouter les changements d'abonnements
    // et recharger automatiquement le feed
    this.posts$ = this.subscriptionService.subscribedTopicIds$.pipe(
      tap(() => this.postService.loadUserFeed()),
      switchMap(() => this.postService.posts$),
      map(posts => this.sortPosts([...posts], this.currentSort))
    );
  }

  applySorting(): void {
    // Recréer l'observable avec le nouveau tri
    this.posts$ = this.subscriptionService.subscribedTopicIds$.pipe(
      switchMap(() => this.postService.posts$),
      map(posts => this.sortPosts([...posts], this.currentSort))
    );
  }

  sortPosts(posts: Post[], sortOption: SortOption): Post[] {
    switch (sortOption) {
      case 'date-desc':
        return posts.sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
      case 'date-asc':
        return posts.sort((a, b) =>
          new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime()
        );
      case 'post-asc':
        return posts.sort((a, b) =>
          a.title.localeCompare(b.title)
        );
      default:
        return posts;
    }
  }

  setSortOption(option: SortOption, label: string): void {
    this.currentSort = option;
    this.sortLabel = label;
    this.applySorting();
  }

  navigateToCreate(): void {
    this.router.navigate(['/posts/create']);
  }

  navigateToDetail(postId: number): void {
    this.router.navigate(['/posts', postId]);
  }
}
