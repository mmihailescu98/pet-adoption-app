import { Component } from '@angular/core';
import {Menubar} from 'primeng/menubar';
import {MenuItem} from 'primeng/api';
import {Router} from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  imports: [
    Menubar
  ],
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css'
})
export class NavBar {

  constructor(private router: Router) {
  }

  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
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
}
