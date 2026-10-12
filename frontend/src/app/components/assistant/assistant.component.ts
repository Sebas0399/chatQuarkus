import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-assistant',
  templateUrl: './assistant.component.html',
  styleUrls: ['./assistant.component.css']
})
export class AssistantComponent implements OnInit {
  assistant: any = { name: '', prompt: '', type: 'GEMINI' };
  testScript = '';
  testArgs = '{"name": "Developer"}';
  testResult: any = null;

  // RAG Ingestion properties
  ragTitle = '';
  ragText = '';
  isIngesting = false;
  ingestMessage = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.loadAssistant();
  }

  loadAssistant() {
    this.api.getAssistant().subscribe({
      next: (res: any) => {
        if (res.data) this.assistant = res.data;
      },
      error: (err) => console.error(err)
    });
  }

  saveAssistant() {
    this.api.saveAssistant(this.assistant).subscribe({
      next: () => alert('Assistant saved successfully!'),
      error: () => alert('Error saving assistant')
    });
  }

  testTool() {
    let argsJson = {};
    try {
      argsJson = JSON.parse(this.testArgs);
    } catch (e) {
      alert('Invalid JSON in arguments');
      return;
    }
    this.api.testAssistantTool({ script: this.testScript, argsJson }).subscribe({
      next: (res: any) => {
        this.testResult = res.data || res;
      },
      error: (err) => {
        this.testResult = err.error || err;
      }
    });
  }

  ingestDocument() {
    if (!this.ragTitle.trim() || !this.ragText.trim() || this.isIngesting) return;

    this.isIngesting = true;
    this.ingestMessage = '';

    // Decodificar el token JWT almacenado para obtener el companyId del tenant actual
    let companyId = 1;
    try {
      const token = localStorage.getItem('auth_token');
      if (token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        if (payload.companyId) {
          companyId = payload.companyId;
        }
      }
    } catch (e) {
      console.error('Error decoding token for companyId', e);
    }

    const payload = {
      title: this.ragTitle,
      text: this.ragText,
      companyId: companyId
    };

    this.api.ingestRagDocument(payload).subscribe({
      next: () => {
        this.isIngesting = false;
        this.ingestMessage = 'Document successfully vectorized and indexed in Elasticsearch!';
        this.ragTitle = '';
        this.ragText = '';
      },
      error: (err) => {
        this.isIngesting = false;
        this.ingestMessage = 'Error ingesting document: ' + (err.error?.error || err.message);
      }
    });
  }
}
