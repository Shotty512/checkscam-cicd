import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

import { ReportService } from '../../../services/report.service';               // ↔︎ chỉnh đường dẫn nếu khác
import { InformationTypeToStringPipe } from '../../shareds/pipes/information-type-to-string.pipe';
import { InformationType } from '../../shareds/enums/information-type.enum';

interface AttachmentDto {
  id: number;
  url: string;
}

interface ReportData {
  id: number;
  info: string;
  description?: string;
  status?: number;
  type: number | string;              // dữ liệu trả về có thể là chuỗi hoặc số
  idScamTypeAfterHandle?: number;
  emailAuthorReport: string;
  reason: string;
  infoDescription: string;
  dateReport?: string;
  attachmentDto: AttachmentDto[];
  [key: string]: any;
}

@Component({
  selector: 'app-detail-report',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    InformationTypeToStringPipe     // pipe standalone
  ],
  templateUrl: './detail-report.component.html',
  styleUrls: ['./detail-report.component.scss']
})
export class DetailReportComponent implements OnInit {

  report: ReportData = {
    id: 0,
    info: '',
    type: 0,
    emailAuthorReport: '',
    reason: '',
    infoDescription: '',
    attachmentDto: []
  };

  isLoading = true;
  errorMessage: string | null = null;
  selectedImageUrl: string | null = null;

  /** URL gốc lấy ảnh (thay bằng domain thực tế của bạn) */
  readonly imageBaseUrl = 'http://localhost:8080/api/v1/report/image/';

  constructor(
    private reportService: ReportService,
    private route: ActivatedRoute
  ) { }

  /* ---------------- life‑cycle ---------------- */
  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) {
      this.errorMessage = 'Không tìm thấy ID báo cáo trong URL.';
      this.isLoading = false;
      return;
    }

    const id = Number(idParam);
    if (isNaN(id)) {
      this.errorMessage = 'ID báo cáo không hợp lệ.';
      this.isLoading = false;
      return;
    }
    this.loadReportById(id);
  }

  /* ---------------- getter / helper ---------------- */
  /** Ép kiểu `report.type` → enum; nếu sai thì trả về PhoneNumber mặc định */
  get reportTypeEnum(): InformationType {
    const val = Number(this.report.type);
    return isNaN(val) ? InformationType.PhoneNumber : val as InformationType;
  }

  /** Lấy URL ảnh gốc từ attachment */
  getImageUrl(attachment: AttachmentDto): string {
    if (!attachment?.url) return 'assets/img/placeholder.png';

    const fileName = attachment.url.split('/').pop();
    if (!fileName) return 'assets/img/placeholder.png';

    return `${this.imageBaseUrl}${encodeURIComponent(fileName)}`;
  }

  /* ---------------- call API ---------------- */
  private loadReportById(id: number): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.reportService.getReportById(id).subscribe({
      next: ({ data }) => {
        if (!data) {
          this.errorMessage = 'Không nhận được dữ liệu báo cáo hợp lệ từ API.';
          return;
        }

        /** Bảo đảm attachmentDto luôn là mảng */
        data.attachmentDto = Array.isArray(data.attachmentDto) ? data.attachmentDto : [];
        /** Ép kiểu type sang number để pipe hoạt động đúng */
        data.type = Number(data.type);
        this.report = data;
      },
      error: err => {
        this.errorMessage =
          `Lỗi khi tải báo cáo: ${err.error?.message || err.message || 'Lỗi không xác định'}`;
      },
      complete: () => (this.isLoading = false)
    });
  }

  /* ---------------- lightbox ---------------- */
  openImage(url: string): void {
    this.selectedImageUrl = url;
    document.body.style.overflow = 'hidden';   // khoá cuộn nền
  }

  closeImage(): void {
    this.selectedImageUrl = null;
    document.body.style.overflow = '';
  }

  @HostListener('window:keydown.escape')
  onEscKey(): void {
    if (this.selectedImageUrl) this.closeImage();
  }

  /* ---------------- image error ---------------- */
  onImageError(ev: Event): void {
    (ev.target as HTMLImageElement).classList.add('image-error-placeholder');
  }
}
