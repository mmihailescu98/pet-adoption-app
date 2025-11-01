import {createAction, props} from '@ngrx/store';
import {NotificationDTO} from '../../api';

export const loadUnreadCount = createAction('[Notification] Load Unread Count');
export const loadUnreadCountSuccess = createAction('[Notification] Load Unread Count Success', props<{ count: number }>());
export const loadUnreadCountFailure = createAction('[Notification] Load Unread Count Failure');

export const loadNotifications = createAction('[Notification] Load Notifications');
export const loadNotificationsSuccess = createAction('[Notification] Load Notifications Success', props<{ notifications: NotificationDTO[] }>());
export const loadNotificationsFailure = createAction('[Notification] Load Notifications Failure');

export const markAsRead = createAction('[Notification] Mark As Read', props<{ id: number }>());
export const markAsReadSuccess = createAction('[Notification] Mark As Read Success');

export const markAllAsRead = createAction('[Notification] Mark All As Read');
export const markAllAsReadSuccess = createAction('[Notification] Mark All As Read Success');
