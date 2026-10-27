import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpUtilService } from './http.util.service';
import { Observable } from 'rxjs';
import { NewsDTO } from '../dtos/news.dto';

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  private apiNews = `${environment.apiBaseUrl}/news`;
  private apiUpdateNews = `${environment.apiBaseUrl}/news`;
  constructor(
    private http: HttpClient,
    private httpUtilService: HttpUtilService
  ) { }

  private getApiConfig() {
    return {
      headers: this.httpUtilService.createHeaders(),
    };
  }

  getListNews(): Observable<any> {
    return this.http.get(`${environment.apiBaseUrl}/news`);
  }

  getNewsById(id: number): Observable<any> {
    return this.http.get(`${environment.apiBaseUrl}/news/${id}`);
  }

  createNews(newsDTO: NewsDTO): Observable<any> {
    return this.http.post(this.apiNews, newsDTO, this.getApiConfig());
  }

  deleteNewsById(id: number): Observable<any> {
    return this.http.delete(`${environment.apiBaseUrl}/news/${id}`);
  }

  uploadFiles(newsId: number | string, formData: FormData): Observable<any> {
    return this.http.post<any>(`${environment.apiBaseUrl}/news/uploads/${newsId}`, formData);
  }

  updateNews(id: number,newsDTO: NewsDTO): Observable<any> {
    return this.http.put(`${environment.apiBaseUrl}/news/${id}`, newsDTO);
  }
}