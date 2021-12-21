import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const AUTH_API = 'http://localhost:8080/api/auth/';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  public login(user): Observable<any> {
    return this.http.post(AUTH_API + 'signin', {
      username: user.username,
      password: user.password
    });
  }

  public register(user): Observable<any> {
    return this.http.post(AUTH_API + 'signup', {
      username: user.username,
      firstname: user.firstname,
      lastname: user.lastname,
      email: user.email,
      password: user.password,
      confirmPassword: user.confirmPassword
    })
  }
}
