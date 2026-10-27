
  import { Component } from '@angular/core';
  import { UserService } from '../../services/user.service';
  import { CommonModule } from '@angular/common';
  import { Router, RouterModule } from '@angular/router';
  import { UserDTO } from '../../dtos/user.dto';
  import { FormsModule } from '@angular/forms';
  import { MatDialog } from '@angular/material/dialog';
  import { CreateUserComponent } from './create-user/create-user.component';
  import { UpdateUserComponent } from './update-user/update-user.component';

  @Component({
    selector: 'app-user',
    imports: [
      CommonModule,
      RouterModule,
      FormsModule
    ],
    templateUrl: './user.component.html',
    styleUrl: './user.component.scss'
  })
  export class UserComponent {
    name : string = '';
    email: string  = '';
    password: string = '';
    isPopupVisible: any;

    accounts: any[] = [];
    selectedUser: any = null;
    isEditMode = false;

    constructor(
      private userService: UserService,
      private router: Router,
      private dialog: MatDialog
    ) {

    }

    ngOnInit() {
      this.loadListUsers();
    }

    loadListUsers() {
      this.userService.getListUsers().subscribe({
        next: (response) => {
          this.accounts = response.map((user: any) => ({
            ...user,
            password: user.password || '' // Đảm bảo có trường password
          }));
        },
        error: (error) => {
          alert(error.error);
        }
      })
    }

    deleteUser(id: number) {
      if (confirm("Bạn có chắc muốn xóa người dùng này?")) {
        this.userService.deleteUser(id).subscribe({
          next: () => {
            this.loadListUsers();
          },
          error: (error) => {
            alert(error.error);
          }
        });
      }
    }

    openDialogCreateUser(): void {
      const dialogRef = this.dialog.open(CreateUserComponent, {
        width: '400px',
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result === true) {
          this.loadListUsers();
        }
      });
    }

    openDialogUpdateUser(user: any): void {
      const dialogRef = this.dialog.open(UpdateUserComponent, {
        width: '400px',
        data: {
          id: user.id,
          name: user.name,
          email: user.email,
          password: user.password || '' // Truyền password vào dialog
        }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result === true) {
          this.loadListUsers();
        }
      });
    }


    unlockAccount(userId: number) {
      // Implement unlock account functionality
      console.log('Unlock account:', userId);
    }
  }
