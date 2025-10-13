import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AuthService } from '../../../../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../auth-form.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {
  registerForm!: FormGroup;
  isLoading = false;
  errorMessage = '';
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.passwordValidator()]]
    });
  }

  passwordValidator() {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) {
        return null;
      }

      const hasMinLength = value.length >= 8;
      const hasUpperCase = /[A-Z]/.test(value);
      const hasLowerCase = /[a-z]/.test(value);
      const hasNumber = /[0-9]/.test(value);
      const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(value);

      const passwordValid = hasMinLength && hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar;

      return !passwordValid ? {
        passwordStrength: {
          hasMinLength,
          hasUpperCase,
          hasLowerCase,
          hasNumber,
          hasSpecialChar
        }
      } : null;
    };
  }

  get password() {
    return this.registerForm.get('password');
  }

  hasMinLength(): boolean {
    const value = this.password?.value;
    return value ? value.length >= 8 : false;
  }

  hasUpperCase(): boolean {
    const value = this.password?.value;
    return value ? /[A-Z]/.test(value) : false;
  }

  hasLowerCase(): boolean {
    const value = this.password?.value;
    return value ? /[a-z]/.test(value) : false;
  }

  hasNumber(): boolean {
    const value = this.password?.value;
    return value ? /[0-9]/.test(value) : false;
  }

  hasSpecialChar(): boolean {
    const value = this.password?.value;
    return value ? /[!@#$%^&*(),.?":{}|<>]/.test(value) : false;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      this.authService.register(this.registerForm.value)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.router.navigate(['/posts']);
          },
          error: () => {
            this.errorMessage = 'Erreur lors de la création du compte';
            this.isLoading = false;
          }
        });
    }
  }

  goBack(): void {
    this.router.navigate(['/auth']);
  }
}