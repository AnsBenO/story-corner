import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoaderComponent } from './components/common/loader/loader.component';
import { NavigationComponent } from './components/navigation/navigation.component';

import { NotFoundComponent } from './components/common/not-found/not-found.component';
import { NotificationComponent } from './components/common/notification/notification.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    LoaderComponent,
    NotFoundComponent,
    NavigationComponent,
    NotificationComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'book store';
}
