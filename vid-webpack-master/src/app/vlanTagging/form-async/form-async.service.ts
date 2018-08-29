import {Injectable} from '@angular/core';
import {Tenant} from "../../shared/models/tenant";

@Injectable()
export class FormAsyncService {
  constructor(){}

  public onTenantSelect(tenants: Tenant[], newValue: string) {
    let tenantTemp: Tenant[] = tenants.filter(tenant => tenant.id == newValue);
    return tenantTemp[0] && tenantTemp[0].cloudOwner;
  }
}
