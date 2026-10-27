import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpUtilService } from './http.util.service';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { ReportDTO } from '../dtos/report.dto';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiCreateReport = `${environment.apiBaseUrl}/report`;
  constructor(
    private http: HttpClient,
    private httpUtilService: HttpUtilService
  ) { }

  private getApiConfig() {
    return {
      headers: this.httpUtilService.createHeaders(),
    };
  }

  createReport(reportDTO: ReportDTO): Observable<any> {
    return this.http.post(this.apiCreateReport, reportDTO, this.getApiConfig());
  }

  getListReports(): Observable<any> {
    return this.http.get(`${environment.apiBaseUrl}/report/all`);
  }

  getReportById(id: number): Observable<any> {
    return this.http.get(`${environment.apiBaseUrl}/report/${id}`);
  }

  uploadFiles(reportId: number | string, formData: FormData): Observable<any> {
    return this.http.post<any>(`${environment.apiBaseUrl}/report/uploads/${reportId}`, formData);
  }

  handleReport(body: {
    idReport: number;
    status: 2 | 3;
    idScamTypeAfterHandle: number | null;
  }): Observable<any> {
    return this.http.post(`${environment.apiBaseUrl}/report/handle`, body);
  }

  getMonthlyStats(year: number): Observable<any[]> {
    const params = new HttpParams().set('year', year.toString());
    return this.http.get<any[]>(`${environment.apiBaseUrl}/report/monthly`, { params });
  }

  getYearlyStats(): Observable<{ year: number; count: number }[]> {
  return this.http.get<{ year: number; count: number }[]>(
    `${environment.apiBaseUrl}/report/yearly`
  );
}
}
