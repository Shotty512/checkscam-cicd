
  import { HttpClient } from '@angular/common/http';
  import { Injectable } from '@angular/core';
  import { HttpUtilService } from './http.util.service';
  import { Observable } from 'rxjs';
  import { LoginDTO } from '../dtos/login.dto';
  import { environment } from '../environments/environment';
  import { UserDTO } from '../dtos/user.dto';

  @Injectable({
    providedIn: 'root'
  })
  export class UserService {
    private apiLogin = `${environment.apiBaseUrl}/auth/login`;
    private apiCreateUser = `${environment.apiBaseUrl}/users`;
    private apiUpdateUser = `${environment.apiBaseUrl}/users`;
    private apiLogout = `${environment.apiBaseUrl}/auth/logout`;
    constructor(
      private http: HttpClient,
      private httpUtilService: HttpUtilService
    ) { }

    private getApiConfig() {
      return {
        headers: this.httpUtilService.createHeaders(),
      };
    }

    login(loginDTO: LoginDTO): Observable<any> {    
      return this.http.post(this.apiLogin, loginDTO, this.getApiConfig());
    } 
    
    logout(): Observable<void> {
      return this.http.post<void>(this.apiLogout, this.getApiConfig());
    }

    getListUsers(): Observable<any> {
      return this.http.get(`${environment.apiBaseUrl}/users`);
    }

    createUser(userDTO: UserDTO): Observable<any> {
      return this.http.post(this.apiCreateUser, userDTO, this.getApiConfig());
    }

    deleteUser(id: number): Observable<any> {
      return this.http.delete(`${environment.apiBaseUrl}/users/${id}`);
    }

    updateUser(id: number, userDTO: UserDTO): Observable<any> {
      return this.http.put(`${this.apiUpdateUser}/${id}`, userDTO, this.getApiConfig());
    }

    saveUserData(token: any): void {
    try {
      // Nếu truyền vào là object chứa access_token thì lấy chuỗi token
      const rawToken = typeof token === 'string' ? token : token?.access_token;

      if (!rawToken || typeof rawToken !== 'string') {
        console.error("Token không hợp lệ:", token);
        return;
      }

      const payload = JSON.parse(atob(rawToken.split('.')[1]));

      const userData = {
        role: payload?.roles || [],
        token: rawToken
      };

      localStorage.setItem('user', JSON.stringify(userData));
    } catch (error) {
      console.error("Lỗi khi phân tích token:", error);
    }
  }

  }
