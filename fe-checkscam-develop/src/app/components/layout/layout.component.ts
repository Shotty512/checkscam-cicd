  import { Component, OnInit, HostListener } from '@angular/core';
  import { Router, RouterModule } from '@angular/router';
  import { UserService } from '../../services/user.service';
  import { CommonModule } from '@angular/common';

  @Component({
    selector: 'app-layout',
    imports: [
      RouterModule,
      CommonModule
    ],
    templateUrl: './layout.component.html',
    styleUrl: './layout.component.scss'
  })
  export class LayoutComponent implements OnInit {
    userRole: string[] = [];
    isAdmin: boolean = false;
    isCollaborator: boolean = false;
    isDropdownOpen: boolean = false;
    currentUser: any = {
      name: '',
      email: ''
    };

    constructor(
      private userService: UserService,
      private router: Router,
    ) { }

    ngOnInit() {
      const userData = localStorage.getItem('user');
      if (userData) {
        const user = JSON.parse(userData);
        this.userRole = user.role || [];
        this.isAdmin = this.userRole.includes('ROLE_ADMIN');
        this.isCollaborator = this.userRole.includes('ROLE_COLLAB');
        
        const token = localStorage.getItem('jwt_token');
        if (token) {
          const tokenData = JSON.parse(atob(token.split('.')[1]));
          this.currentUser = {
            name: tokenData.checkscam?.principal?.username || '',
            email: tokenData.sub || ''
          };
        }
      }
    }

    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen;
    }

    @HostListener('document:click', ['$event'])
    onDocumentClick(event: MouseEvent) {
      const target = event.target as HTMLElement;
      if (!target.closest('#userDropdown')) {
        this.isDropdownOpen = false;
      }
    }

    logout() {
      this.userService.logout().subscribe({
        next: () => {
          debugger
          localStorage.removeItem('jwt_token');
          localStorage.removeItem('user');
          this.router.navigate(['/login']);
        },
        error: (error) => {
          debugger
          alert(error?.error);
        }
      });
    }
  }
