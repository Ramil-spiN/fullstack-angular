import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

const IMAGE_API = 'http://localhost:8080/api/image/';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) { }

  getProfileImage(): Observable<any> {
    return this.http.get(IMAGE_API + 'profile-image');
  }

  uploadProfileImage(file: File): Observable<any> {
    const uploadData = new FormData();
    uploadData.append('file', file);
    return this.http.post(IMAGE_API + 'upload', uploadData);
  }

  getBricksetImage(bricksetId: number): Observable<any> {
    return this.http.get(IMAGE_API + bricksetId + '/image');
  }

  getBricksetUserImage(bricksetId: number): Observable<any> {
    return this.http.get(IMAGE_API + bricksetId + '/user-image');
  }

  uploadBricksetImage(bricksetId: number, file: File): Observable<any> {
    const uploadData = new FormData();
    uploadData.append('file', file);
    return this.http.post(IMAGE_API + bricksetId + '/upload', uploadData);
  }
}
