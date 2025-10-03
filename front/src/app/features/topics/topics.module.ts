import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { TopicsRoutingModule } from './topics-routing.module';
import { TopicsListComponent } from './components/topics-list/topics-list.component';
import { TopicCreateComponent } from './components/topic-create/topic-create.component';


@NgModule({
  declarations: [
    TopicsListComponent,
    TopicCreateComponent
  ],
  imports: [
    CommonModule,
    TopicsRoutingModule,
    ReactiveFormsModule
  ]
})
export class TopicsModule { }
