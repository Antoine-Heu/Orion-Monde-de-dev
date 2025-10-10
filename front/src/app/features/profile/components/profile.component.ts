import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { UserService } from '../../../services/user.service';
import { SubscriptionService } from '../../../services/subscription.service';
import { UserDetail } from '../../../interfaces/user.interface';
import { Topic } from '../../../interfaces/topic';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  profileForm: FormGroup;
  userDetail: UserDetail | null = null;
  isLoading = false;
  isSaving = false;
  errorMessage = '';
  successMessage = '';
  unsubscribingTopicId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private subscriptionService: SubscriptionService,
    private dialog: MatDialog
  ) {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['']
    });
  }

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.isLoading = true;
    this.userService.getCurrentUserDetails().subscribe({
      next: (userDetail) => {
        this.userDetail = userDetail;
        this.profileForm.patchValue({
          username: userDetail.username,
          email: userDetail.email,
          password: 'Mot de passe'
        });
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading user profile:', error);
        this.errorMessage = 'Erreur lors du chargement du profil';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.profileForm.valid && !this.isSaving) {
      this.isSaving = true;
      this.errorMessage = '';
      this.successMessage = '';

      const updateData: any = {
        username: this.profileForm.value.username,
        email: this.profileForm.value.email
      };

      // Seulement inclure le password si différent de "Mot de passe"
      if (this.profileForm.value.password && this.profileForm.value.password !== 'Mot de passe') {
        updateData.password = this.profileForm.value.password;
      }

      this.userService.updateCurrentUser(updateData).subscribe({
        next: (user) => {
          this.successMessage = 'Profil mis à jour avec succès';
          this.isSaving = false;
          // Recharger le profil
          this.loadUserProfile();
        },
        error: (error) => {
          console.error('Error updating profile:', error);
          this.errorMessage = 'Erreur lors de la mise à jour du profil';
          this.isSaving = false;
        }
      });
    }
  }

  unsubscribeFromTopic(topic: Topic): void {
    const confirmed = window.confirm(`Êtes-vous sûr de vouloir vous désabonner de "${topic.title}" ?`);

    if (confirmed) {
      this.unsubscribingTopicId = topic.id;
      this.subscriptionService.unsubscribe(topic.id).subscribe({
        next: () => {
          // Retirer le topic de la liste
          if (this.userDetail) {
            this.userDetail.subscribedTopics = this.userDetail.subscribedTopics.filter(
              t => t.id !== topic.id
            );
          }
          this.unsubscribingTopicId = null;
        },
        error: (error) => {
          console.error('Error unsubscribing:', error);
          this.errorMessage = 'Erreur lors du désabonnement';
          this.unsubscribingTopicId = null;
        }
      });
    }
  }

  onPasswordFocus(): void {
    // Vider le champ password quand on clique dessus
    if (this.profileForm.value.password === 'Mot de passe') {
      this.profileForm.patchValue({ password: '' });
    }
  }
}
