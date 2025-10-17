import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Post } from '../interfaces/post';
import { environment } from '../../environments/environment';

export interface PostsResponse {
  posts: Post[];
}

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private readonly API_URL = environment.production ? 'http://localhost:8080/api/posts' : 'http://localhost:8080/api/posts';

  private postsSubject = new BehaviorSubject<Post[]>([]);
  public posts$ = this.postsSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadUserFeed(): void {
    this.http.get<PostsResponse>(`${this.API_URL}/feed`).subscribe({
      next: (response) => {
        this.postsSubject.next(response.posts || []);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des articles', err);
        this.postsSubject.next([]);
      }
    });
  }

  getPostById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.API_URL}/${id}`);
  }

  createPost(title: string, content: string, topicId: number): Observable<Post> {
    return this.http.post<Post>(`${this.API_URL}`, { title, content, topicId });
  }
}
