import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Post } from '../../../../interfaces/post';
import { Comment } from '../../../../interfaces/comment.interface';
import { PostService } from '../../../../services/post.service';
import { CommentService } from '../../../../services/comment.service';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.scss']
})
export class PostDetailComponent implements OnInit, OnDestroy {
  post$!: Observable<Post>;
  comments: Comment[] = [];
  commentForm: FormGroup;
  isSubmitting = false;
  postId!: number;
  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    private commentService: CommentService,
    private fb: FormBuilder
  ) {
    this.commentForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  ngOnInit(): void {
    this.postId = Number(this.route.snapshot.paramMap.get('id'));
    this.post$ = this.postService.getPostById(this.postId);
    this.loadComments();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadComments(): void {
    this.commentService.getCommentsByPostId(this.postId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.comments = response.comments;
        },
        error: (error) => {
          console.error('Error loading comments:', error);
        }
      });
  }

  onSubmitComment(): void {
    if (this.commentForm.valid && !this.isSubmitting) {
      this.isSubmitting = true;
      const commentData = {
        content: this.commentForm.value.content,
        postId: this.postId
      };

      this.commentService.createComment(commentData)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (newComment) => {
            this.comments.push(newComment);
            this.commentForm.reset();
            this.isSubmitting = false;
          },
          error: (error) => {
            console.error('Error creating comment:', error);
            this.isSubmitting = false;
          }
        });
    }
  }

  goBack(): void {
    this.router.navigate(['/posts']);
  }
}
