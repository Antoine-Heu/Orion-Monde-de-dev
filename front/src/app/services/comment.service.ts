import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment, CommentsResponse, CommentCreate } from '../interfaces/comment.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private readonly API_URL = environment.production
    ? 'http://localhost:8080/api/comments'
    : 'http://localhost:8080/api/comments';

  constructor(private http: HttpClient) {}

  getCommentsByPostId(postId: number): Observable<CommentsResponse> {
    return this.http.get<CommentsResponse>(`${this.API_URL}/post/${postId}`);
  }

  createComment(comment: CommentCreate): Observable<Comment> {
    return this.http.post<Comment>(this.API_URL, comment);
  }
}
