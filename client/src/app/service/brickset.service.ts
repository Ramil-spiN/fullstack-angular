import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Brickset} from "../model/Brickset";
import {Observable} from "rxjs";

const BRICKSET_API = 'http://localhost:8080/api/brickset/';

@Injectable({
  providedIn: 'root'
})
export class BricksetService {

  constructor(private http: HttpClient) { }

  getAllBricksets(): Observable<any> {
    return this.http.get(BRICKSET_API + 'all');
  }

  getBricksetsForCurrentUser(): Observable<any> {
    return this.http.get(BRICKSET_API + 'user/bricksets');
  }

  createBrickset(brickset: Brickset): Observable<any> {
    return this.http.post(BRICKSET_API + 'create', brickset);
  }

  updateBrickset(bricksetId: number, brickset: Brickset): Observable<any> {
    return this.http.post(BRICKSET_API + bricksetId + '/updatebrickset', brickset);
  }

  deleteBrickset(bricksetId: number): Observable<any> {
    return this.http.post(BRICKSET_API + bricksetId + '/delete', null);
  }

  likeBrickset(bricksetId: number, username: string): Observable<any> {
    return this.http.post(BRICKSET_API + bricksetId + '/' + username + '/like', null);
  }
}
