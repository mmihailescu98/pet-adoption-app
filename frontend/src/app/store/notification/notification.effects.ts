import {inject, Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {of} from 'rxjs';
import {map, catchError, switchMap} from 'rxjs/operators';
import {NotificationControllerService} from '../../api';
import * as NotificationActions from './notification.actions';

@Injectable()
export class NotificationEffects {

  private actions$ = inject(Actions);
  private notificationService = inject(NotificationControllerService);

  loadUnreadCount$ = createEffect(() =>
    this.actions$.pipe(
      ofType(NotificationActions.loadUnreadCount),
      switchMap(() =>
        this.notificationService.getUnreadCount().pipe(
          map((response) => NotificationActions.loadUnreadCountSuccess({count: response['count'] || 0})),
          catchError(() => of(NotificationActions.loadUnreadCountFailure()))
        )
      )
    )
  );

  loadNotifications$ = createEffect(() =>
    this.actions$.pipe(
      ofType(NotificationActions.loadNotifications),
      switchMap(() =>
        this.notificationService.getUnreadNotifications().pipe(
          map((notifications) => NotificationActions.loadNotificationsSuccess({notifications})),
          catchError(() => of(NotificationActions.loadNotificationsFailure()))
        )
      )
    )
  );

  markAsRead$ = createEffect(() =>
    this.actions$.pipe(
      ofType(NotificationActions.markAsRead),
      switchMap(({id}) =>
        this.notificationService.markAsRead(id).pipe(
          map(() => NotificationActions.markAsReadSuccess()),
          catchError(() => of(NotificationActions.loadUnreadCountFailure()))
        )
      )
    )
  );

  markAllAsRead$ = createEffect(() =>
    this.actions$.pipe(
      ofType(NotificationActions.markAllAsRead),
      switchMap(() =>
        this.notificationService.markAllAsRead().pipe(
          map(() => NotificationActions.markAllAsReadSuccess()),
          catchError(() => of(NotificationActions.loadUnreadCountFailure()))
        )
      )
    )
  );

  reloadAfterMark$ = createEffect(() =>
    this.actions$.pipe(
      ofType(
        NotificationActions.markAsReadSuccess,
        NotificationActions.markAllAsReadSuccess
      ),
      switchMap(() => [
        NotificationActions.loadUnreadCount(),
        NotificationActions.loadNotifications()
      ])
    )
  );
}

