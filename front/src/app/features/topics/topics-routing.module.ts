import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TopicsListComponent } from './components/topics-list/topics-list.component';
import { TopicCreateComponent } from './components/topic-create/topic-create.component';

const routes: Routes = [
  { path: '', component: TopicsListComponent },
  { path: 'create', component: TopicCreateComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TopicsRoutingModule { }
