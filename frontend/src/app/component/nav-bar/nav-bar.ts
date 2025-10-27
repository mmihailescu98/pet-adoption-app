import {Component, OnDestroy, OnInit} from '@angular/core';
import {Menubar} from 'primeng/menubar';
import {MenuItem, PrimeTemplate} from 'primeng/api';
import {Router} from '@angular/router';
import {Observable, Subject, takeUntil} from 'rxjs';
import {UserLoginModel} from '../../api';
import {selectLoggedInUser} from '../../store/auth/auth.selector';
import {Store} from '@ngrx/store';
import {Button} from 'primeng/button';
import {logout} from '../../store/auth/auth.actions';

@Component({
  selector: 'app-nav-bar',
  imports: [
    Menubar,
    PrimeTemplate,
    Button
  ],
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css'
})
export class NavBar implements OnInit, OnDestroy {

  user$: Observable<UserLoginModel | null>;
  items: MenuItem[] | undefined;
  userId: number | null = null;
  private destroy$ = new Subject<void>();

  constructor(private router: Router, private store: Store) {
    this.user$ = this.store.select(selectLoggedInUser);
  }

  ngOnInit() {
    this.user$.pipe(takeUntil(this.destroy$)).subscribe(user => {
      this.userId = user?.id ?? null;
    });

    this.items = [
      {
        label: 'Profile',
        icon: 'pi pi-user',
        command: () => {
          if (this.userId) {
            this.router.navigate(['/user-profile', this.userId]);
          }
        }
      },
      {
        label: 'Home',
        icon: 'pi pi-home',
        command: () => {
          this.router.navigate(['/pet-list']);
        }
      },
      {
        label: 'Dashboard',
        icon: 'pi pi-chart-bar',
        command: () => this.router.navigate(['/dashboard'])
      }
    ]
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onLogout() {
    this.store.dispatch(logout());
    this.router.navigate(['/authentication']);
  }
}
