import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = '/api';

  constructor(private http: HttpClient) {}

  login(credentials: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/login`, credentials);
  }

  registerUser(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/register`, data);
  }

  registerCompany(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/companies/save`, data);
  }

  getContacts(filter: any = {}): Observable<any> {
    return this.http.post(`${this.baseUrl}/contacts/byCompany`, { filter, page: 0, size: 50 });
  }

  getMessages(contactId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/messages/contact/${contactId}`);
  }

  sendMessage(message: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/messages/send`, message);
  }

  getAssistant(): Observable<any> {
    return this.http.get(`${this.baseUrl}/assistants`);
  }

  saveAssistant(assistant: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/assistants`, assistant);
  }

  testAssistantTool(payload: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/assistants/tools/test`, payload);
  }

  ingestRagDocument(payload: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/rag/ingest`, payload);
  }
}
