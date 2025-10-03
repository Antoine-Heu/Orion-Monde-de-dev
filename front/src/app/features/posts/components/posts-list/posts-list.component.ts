import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../../../services/post.service';
import { Post } from '../../../../interfaces/post';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-posts-list',
  templateUrl: './posts-list.component.html',
  styleUrls: ['./posts-list.component.scss']
})
export class PostsListComponent implements OnInit {
  posts$!: Observable<Post[]>;

  constructor(
    private postService: PostService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.posts$ = this.postService.posts$;
    this.postService.loadUserFeed();
  }

  navigateToCreate(): void {
    this.router.navigate(['/posts/create']);
  }

  navigateToDetail(postId: number): void {
    this.router.navigate(['/posts', postId]);
  }

}
