import {Injectable} from "@angular/core";
import {SideMenuModel} from "./side-menu.model";

@Injectable()
export class SideMenuService {
  getRouteIcon(sideMenuItem: SideMenuModel) : string {
    const iconsMap = {
      'welcome' : 'home',
      'Search for Existing Service Instances' : 'icons_search-o',
      'serviceModels.htm#/instances/subscribers' : 'plus-circle-o',
      'serviceModels.htm' : 'attachment',
      'serviceModels.htm#/instantiationStatus' : 'indesign_status'
    };

    if(iconsMap[sideMenuItem.action]){
      return iconsMap[sideMenuItem.action];
    }else {
      return 'location'
    }
  }
}
