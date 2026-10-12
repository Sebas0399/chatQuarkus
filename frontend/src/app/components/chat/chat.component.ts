import { Component, OnInit, ElementRef, ViewChild, AfterViewChecked, OnDestroy } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { WebsocketService } from '../../services/websocket.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, AfterViewChecked, OnDestroy {
  @ViewChild('scrollContainer') private scrollContainer!: ElementRef;

  contacts: any[] = [];
  selectedContact: any = null;
  messages: any[] = [];
  newMessageText = '';
  isSending = false;
  private wsSub: Subscription | null = null;
  private shouldScroll = true;

  constructor(
    private api: ApiService,
    private wsService: WebsocketService
  ) {}

  ngOnInit() {
    this.loadContacts();
    this.wsService.connect();
    this.wsSub = this.wsService.onMessage().subscribe((msg: any) => {
      if (msg && this.selectedContact && (msg.contactId === this.selectedContact.id || msg.contact?.id === this.selectedContact.id)) {
        const exists = this.messages.some(m => m.id === msg.id && m.id != null);
        if (!exists) {
          this.messages.push(msg);
          this.shouldScroll = true;
        }
      }
    });
  }

  ngOnDestroy() {
    if (this.wsSub) {
      this.wsSub.unsubscribe();
    }
  }

  ngAfterViewChecked() {
    if (this.shouldScroll) {
      this.scrollToBottom();
    }
  }

  onScroll(event: any) {
    const el = event.target;
    const atBottom = el.scrollHeight - el.scrollTop - el.clientHeight < 50;
    this.shouldScroll = atBottom;
  }

  scrollToBottom(): void {
    try {
      if (this.scrollContainer) {
        this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
      }
    } catch(err) { }
  }

  loadContacts() {
    this.api.getContacts().subscribe({
      next: (res: any) => {
        this.contacts = res.data?.content || res.content || res.data || [];
      },
      error: (err) => console.error(err)
    });
  }

  selectContact(contact: any) {
    if (this.selectedContact?.id === contact.id) return;
    this.selectedContact = contact;
    this.shouldScroll = true;
    this.loadMessages(contact.id);
  }

  loadMessages(contactId: number) {
    this.api.getMessages(contactId).subscribe({
      next: (res: any) => {
        this.messages = res.data || res || [];
        this.shouldScroll = true;
      },
      error: (err) => console.error(err)
    });
  }

  sendMessage() {
    if (!this.newMessageText.trim() || !this.selectedContact || this.isSending) return;

    this.isSending = true;
    const payload = {
      contactId: this.selectedContact.id,
      text: this.newMessageText,
      isFromCompany: true
    };

    this.api.sendMessage(payload).subscribe({
      next: (res: any) => {
        const sentMsg = res.data || res;
        if (sentMsg && sentMsg.text) {
          const exists = this.messages.some(m => m.id === sentMsg.id && m.id != null);
          if (!exists) {
            this.messages.push(sentMsg);
          }
        } else {
          this.messages.push({
            text: this.newMessageText,
            isFromCompany: true,
            createdAt: new Date().toISOString()
          });
        }
        this.newMessageText = '';
        this.isSending = false;
        this.shouldScroll = true;
      },
      error: (err) => {
        console.error(err);
        this.isSending = false;
      }
    });
  }
}
