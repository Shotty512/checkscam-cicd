
  import { CommonModule } from '@angular/common';
  import { Component } from '@angular/core';
  import { FormsModule } from '@angular/forms';
  import { Router } from '@angular/router';
  import { UserDTO } from '../../../dtos/user.dto';
  import { UserService } from '../../../services/user.service';
  import { MatButtonModule } from '@angular/material/button';
  import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
  import { MatFormFieldModule } from '@angular/material/form-field';
  import { MatInputModule } from '@angular/material/input';

  @Component({
    selector: 'app-create-user',
    imports: [
      CommonModule,
      FormsModule,
      MatDialogModule,
      MatFormFieldModule,
      MatInputModule,
      MatButtonModule,
    ],
    templateUrl: './create-user.component.html',
    styleUrl: './create-user.component.scss'
  })
  export class CreateUserComponent {

    name = '';
    email = '';
    password = '';
    
    constructor(
      private userService: UserService,
      private router: Router,
      private dialogRef: MatDialogRef<CreateUserComponent>,
    ){}
    
    ngOnInit() {
    
    }
    
    createUser(){
      const userDTO: UserDTO = {
        name: this.name,
        email: this.email,
        password: this.password,
      };
      this.userService.createUser(userDTO).subscribe({
        next: () => {
          debugger
          this.dialogRef.close(true);
          // this.router.navigate(['/users']);
        },
        error: (error) => {
          debugger
          alert(error.error);
        }
      })
    }

    closeDialog(): void {
      this.dialogRef.close();
    }
  }

