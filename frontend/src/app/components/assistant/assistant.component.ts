import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-assistant',
  templateUrl: './assistant.component.html',
  styleUrls: ['./assistant.component.css']
})
export class AssistantComponent implements OnInit {
  assistant: any = {
    name: '',
    systemPrompt: '',
    iaProvider: 'GEMINI',
    model: '',
    url: '',
    token: '',
    tools: [],
    documents: []
  };
  testScript = '';
  testArgs = '{"name": "Developer"}';
  testResult: any = null;
  isLoading = true;

  // Tool Editor State
  editingToolIndex: number | null = null;
  editingToolName = '';
  editingToolDescription = '';
  editingToolScript = '';

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
    this.isLoading = true;
    this.api.getAssistant().subscribe({
      next: (res: any) => {
        const data = res.data || res;
        if (data) {
          this.assistant = {
            ...data,
            name: data.name || data.model || 'AI Assistant',
            systemPrompt: data.systemPrompt || data.prompt || '',
            tools: data.tools || [],
            documents: data.documents || []
          };
        }
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  saveAssistant() {
    const payload = {
      ...this.assistant,
      prompt: this.assistant.systemPrompt,
      toolsJson: JSON.stringify(this.assistant.tools || []),
      documentsJson: JSON.stringify(this.assistant.documents || [])
    };
    this.api.saveAssistant(payload).subscribe({
      next: () => alert('Assistant saved successfully!'),
      error: () => alert('Error saving assistant')
    });
  }

  editTool(tool: any, index: number) {
    this.editingToolIndex = index;
    this.editingToolName = tool.name;
    this.editingToolDescription = tool.description;
    this.editingToolScript = tool.script;
  }

  cancelEditTool() {
    this.editingToolIndex = null;
    this.editingToolName = '';
    this.editingToolDescription = '';
    this.editingToolScript = '';
  }

  updateTool() {
    if (this.editingToolIndex !== null && this.assistant.tools[this.editingToolIndex]) {
      this.assistant.tools[this.editingToolIndex] = {
        name: this.editingToolName,
        description: this.editingToolDescription,
        script: this.editingToolScript
      };
      this.cancelEditTool();
      this.saveAssistant();
    }
  }

  addTool() {
    if (!this.assistant.tools) {
      this.assistant.tools = [];
    }
    this.assistant.tools.push({
      name: 'nueva_herramienta',
      description: 'Descripción de la herramienta',
      script: 'async function execute(args) {\n  return { success: true };\n}'
    });
    this.editTool(this.assistant.tools[this.assistant.tools.length - 1], this.assistant.tools.length - 1);
  }

  deleteTool(index: number) {
    if (confirm('Are you sure you want to delete this tool?')) {
      this.assistant.tools.splice(index, 1);
      this.cancelEditTool();
      this.saveAssistant();
    }
  }

  runTool(tool: any) {
    this.testScript = tool.script;
    if (tool.name.includes('citas')) {
      this.testArgs = '{"specialty": "Medicina General"}';
    } else if (tool.name.includes('cotizar')) {
      this.testArgs = '{"service": "Consulta General"}';
    } else if (tool.name.includes('sede')) {
      this.testArgs = '{"location": "Sede Central"}';
    } else {
      this.testArgs = '{}';
    }
    this.testTool();
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

    let companyId = this.assistant.companyId || 1;
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
        this.loadAssistant();
      },
      error: (err) => {
        this.isIngesting = false;
        this.ingestMessage = 'Error ingesting document: ' + (err.error?.error || err.message);
      }
    });
  }
}
