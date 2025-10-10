import { Topic } from './topic';

export interface User {
  id: number;
  username: string;
  email: string;
}

export interface UserDetail {
  id: number;
  username: string;
  email: string;
  createdAt: string;
  updatedAt: string;
  subscribedTopics: Topic[];
}

export interface UserUpdate {
  username: string;
  email: string;
  password?: string;
}
