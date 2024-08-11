import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostListener,
  WritableSignal,
  inject,
  signal,
} from '@angular/core';
import { RouterModule } from '@angular/router';
import { CartStore } from '../../store/cart.store';
import { CartComponent } from '../cart/cart.component';
import { faBars, faXmark } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [CommonModule, RouterModule, CartComponent, FontAwesomeModule],
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.scss',

  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NavigationComponent {
  cartStore = inject(CartStore);
  showCart: WritableSignal<boolean> = signal(false);
  isMenuOpen: WritableSignal<boolean> = signal(false);
  menuIcon = faBars;
  xMarkIcon = faXmark;

  toggleMenu() {
    this.isMenuOpen.set(!this.isMenuOpen());
  }

  openCart() {
    this.showCart.set(true);
  }

  closeCart() {
    this.showCart.set(false);
  }

  @HostListener('document:click', ['$event'])
  @HostListener('document:touchstart', ['$event'])
  handleOutsideClick(event: Event) {
    const menuDiv = document.querySelector('#navDiv');
    const closeButton = document.querySelector('#closeMenu');

    if (
      menuDiv &&
      !menuDiv.contains(event.target as Node) &&
      !closeButton?.contains(event.target as Node)
    ) {
      this.isMenuOpen.set(false);
    }
  }

  @HostListener('document:scroll')
  handleScrolling() {
    this.isMenuOpen.set(false);
  }
}
