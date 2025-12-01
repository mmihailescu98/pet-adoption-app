import {Component, OnDestroy, OnInit} from '@angular/core';
import {Menubar} from 'primeng/menubar';
import {MenuItem, PrimeTemplate} from 'primeng/api';
import {Router} from '@angular/router';
import {Observable, Subject, takeUntil, interval} from 'rxjs';
import {UserLoginModel} from '../../api';
import {selectLoggedInUser} from '../../store/auth/auth.selector';
import {Store} from '@ngrx/store';
import {Button} from 'primeng/button';
import {logout} from '../../store/auth/auth.actions';
import {selectUnreadCount, selectNotifications} from '../../store/notification/notification.selector';
import {loadNotifications, loadUnreadCount, markAllAsRead, markAsRead} from '../../store/notification/notification.actions';
import {NotificationDTO} from '../../api';
import {CommonModule} from '@angular/common';
import {Popover, PopoverModule} from 'primeng/popover';
import {startWith, map} from 'rxjs/operators';

@Component({
  selector: 'app-nav-bar',
  imports: [
    Menubar,
    PrimeTemplate,
    Button,
    CommonModule,
    PopoverModule
  ],
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css'
})
export class NavBar implements OnInit, OnDestroy {

  user$: Observable<UserLoginModel | null>;
  unreadCount$: Observable<number>;
  notifications$: Observable<NotificationDTO[]>;
  items: MenuItem[] | undefined;
  userId: number | null = null;
  private destroy$ = new Subject<void>();

  constructor(private router: Router, private store: Store) {
    this.user$ = this.store.select(selectLoggedInUser);
    this.unreadCount$ = this.store.select(selectUnreadCount);
    this.notifications$ = this.store.select(selectNotifications);
  }

  ngOnInit() {
    this.user$.pipe(takeUntil(this.destroy$)).subscribe(user => {
      this.userId = user?.id ?? null;
    });

    interval(30000).pipe(startWith(0), takeUntil(this.destroy$)).subscribe(() => {this.store.dispatch(loadUnreadCount());});

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
      },
      {
        label: 'Add pet',
        icon: 'pi pi-plus',
        command: () => this.router.navigate(['/add-pet'])
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

  showNotifications(event: Event, popover: Popover) {
    this.store.dispatch(loadNotifications());
    popover.toggle(event);
  }

  markAllAsRead() {
    this.store.dispatch(markAllAsRead());
  }

  markAsRead(notificationId: number | undefined) {
    if (notificationId) {
      this.store.dispatch(markAsRead({ id: notificationId }));
    }
  }

  navigateToPet(petId: number | null | undefined, notificationId: number | undefined) {
    if (notificationId) {
      this.markAsRead(notificationId);
    }
    if (petId) {
      this.router.navigate(['/pet-profile', petId]);
    }
  }
}
