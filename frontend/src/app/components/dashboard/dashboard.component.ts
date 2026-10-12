import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  contacts: any[] = [];
  assistant: any = { name: '', prompt: '', type: 'GEMINI' };

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.loadContacts();
    this.loadAssistant();
  }

  loadContacts() {
    this.api.getContacts().subscribe({
      next: (res: any) => {
        this.contacts = res.data?.content || res.content || res.data || [];
      },
      error: (err) => console.error(err)
    });
  }

  loadAssistant() {
    this.api.getAssistant().subscribe({
      next: (res: any) => {
        if (res.data) this.assistant = res.data;
      },
      error: (err) => console.error(err)
    });
  }
}
