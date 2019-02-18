import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {Injectable} from "@angular/core";

export enum Features {
  FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST='FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST',
  FLAG_1902_NEW_VIEW_EDIT='FLAG_1902_NEW_VIEW_EDIT',
  FLAG_1902_VNF_GROUPING='FLAG_1902_VNF_GROUPING',
  FLAG_VF_MODULE_RESUME_STATUS_CREATE = 'FLAG_VF_MODULE_RESUME_STATUS_CREATE',
  DRAG_AND_DROP_OPERATION = 'DRAG_AND_DROP_OPERATION',
  FLAG_1906_COMPONENT_INFO = 'FLAG_1906_COMPONENT_INFO',
}

@Injectable()
export class FeatureFlagsService {

  constructor(private store: NgRedux<AppState>){}

  public getFlagState(flag: Features):boolean {
    return FeatureFlagsService.getFlagState(flag, this.store);
  }

  /*static method for easy refactoring of code, so no injection of FeatureFlagsService is needed*/
  public static getFlagState(flag: Features, store: NgRedux<AppState>):boolean {
    return store.getState().global.flags[flag];
  }


}
