import {NotificationDTO} from '../../api';

export interface NotificationState {
  unreadCount: number;
  notifications: NotificationDTO[];
  loading: boolean;
}

export const initialNotificationState: NotificationState = {
  unreadCount: 0,
  notifications: [],
  loading: false
};

