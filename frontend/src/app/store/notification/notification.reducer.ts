import {createReducer, on} from '@ngrx/store';
import {initialNotificationState} from './notification.state';
import * as NotificationActions from './notification.actions';

export const notificationReducer = createReducer(
  initialNotificationState,

  on(NotificationActions.loadUnreadCount, (state) => ({
    ...state,
    loading: true
  })),

  on(NotificationActions.loadUnreadCountSuccess, (state, {count}) => ({
    ...state,
    unreadCount: count,
    loading: false
  })),

  on(NotificationActions.loadUnreadCountFailure, (state) => ({
    ...state,
    loading: false
  })),

  on(NotificationActions.loadNotifications, (state) => ({
    ...state,
    loading: true
  })),

  on(NotificationActions.loadNotificationsSuccess, (state, {notifications}) => ({
    ...state,
    notifications,
    loading: false
  })),

  on(NotificationActions.loadNotificationsFailure, (state) => ({
    ...state,
    loading: false
  })),


  on(NotificationActions.markAllAsReadSuccess, (state) => ({
    ...state,
    unreadCount: 0,
    notifications: state.notifications.map(n => ({...n, isRead: true}))
  }))
);

