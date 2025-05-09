import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard extends KeycloakAuthGuard {
  constructor(
    protected override readonly router: Router,
    protected keycloak: KeycloakService) {
    super(router, keycloak);
  }

  public async isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    // Force the user to log in if currently unauthenticated.import
    if (!this.authenticated) {
        await this.keycloak.login({
          redirectUri: window.location.origin
        });
      }
      // Get the roles from the route data
      const requiredRoles = route.data['roles'] as Array<string> || [];

      // Allow the user to proceed if no additional roles are required to access the route
      if(!(requiredRoles instanceof Array) || (requiredRoles.length === 0)) {
        return true;
      }

      // Allow the user to proceed if all required roles are present.
      return requiredRoles.every(role => this.roles.includes(role));
    }
  }
