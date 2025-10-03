import { Post } from "./post";
import { Topic } from "./topic";

export interface LoginRequest {
  identifier: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface TokenResponse {
  token: string;
}

export interface User {
  id?: number;
  username: string;
  email: string;
  posts: Post[];
  subscribedTopics: Topic[];
}