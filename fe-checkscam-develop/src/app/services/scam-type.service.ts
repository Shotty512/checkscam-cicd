import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

export interface ScamTypeDto {
    id: number;
    name: string;
    code: string;
}

@Injectable({ providedIn: 'root' })
export class ScamTypeService {
    private readonly BASE = 'http://localhost:8080/api/v1/scam-types';

    constructor(private http: HttpClient) { }

    /** GET /batch */
    getAll(): Observable<ScamTypeDto[]> {
        return this.http.get<any>(`${this.BASE}/batch`)
            .pipe(map(res => res.data as ScamTypeDto[]));
    }

    /** POST /  */
    create(body: { name: string; code: string }): Observable<ScamTypeDto> {
        return this.http.post<any>(this.BASE, body)
            .pipe(map(res => res.data as ScamTypeDto));
    }
}
