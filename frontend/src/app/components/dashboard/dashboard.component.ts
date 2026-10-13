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
  isLoading = true;

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.isLoading = true;
    let completed = 0;
    const checkDone = () => {
      completed++;
      if (completed >= 2) {
        this.isLoading = false;
      }
    };

    this.api.getContacts().subscribe({
      next: (res: any) => {
        this.contacts = res.data?.content || res.content || res.data || [];
        checkDone();
      },
      error: (err) => {
        console.error(err);
        checkDone();
      }
    });

    this.api.getAssistant().subscribe({
      next: (res: any) => {
        if (res.data) this.assistant = res.data;
        checkDone();
      },
      error: (err) => {
        console.error(err);
        checkDone();
      }
    });
  }
}
