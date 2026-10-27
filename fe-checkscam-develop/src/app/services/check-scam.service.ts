import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpUtilService } from './http.util.service';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { CheckScamRequestDTO } from '../dtos/check-scam-request.dto';

@Injectable({
  providedIn: 'root'
})
export class CheckScamService {
  private apiCheckScam = `${environment.apiBaseUrl}/check-scam`;
  private apiChat = `${environment.apiBaseUrl}/check-scam/chat`;
  constructor(
    private http: HttpClient,
    private httpUtilService: HttpUtilService
  ) { }

  private getApiConfig() {
    return {
      headers: this.httpUtilService.createHeaders(),
    };
  }

  checkScam(checkScamRequestDTO: CheckScamRequestDTO): Observable<any> {
    return this.http.post(this.apiCheckScam, checkScamRequestDTO, this.getApiConfig());
  }

  chat(message: string): Observable<any> {
    return this.http.post(this.apiChat, message, this.getApiConfig());
  }
}
