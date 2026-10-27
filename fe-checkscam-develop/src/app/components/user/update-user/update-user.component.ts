  import { CommonModule } from '@angular/common';
  import { Component, Inject, OnInit } from '@angular/core';
  import { FormsModule } from '@angular/forms';
  import { MatButtonModule } from '@angular/material/button';
  import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
  import { MatFormFieldModule } from '@angular/material/form-field';
  import { MatInputModule } from '@angular/material/input';
  import { UserDTO } from '../../../dtos/user.dto';
  import { UserService } from '../../../services/user.service';

  interface UserData {
    id: number;
    name: string;
    email: string;
    password: string;
  }

  @Component({
    selector: 'app-update-user',
    imports: [
      CommonModule,
      FormsModule,
      MatDialogModule,
      MatFormFieldModule,
      MatInputModule,
      MatButtonModule,
    ],
    templateUrl: './update-user.component.html',
    styleUrl: './update-user.component.scss'
  })
  export class UpdateUserComponent implements OnInit {
    name = '';
    email = '';
    password = '';
    userId!: number;
    showPassword = false;

    constructor(
      private userService: UserService,
      private dialogRef: MatDialogRef<UpdateUserComponent>,
      @Inject(MAT_DIALOG_DATA) public data: UserData
    ) { }

    ngOnInit() {
      if (this.data) {
        console.log('User data:', this.data); // Debug log
        this.userId = this.data.id;
        this.name = this.data.name;
        this.email = this.data.email;
        this.password = this.data.password || '';
      }
    }

    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    }

    updateUser() {
      if (!this.userId) {
        alert('Không tìm thấy thông tin người dùng');
        return;
      }

      const userDTO: UserDTO = {
        name: this.name,
        email: this.email,
        password: this.password
      };

      this.userService.updateUser(this.userId, userDTO).subscribe({
        next: () => {
          debugger
          this.dialogRef.close(true);
        },
        error: (error) => {
          debugger
          alert(error.error);
        }
      });
    }

    closeDialog(): void {
      this.dialogRef.close();
    }
  }
