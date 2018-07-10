import {ActivatedRouteSnapshot, Resolve} from "@angular/router";
import {Injectable} from "@angular/core";
import {ConfigurationService} from "./configuration.service";
import {Observable} from "rxjs/Observable";

@Injectable()
export class FlagsResolve implements Resolve<Observable< { [key: string]: boolean }>> {

  constructor(private _configurationService: ConfigurationService) {}

  resolve(route: ActivatedRouteSnapshot) {
    return this._configurationService.getFlags();
  }
}
