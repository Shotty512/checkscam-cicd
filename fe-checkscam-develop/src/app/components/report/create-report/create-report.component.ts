  import { CommonModule } from '@angular/common';
  import { Component, OnInit } from '@angular/core';
  import { FormsModule } from '@angular/forms';
  import { Router, RouterModule } from '@angular/router';
  import { RecaptchaModule } from 'ng-recaptcha';
  import { HttpErrorResponse } from '@angular/common/http';
  import { ReportService } from '../../../services/report.service';

  // Import Interface ReportDTO (Bạn cần đảm bảo file này có trường pageToReport)
  import { ReportDTO } from '../../../dtos/report.dto';

  import { HeaderComponent } from '../../header/header.component';
  import { FooterComponent } from "../../footer/footer.component";
  import { ChatBoxComponent } from "../../chat-box/chat-box.component";

  @Component({
    selector: 'app-create-report',
    standalone: true,
    imports: [CommonModule, RouterModule, FormsModule, RecaptchaModule, FooterComponent, HeaderComponent, ChatBoxComponent],
    templateUrl: './create-report.component.html',
    styleUrls: ['./create-report.component.scss']
  })
  export class CreateReportComponent implements OnInit {
    info = '';
    type = 1;
    emailAuthorReport = '';
    reason = '';
    infoDescription = '';
    accountHolderName = '';
    bankName = '';
    showChatbox: boolean = false;

    // >> Bổ sung: Thêm thuộc tính pageToReport
    pageToReport: string = '';

    // >> Bổ sung: Thêm thuộc tính agreeTerms cho checkbox
    agreeTerms: boolean = false;


    selectedFiles: File[] = [];
    captchaToken = '';

    constructor(private reportService: ReportService, private router: Router) { }

    ngOnInit(): void {
      // Bạn có thể cần fetch dữ liệu ban đầu hoặc setup gì đó ở đây nếu cần
    }

    onFileSelect(event: Event): void {
      const input = event.target as HTMLInputElement;
      this.selectedFiles = input.files ? Array.from(input.files) : [];
    }

    handleCaptchaResponse(token: string | null): void {
      this.captchaToken = token ?? '';
    }

    createReport(): void {
      if (!this.captchaToken) {
        alert('Vui lòng hoàn thành CAPTCHA');
        return;
      }
      // >> Cập nhật: Kiểm tra thêm các trường mới và checkbox đồng ý
      // if (this.isFormInvalid()) {
      //   alert('Vui lòng điền đầy đủ các trường bắt buộc và đồng ý với điều khoản.');
      //   return;
      // }

      const info2 = this.type === 2 ? this.accountHolderName : undefined;
      const info3 = this.type === 2 ? this.bankName : undefined;

      // >> Cập nhật: Thêm pageToReport vào reportDTO
      const reportDTO: ReportDTO = {
        info: this.info,
        pageToReport: this.pageToReport, // << Thêm trường này
        emailAuthorReport: this.emailAuthorReport,
        type: this.type,
        reason: this.reason,
        infoDescription: this.infoDescription,
        captchaToken: this.captchaToken,
        info2,
        info3
        // Bạn có thể không cần gửi agreeTerms lên backend, tùy yêu cầu
        // agreeTerms: this.agreeTerms
      };

      this.reportService.createReport(reportDTO).subscribe({
        next: (res: any) => {
          alert('Gửi thông tin báo cáo thành công!');
          const reportId = res.data?.id ?? res.id;
          if (!reportId) {
            alert('Không nhận được ID báo cáo từ server.');
            return;
          }
          if (this.selectedFiles.length) {
            this.uploadFiles(reportId);
          } else {
            // >> Cập nhật: Có thể chuyển hướng về trang khác thay vì chatbot nếu muốn
            this.router.navigate(['/bao-cao-thanh-cong']); // Ví dụ trang báo cáo thành công
          }
        },
        error: (err: HttpErrorResponse) => {
          console.error('Lỗi khi tạo báo cáo:', err);
          alert(`Lỗi: ${err.error?.message || err.message}`);
          // >> Cập nhật: Có thể chuyển hướng về trang khác khi lỗi
          // this.router.navigate(['/bao-cao-loi']); // Ví dụ trang báo cáo lỗi
        }
      });
    }

    private uploadFiles(reportId: number | string): void {
      const formData = new FormData();
      this.selectedFiles.forEach(f => formData.append('files', f, f.name));

      this.reportService.uploadFiles(reportId, formData).subscribe({
        next: () => {
          alert('Tải lên tệp thành công!');
          // >> Cập nhật: Chuyển hướng sau khi upload file
          this.router.navigate(['/bao-cao-thanh-cong']); // Ví dụ trang báo cáo thành công
        },
        error: (err: HttpErrorResponse) => {
          console.error('Lỗi khi tải lên tệp:', err);
          alert(`Lỗi: ${err.error?.message || err.message}`);
          // >> Cập nhật: Chuyển hướng khi lỗi upload
          this.router.navigate(['/bao-cao-loi']); // Ví dụ trang báo cáo lỗi
        }
      });
    }

    private isFormInvalid(): boolean {
      // >> Cập nhật: Thêm kiểm tra cho pageToReport và agreeTerms
      const basicInvalid = !this.info || !this.reason || !this.pageToReport || !this.emailAuthorReport || !this.infoDescription;
      const extraInvalidForType2 = this.type === 2 && (!this.accountHolderName || !this.bankName);
      const captchaInvalid = !this.captchaToken;
      const termsNotAgreed = !this.agreeTerms; // Kiểm tra checkbox đồng ý

      return basicInvalid || extraInvalidForType2 || captchaInvalid || termsNotAgreed;
    }
    // >> Bổ sung: Phương thức reset form sau khi gửi thành công/thất bại (tùy logic)
    resetForm(): void {
      this.info = '';
      this.type = 1;
      this.emailAuthorReport = '';
      this.reason = '';
      this.infoDescription = '';
      this.accountHolderName = '';
      this.bankName = '';
      this.pageToReport = ''; // Reset trường mới
      this.agreeTerms = false; // Reset checkbox
      this.selectedFiles = [];
      this.captchaToken = ''; // Reset captcha token
      // Nếu dùng re-captcha, cần reset lại widget captcha nếu có API hỗ trợ
    }

    onAiTuVanClicked() {
      this.showChatbox = true;
    }


    closeChatbox() {
      this.showChatbox = false;
    }
  }