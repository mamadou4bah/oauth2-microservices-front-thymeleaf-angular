import { Component, OnInit } from '@angular/core';
import { SecurityService } from './services/security/security.service';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'customer-front-angular-app';
   public profile? : KeycloakProfile;
   public username?: any;

  constructor(public keycloakService: KeycloakService) {
    // Constructor logic can go here
  }

  ngOnInit() {
    if(this.keycloakService.isLoggedIn()) {
      this.keycloakService.loadUserProfile().then(profile => {
        console.log('Profile:', profile);
        this.profile = profile;
        this.username = this.profile.username;
      });
    }
  }

  async login() {
    console.log('In Login:');
    await this.keycloakService.login({
      redirectUri: window.location.origin
    });
  }

  logout() {
    console.log('In Logout:');
    this.keycloakService.logout(window.location.origin);
  }
}
