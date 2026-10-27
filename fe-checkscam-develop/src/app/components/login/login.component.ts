    import { Component, ViewChild } from '@angular/core';
    import { FormsModule, NgForm } from '@angular/forms';
    import { Router, RouterModule } from '@angular/router';
    import { UserService } from '../../services/user.service';
    import { LoginDTO } from '../../dtos/login.dto';
    import { TokenService } from '../../services/token.service';

    @Component({
        selector: 'app-login',
        standalone: true,
        imports: [FormsModule, RouterModule],
        templateUrl: './login.component.html',
        styleUrls: ['./login.component.scss']
    })
    export class LoginComponent {
        @ViewChild('loginForm') loginForm!: NgForm;
        username = 'admin@gmail.com';
        password = '123456';

        constructor(
            private router: Router,
            private userService: UserService,
            private tokenService: TokenService,
        ) { }

        login() {
            this.tokenService.clearToken();
            const loginDTO: LoginDTO = {
                username: this.username,
                password: this.password
            };
            this.userService.login(loginDTO).subscribe({
                next: (response) => {
                    debugger
                    const token = response.access_token;
                    this.tokenService.saveToken(token);
                    this.userService.saveUserData(response);
                    this.router.navigate(['/dashboard']);
                },
                error: (error) => {
                    debugger
                    alert(error?.error);
                }
            });
        }

        forgotPassword(event: any) { // Thêm tham số "event" ở đây
            event.preventDefault(); // Ngăn chặn hành vi mặc định của thẻ <a>
            alert('Vui lòng liên hệ quản trị viên để được cấp lại mật khẩu.');
        }
    }
