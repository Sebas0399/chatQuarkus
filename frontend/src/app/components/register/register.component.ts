import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';
  companyName = '';
  errorMessage = '';
  successMessage = '';

  constructor(private api: ApiService, private router: Router) {}

  onRegister() {
    this.api.registerUser({ name: this.name, email: this.email, password: this.password, companyName: this.companyName }).subscribe({
      next: (res: any) => {
        this.successMessage = 'Registration successful! Redirecting to login...';
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: (err) => {
        this.errorMessage = 'Registration failed. Check details.';
      }
    });
  }
}
