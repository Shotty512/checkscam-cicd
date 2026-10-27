import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Thường cần CommonModule cho các structural directives (*ngIf, *ngFor) nếu component không standalone
import { RouterLink } from '@angular/router'; // Nếu có link trong footer

@Component({
  selector: 'app-footer', 
  imports: [
    CommonModule,
  ],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss' 
})
export class FooterComponent {
  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}