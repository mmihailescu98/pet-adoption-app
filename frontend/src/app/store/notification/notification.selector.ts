import {createFeatureSelector, createSelector} from '@ngrx/store';
import {NotificationState} from './notification.state';

export const selectNotificationState = createFeatureSelector<NotificationState>('notification');

export const selectUnreadCount = createSelector(selectNotificationState, (state) => state.unreadCount);

export const selectNotifications = createSelector(selectNotificationState, (state) => state.notifications);
