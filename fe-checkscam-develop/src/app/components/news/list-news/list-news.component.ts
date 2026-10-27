import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { NewsService } from '../../../services/news.service';
import { HeaderComponent } from '../../header/header.component';
import { FooterComponent } from '../../footer/footer.component';
import { ChatBoxComponent } from '../../chat-box/chat-box.component';



interface AttachmentDto {
  id: number;
  url?: string | null;
}

@Component({
  selector: 'app-view-news',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    HeaderComponent,
    FooterComponent,
    ChatBoxComponent,
  ],
  templateUrl: './list-news.component.html',
  styleUrls: ['./list-news.component.scss'],
})
export class ListNewsComponent implements OnInit {
  /* Tin tức */
  posts: any[] = [];
  pagedPosts: any[] = [];

  /* Phân trang */
  pageSize = 6;
  currentPage = 1;
  totalPosts = 0;
  totalPages = 0;
  pages: number[] = [];
  startIndex = 0;
  endIndex = 0;

  /* Tìm kiếm */
  searchTerm = '';

  /* Chat */
  showChatbox = false;

  /* URL ảnh */
  readonly imageBaseUrl = 'http://localhost:8080/api/v1/report/image/';

  constructor(
    private newsService: NewsService,
    private router: Router,
  ) {}

  /* ===== Lifecycle ===== */
  ngOnInit(): void {
    this.loadAllNews();
  }

  /* ===== API ===== */
  loadAllNews(): void {
    this.newsService.getListNews().subscribe({
      next: (res) => {
        this.posts = res;
        this.totalPosts = this.posts.length;
        this.calculateTotalPages();
        this.paginatePosts();
      },
      error: (err) => alert(err?.error || 'Lỗi khi tải danh sách tin tức'),
    });
  }

  /* ===== Ảnh ===== */
  getImageUrl({ url }: AttachmentDto): string {
    if (!url) return 'assets/img/placeholder.png';

    return url.startsWith('http')
      ? url
      : `${this.imageBaseUrl}${encodeURIComponent(url)}`;
  }

  onImageError(ev: Event): void {
    const img = ev.target as HTMLImageElement;
    if (!img.src.includes('placeholder.png')) {
      img.src = 'assets/img/placeholder.png';
    }
  }

  /* ===== Tìm kiếm & phân trang ===== */
  searchPosts(): void {
    this.currentPage = 1;
    this.paginatePosts();
  }

  paginatePosts(): void {
    const list = this.searchTerm
      ? this.posts.filter((p) =>
          p.name.toLowerCase().includes(this.searchTerm.toLowerCase()),
        )
      : this.posts;

    this.totalPosts = list.length;
    this.calculateTotalPages();

    this.startIndex = (this.currentPage - 1) * this.pageSize;
    this.endIndex = Math.min(
      this.startIndex + this.pageSize - 1,
      this.totalPosts - 1,
    );

    this.pagedPosts = list.slice(this.startIndex, this.endIndex + 1);
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.paginatePosts();
    }
  }

  calculateTotalPages(): void {
    this.totalPages = Math.ceil(this.totalPosts / this.pageSize);
    this.pages = Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }

  /* ===== Chat ===== */
  onAiTuVanClicked(): void {
    this.showChatbox = true;
  }

  closeChatbox(): void {
    this.showChatbox = false;
  }

  /* ===== trackBy ===== */
  trackById(_: number, item: any): number {
    return item.id;
  }
}