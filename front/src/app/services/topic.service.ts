import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Topic } from '../interfaces/topic';
import { environment } from '../../environments/environment';

export interface TopicsResponse {
  topics: Topic[];
}

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  private readonly API_URL = environment.production ? 'http://localhost:8080/api/topics' : 'http://localhost:8080/api/topics';

  private topicsSubject = new BehaviorSubject<Topic[]>([]);
  public topics$ = this.topicsSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadAllTopics(): void {
    this.http.get<TopicsResponse>(`${this.API_URL}`).subscribe({
      next: (response) => {
        this.topicsSubject.next(response.topics);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des topics', err);
        this.topicsSubject.next([]);
      }
    });
  }

  getTopicById(id: number): Observable<Topic> {
    return this.http.get<Topic>(`${this.API_URL}/${id}`);
  }

  createTopic(title: string, description: string): Observable<Topic> {
    return this.http.post<Topic>(this.API_URL, { title, description });
  }
}
