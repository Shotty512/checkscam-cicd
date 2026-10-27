/* view-news.component.ts */
import { CommonModule } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NewsService } from '../../../services/news.service';
import { HeaderComponent } from '../../header/header.component';
import { FooterComponent } from '../../footer/footer.component';  

interface AttachmentDto {
  id: number;
  url?: string | null;
}


@Component({
  selector: 'app-view-news',
  standalone: true,                           
  imports: [
    CommonModule,                              
    HeaderComponent,
    // FooterComponent                         
  ],
  templateUrl: './view-news.component.html',
  styleUrls: ['./view-news.component.scss']  
})
export class ViewNewsComponent implements OnInit {

  post: any = {};
  attachmentDto: AttachmentDto[] = [];
  selectedImageUrl: string | null = null;

  readonly imageBaseUrl = 'http://localhost:8080/api/v1/report/image/';

  constructor(
    private newsService: NewsService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadNewsById(id);
  }

  private loadNewsById(id: number): void {
    this.newsService.getNewsById(id).subscribe({
      next: (res) => {
         console.log('attachments from API', res.attachments);
        this.post = res;
        this.attachmentDto = res.attachments ?? [];   // đổi tên trường nếu cần
      },
      error: (err) => {
        alert(err?.error || 'Lỗi khi tải bài viết');
      }
    });
  }

  /* ---------- Helpers ---------- */

  getImageUrl({ url }: AttachmentDto): string {
    if (!url) return 'assets/img/placeholder.png';
    if (url.startsWith('http')) return url;

    const fileName = url.split('/').pop();
    return fileName ? `${this.imageBaseUrl}${encodeURIComponent(fileName)}` 
                    : 'assets/img/placeholder.png';
  }

  trackById(_: number, item: AttachmentDto): number {
    return item.id;
  }
  getFileName(att: AttachmentDto): string {
    return att.url?.split('/').pop() ?? 'Đính kèm';
  }
  /* ---------- Lightbox ---------- */
  openImage(url: string): void {
    this.selectedImageUrl = url;
    document.body.style.overflow = 'hidden';
  }

  closeImage(): void {
    this.selectedImageUrl = null;
    document.body.style.overflow = '';
  }

  @HostListener('window:keydown.escape')
  onEscKey(): void {
    if (this.selectedImageUrl) this.closeImage();
  }

  onImageError(ev: Event): void {
    (ev.target as HTMLImageElement).src = 'assets/img/placeholder.png';
  }
}