import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  isLoading = false;

  constructor(
    private api: ApiService,
    private tokenService: TokenService,
    private router: Router
  ) {}

  onLogin() {
    if (!this.username || !this.password) {
      this.errorMessage = 'Please enter both username and password';
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';
    this.api.login({ username: this.username, password: this.password }).subscribe({
      next: (res: any) => {
        this.isLoading = false;
        const token = res.token || res.accessToken || (res.data && res.data.token) || (typeof res === 'string' ? res : res.jwt);
        if (token) {
          this.tokenService.saveToken(typeof token === 'string' ? token : JSON.stringify(token));
        } else {
          this.tokenService.saveToken(JSON.stringify(res));
        }
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'Invalid credentials or server error';
      }
    });
  }
}
