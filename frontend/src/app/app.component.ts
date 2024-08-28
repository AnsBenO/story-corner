import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { LoaderComponent } from './components/common/loader/loader.component';
import { NavigationComponent } from './components/common/navigation/navigation.component';

import { NotFoundComponent } from './components/common/not-found/not-found.component';
import { NotificationComponent } from './components/common/notification/notification.component';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterModule,
    LoaderComponent,
    NotFoundComponent,
    NavigationComponent,
    NotificationComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  authService = inject(AuthService);
  destroy = inject(DestroyRef);
  title = 'book store';
  router = inject(Router);
}
