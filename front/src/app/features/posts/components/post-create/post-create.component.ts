import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PostService } from '../../../../services/post.service';
import { TopicService } from '../../../../services/topic.service';
import { Topic } from '../../../../interfaces/topic';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-post-create',
  templateUrl: './post-create.component.html',
  styleUrls: ['./post-create.component.scss']
})
export class PostCreateComponent implements OnInit {
  postForm!: FormGroup;
  topics$!: Observable<Topic[]>;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private topicService: TopicService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.postForm = this.fb.group({
      title: ['', [Validators.required]],
      topicId: ['', [Validators.required]],
      content: ['', [Validators.required]]
    });

    this.topics$ = this.topicService.topics$;
    this.topicService.loadAllTopics();
  }

  onSubmit(): void {
    if (this.postForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      const { title, content, topicId } = this.postForm.value;

      this.postService.createPost(title, content, topicId).subscribe({
        next: () => {
          this.postService.loadUserFeed();
          this.router.navigate(['/posts']);
        },
        error: (err) => {
          this.errorMessage = 'Erreur lors de la création de l\'article';
          this.isLoading = false;
          console.error(err);
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/posts']);
  }
}
