import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faFaceSadTear } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule],
  template: ` <div
    class="mt-20 flex flex-col items-center justify-start min-h-screen bg-gray-100"
  >
    <fa-icon [icon]="sadIcon" class="text-9xl mb-8 mt-10 text-cool-gray-dark" />
    <h1 class="text-4xl font-bold mb-4">404 - Page Not Found</h1>
    <p class="text-lg text-gray-600">
      The page you are looking for does not exist.
    </p>
    <a routerLink="/home" class="mt-6 text-blue-500 hover:underline"
      >Go back to Home</a
    >
  </div>`,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NotFoundComponent {
  sadIcon = faFaceSadTear;
}
