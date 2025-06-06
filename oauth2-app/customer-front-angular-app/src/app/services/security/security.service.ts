import { Injectable } from '@angular/core';
import { KeycloakProfile } from 'keycloak-js';
import { KeycloakService, KeycloakEventType } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class SecurityService {

  public profile? : KeycloakProfile;

  constructor(public kcService: KeycloakService) {
       this.init();
  }

  init() {
    this.kcService.keycloakEvents$.subscribe({
      next: (event) => {
         if (event.type === KeycloakEventType.OnAuthSuccess) {
            this.kcService.loadUserProfile().then(profile => {
              this.profile = profile;
            });
         }
      }
    });
  }

  public hasRoleIn(roles: string[]): boolean {
    let userRoles = this.kcService.getUserRoles();
    for(let role of roles) {
      if (userRoles.includes(role)) return true;
    }
    return false;
  }
}
