  import { CommonModule } from '@angular/common';
  import { Component, EventEmitter, Output } from '@angular/core';
  import { FormsModule } from '@angular/forms';
  import { CheckScamService } from '../../services/check-scam.service';

  interface Message {
    text: string;
    isUser: boolean;
  }

  @Component({
    selector: 'app-chat-box',
    standalone: true,
    imports: [
      CommonModule,
      FormsModule
    ],
    templateUrl: './chat-box.component.html',
    styleUrl: './chat-box.component.scss'
  })
  export class ChatBoxComponent {
    @Output() close = new EventEmitter<void>();
    messages: Message[] = [];
    messageText: string = '';

    constructor(private checkScamService: CheckScamService) {}

    sendMessage() {
      if (this.messageText.trim()) {
        // Add user message
        this.messages.push({
          text: this.messageText,
          isUser: true
        });

        // Call API
        this.checkScamService.chat(this.messageText).subscribe({
          next: (response) => {
            debugger
            if (response.code === 0) {
              // Add AI response
              this.messages.push({
                text: response.message,
                isUser: false
              });
            } else {
              // Handle error response
              this.messages.push({
                text: 'Xin lỗi, tôi không thể xử lý yêu cầu của bạn lúc này.',
                isUser: false
              });
            }
          },
          error: (error) => {
            debugger
            // Handle API error
            this.messages.push({
              text: 'Đã xảy ra lỗi khi kết nối với server.',
              isUser: false
            });
            console.error('Chat API Error:', error);
          }
        });

        // Clear input
        this.messageText = '';
      }
    }
  }

