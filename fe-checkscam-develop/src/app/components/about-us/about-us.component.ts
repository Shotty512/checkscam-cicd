import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { RouterLink } from '@angular/router'; 
import { RouterModule } from '@angular/router'; 
import { HeaderComponent } from "../header/header.component";

@Component({
  selector: 'app-about-us',
  standalone: true,
  imports: [ RouterModule], 
  templateUrl: './about-us.component.html',
  styleUrls: ['./about-us.component.scss']
})
export class AboutUsComponent implements OnInit {
  constructor(private location: Location) { }

  ngOnInit(): void {
    // Bạn có thể thêm logic khởi tạo dữ liệu ở đây nếu cần
  }

  goBack(): void {
    this.location.back();
  }

}