export interface Comment {
  id: number;
  content: string;
  postId: number;
  authorId: number;
  authorName: string;
}

export interface CommentsResponse {
  comments: Comment[];
}

export interface CommentCreate {
  content: string;
  postId: number;
}
