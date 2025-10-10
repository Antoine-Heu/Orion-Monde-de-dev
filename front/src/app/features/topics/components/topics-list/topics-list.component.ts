import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TopicService } from '../../../../services/topic.service';
import { SubscriptionService } from '../../../../services/subscription.service';
import { Topic } from '../../../../interfaces/topic';
import { Observable, combineLatest, map } from 'rxjs';

export interface TopicWithSubscription extends Topic {
  isSubscribed: boolean;
}

@Component({
  selector: 'app-topics-list',
  templateUrl: './topics-list.component.html',
  styleUrls: ['./topics-list.component.scss']
})
export class TopicsListComponent implements OnInit {
  topicsWithSubscription$!: Observable<TopicWithSubscription[]>;
  isLoading: { [key: number]: boolean } = {};

  constructor(
    private topicService: TopicService,
    private subscriptionService: SubscriptionService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.topicService.loadAllTopics();
    this.subscriptionService.loadUserSubscriptions();

    this.topicsWithSubscription$ = combineLatest([
      this.topicService.topics$,
      this.subscriptionService.subscribedTopicIds$
    ]).pipe(
      map(([topics, subscribedIds]) =>
        topics.map(topic => ({
          ...topic,
          isSubscribed: subscribedIds.includes(topic.id)
        }))
      )
    );
  }

  subscribe(topic: TopicWithSubscription): void {
    // Ne rien faire si déjà abonné
    if (topic.isSubscribed) {
      return;
    }

    this.isLoading[topic.id] = true;

    this.subscriptionService.subscribe(topic.id).subscribe({
      next: () => {
        this.subscriptionService.loadUserSubscriptions();
        this.isLoading[topic.id] = false;
      },
      error: (err) => {
        console.error('Erreur lors de l\'abonnement', err);
        this.isLoading[topic.id] = false;
      }
    });
  }

  navigateToCreate(): void {
    this.router.navigate(['/topics/create']);
  }
}
