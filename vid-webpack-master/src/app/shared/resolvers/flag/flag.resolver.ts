import {ActivatedRouteSnapshot, Resolve} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {ConfigurationService} from "../../services/configuration.service";

@Injectable()
export class FlagsResolve implements Resolve<Observable< { [key: string]: boolean }>> {

  constructor(private _configurationService: ConfigurationService) {}

  resolve(route: ActivatedRouteSnapshot) {
    return this._configurationService.getFlags();
  }
}
