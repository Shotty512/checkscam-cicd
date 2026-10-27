import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CheckScamService } from '../../services/check-scam.service';
import { CheckScamRequestDTO } from '../../dtos/check-scam-request.dto';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { ChatBoxComponent } from '../chat-box/chat-box.component';

interface Message {
  sender: 'user' | 'bot';
  text: string;
  isUser: boolean;
}

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    HeaderComponent,
    FooterComponent,
    ChatBoxComponent
  ],
  templateUrl: './chatbot.component.html',
  styleUrl: './chatbot.component.scss'
})
export class ChatbotComponent {
  messages: Message[] = [];
  info: string = '';
  selectedType: number = 1;
  showChatbox: boolean = false;

  constructor(private checkScamService: CheckScamService) { }

  sendMessage(): void {
    const value = this.info.trim();
    if (!value) { return; }

    if (this.selectedType === 1 && !this.isPhoneNumber(value)) {
      alert('Số điện thoại phải bắt đầu bằng 0 và gồm 10 chữ số.');
      return;
    }
    if (this.selectedType === 2 && !this.isBankNumber(value)) {
      alert('Số tài khoản chỉ được chứa ký tự số.');
      return;
    }
    if (this.selectedType === 3 && !this.isUrl(value)) {
    alert('URL không hợp lệ (ví dụ hợp lệ: https://example.com hoặc example.vn).');
    return;
  }

    this.messages.push({ sender: 'user', text: value, isUser: true });
    this.checkScam(value, this.selectedType);
    this.info = '';
  }
  

  checkScam(query: string, type: number) {
    const requestBody: CheckScamRequestDTO = {
      info: query,
      type: type
    };

    this.checkScamService.checkScam(requestBody).subscribe({
      next: (response) => {
        let botResponseText = '';
        if (response?.code === 200 && response?.data) {
          botResponseText = response.data;
        } else {
          botResponseText = (response?.message ? ' Chi tiết: ' + response.message : 'Không nhận được phản hồi từ bot.');
        }
        this.messages.push({ sender: 'bot', text: botResponseText, isUser: false });
      },
      error: (error) => {
        const errorMessage = error?.error || 'Đã xảy ra lỗi khi tra cứu.';
        alert(errorMessage);
        this.messages.push({ sender: 'bot', text: 'Lỗi: ' + errorMessage, isUser: false });
      }
    });
  }

  private isPhoneNumber(value: string): boolean {
    return /^0\d{9}$/.test(value.trim());
  }

  private isBankNumber(value: string): boolean {
    return /^\d+$/.test(value.trim());
  }

  private isUrl(value: string): boolean {
  const urlRegex = /^(https?:\/\/)?([a-zA-Z0-9-]+\.)+[a-zA-Z]{2,}(\/\\S*)?$/i;
  return urlRegex.test(value.trim());
}

  onAiTuVanClicked() {
    this.showChatbox = true;
  }

  closeChatbox() {
    this.showChatbox = false;
  }
}