import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {Injectable} from "@angular/core";

export enum Features {
  FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST='FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST',
  FLAG_1902_NEW_VIEW_EDIT='FLAG_1902_NEW_VIEW_EDIT',
  FLAG_1902_VNF_GROUPING='FLAG_1902_VNF_GROUPING',
  FLAG_VF_MODULE_RESUME_STATUS_CREATE = 'FLAG_VF_MODULE_RESUME_STATUS_CREATE',
  FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE = 'FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE',
  FLAG_1906_COMPONENT_INFO = 'FLAG_1906_COMPONENT_INFO',
  FLAG_1908_RESUME_MACRO_SERVICE = 'FLAG_1908_RESUME_MACRO_SERVICE',
  FLAG_FLASH_REPLACE_VF_MODULE ='FLAG_FLASH_REPLACE_VF_MODULE',
  FLAG_FLASH_MORE_ACTIONS_BUTTON_IN_OLD_VIEW_EDIT ='FLAG_FLASH_MORE_ACTIONS_BUTTON_IN_OLD_VIEW_EDIT',
  FLAG_2002_VFM_UPGRADE_ADDITIONAL_OPTIONS ='FLAG_2002_VFM_UPGRADE_ADDITIONAL_OPTIONS',
  FLAG_2004_INSTANTIATION_STATUS_FILTER ='FLAG_2004_INSTANTIATION_STATUS_FILTER',
  FLAG_2004_CREATE_ANOTHER_INSTANCE_FROM_TEMPLATE = 'FLAG_2004_CREATE_ANOTHER_INSTANCE_FROM_TEMPLATE',
  FLAG_2006_VFM_SDNC_PRELOAD_FILES = 'FLAG_2006_VFM_SDNC_PRELOAD_FILES',
  FLAG_MORE_AUDIT_INFO_LINK_ON_AUDIT_INFO = 'FLAG_MORE_AUDIT_INFO_LINK_ON_AUDIT_INFO',
  FLAG_2004_INSTANTIATION_TEMPLATES_POPUP = 'FLAG_2004_INSTANTIATION_TEMPLATES_POPUP',
  FLAG_2006_USER_PERMISSIONS_BY_OWNING_ENTITY= 'FLAG_2006_USER_PERMISSIONS_BY_OWNING_ENTITY',
  FLAG_2006_SHOW_NEW_SIDE_MENU = 'FLAG_2006_SHOW_NEW_SIDE_MENU',
  FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF = 'FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF',
  FLAG_2006_VNF_LOB_MULTI_SELECT= 'FLAG_2006_VNF_LOB_MULTI_SELECT',
  FLAG_2006_NEW_VIEW_EDIT_BUTTON_IN_INSTANTIATION_STATUS= 'FLAG_2006_NEW_VIEW_EDIT_BUTTON_IN_INSTANTIATION_STATUS',

}

@Injectable()
export class FeatureFlagsService {

  constructor(private store: NgRedux<AppState>){}

  public getFlagState(flag: Features):boolean {
    return FeatureFlagsService.getFlagState(flag, this.store);
  }

  public getAllFlags():  { [key: string]: boolean}{
    return this.store.getState().global.flags;
  }

  public static getAllFlags(store: NgRedux<AppState>):  { [key: string]: boolean}{
    return store.getState().global.flags;
  }

  /*static method for easy refactoring of code, so no injection of FeatureFlagsService is needed*/
  public static getFlagState(flag: Features, store: NgRedux<AppState>):boolean {
    let storeStateGlobalFields = store.getState().global;
    if(storeStateGlobalFields && storeStateGlobalFields.flags && storeStateGlobalFields.flags[flag] !== undefined){
      return storeStateGlobalFields.flags[flag];
    }
    return false;
  }


}
