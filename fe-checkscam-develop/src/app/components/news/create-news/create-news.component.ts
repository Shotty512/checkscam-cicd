import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

import { NewsService } from '../../../services/news.service';
import { NewsDTO } from '../../../dtos/news.dto';

@Component({
  selector: 'app-create-news',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-news.component.html',
  styleUrls: ['./create-news.component.scss']
})
export class CreateNewsComponent {
  name = '';
  shortDescription = '';
  content = '';
  selectedFiles: File[] = [];

  constructor(
    private newsService: NewsService,
    private router: Router
  ) {}

  /** khi chọn file */
  onFileSelect(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFiles = input.files ? Array.from(input.files) : [];
  }

  /** tạo tin & upload ảnh */
  createNews(): void {
    const dto: NewsDTO = {
      name: this.name,
      shortDescription: this.shortDescription,
      content: this.content
    };

    this.newsService.createNews(dto).subscribe({
      next: res => {
        const id = res?.data?.id ?? res?.id;
        if (!id) {
          alert('Không nhận được ID bài viết từ server');
          return;
        }

        if (this.selectedFiles.length) {
          this.uploadFiles(id);
        } else {
          alert('Tạo bài viết thành công!');
          this.router.navigate(['/news']);
        }
      },
      error: (err: HttpErrorResponse) =>
        alert(`Lỗi khi tạo bài viết: ${err.error?.message || err.message}`)
    });
  }

  private uploadFiles(newsId: number | string): void {
    const formData = new FormData();
    this.selectedFiles.forEach(f => formData.append('files', f, f.name));

    this.newsService.uploadFiles(newsId, formData).subscribe({
      next: () => {
        alert('Tạo bài viết & tải ảnh thành công!');
        this.router.navigate(['/news']);
      },
      error: (err: HttpErrorResponse) =>
        alert(`Lỗi upload: ${err.error?.message || err.message}`)
    });
  }
}