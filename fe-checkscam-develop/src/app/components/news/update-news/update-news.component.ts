import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NewsDTO } from '../../../dtos/news.dto';
import { NewsService } from '../../../services/news.service';

@Component({
  selector: 'app-update-news',
  standalone: true,
  imports: [ CommonModule, FormsModule ],
  templateUrl: './update-news.component.html',
  styleUrl: './update-news.component.scss'
})
export class UpdateNewsComponent implements OnInit {
  newId!: number;
  name = '';
  shortDescription = '';
  content = '';

  constructor(
    private newsService: NewsService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.newId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.newId) {
      this.loadNewsById();
    }
  }

  private loadNewsById(): void {
    this.newsService.getNewsById(this.newId).subscribe({
      next: (response) => {
        this.name = response.name;
        this.shortDescription = response.shortDescription;
        this.content = response.content;
      },
      error: (error) => {
        alert(error?.error);
      }
    });
  }

  updateNews(): void {
    const newsDTO: NewsDTO = {
      name: this.name,
      shortDescription: this.shortDescription,
      content: this.content
    };
    this.newsService.updateNews(this.newId, newsDTO).subscribe({
      next: () => {
        debugger
        this.router.navigate(['/news'])
      },
      error: (error) => {
        debugger
        alert(error.error);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/news']);
  }
}