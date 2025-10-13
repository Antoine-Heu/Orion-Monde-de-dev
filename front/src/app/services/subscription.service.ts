import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Topic } from '../interfaces/topic';

export interface TopicsResponse {
  topics: Topic[];
}

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  private readonly API_URL = environment.production ? 'http://localhost:8080/api/topics' : 'http://localhost:8080/api/topics';

  private subscribedTopicIdsSubject = new BehaviorSubject<number[]>([]);
  public subscribedTopicIds$ = this.subscribedTopicIdsSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadUserSubscriptions(): void {
    this.http.get<TopicsResponse>(`${this.API_URL}/subscriptions`).subscribe({
      next: (response) => {
        const topicIds = response.topics.map(topic => topic.id);
        this.subscribedTopicIdsSubject.next(topicIds);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des abonnements', err);
        this.subscribedTopicIdsSubject.next([]);
      }
    });
  }

  subscribe(topicId: number): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/${topicId}/subscribe`, {}).pipe(
      tap(() => {
        // Recharger les abonnements pour mettre à jour subscribedTopicIds$
        this.loadUserSubscriptions();
      })
    );
  }

  unsubscribe(topicId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${topicId}/subscribe`).pipe(
      tap(() => {
        // Recharger les abonnements pour mettre à jour subscribedTopicIds$
        this.loadUserSubscriptions();
      })
    );
  }

  isSubscribed(topicId: number): boolean {
    return this.subscribedTopicIdsSubject.value.includes(topicId);
  }
}
