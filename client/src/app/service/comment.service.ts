import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const COMMENT_API = 'http://localhost:8080/api/comment/';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) { }

  getCommentsForBrickset(bricksetId: number): Observable<any> {
    return this.http.get(COMMENT_API + bricksetId + '/all');
  }

  createComment(bricksetId: number, message: string): Observable<any> {
    return this.http.post(COMMENT_API + bricksetId + '/create', {message: message});
  }

  deleteComment(commentId: number): Observable<any> {
    return this.http.post(COMMENT_API + commentId + '/delete', null);
  }

}
