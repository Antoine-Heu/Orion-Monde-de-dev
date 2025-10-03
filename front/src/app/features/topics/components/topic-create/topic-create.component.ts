import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TopicService } from '../../../../services/topic.service';

@Component({
  selector: 'app-topic-create',
  templateUrl: './topic-create.component.html',
  styleUrls: ['./topic-create.component.scss']
})
export class TopicCreateComponent implements OnInit {
  topicForm!: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private topicService: TopicService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.topicForm = this.fb.group({
      title: ['', [Validators.required]],
      description: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.topicForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      const { title, description } = this.topicForm.value;

      this.topicService.createTopic(title, description).subscribe({
        next: () => {
          this.topicService.loadAllTopics();
          this.router.navigate(['/topics']);
        },
        error: (err) => {
          this.errorMessage = 'Erreur lors de la création du thème';
          this.isLoading = false;
          console.error(err);
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/topics']);
  }
}
