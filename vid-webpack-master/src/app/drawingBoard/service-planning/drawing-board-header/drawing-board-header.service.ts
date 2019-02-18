import {Injectable} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {addServiceAction} from "../../../shared/storeUtil/utils/service/service.actions";
import {ServiceInstanceActions} from "../../../shared/models/serviceInstanceActions";
import {AppState} from "../../../shared/store/reducers";
import {DrawingBoardTreeComponent} from "../drawing-board-tree/drawing-board-tree.component";
import {AuditInfoModalComponent} from "../../../shared/components/auditInfoModal/auditInfoModal.component";
import {ServiceModel} from "../../../shared/models/serviceModel";
import {NgRedux} from "@angular-redux/store";
import * as _ from 'lodash';
import {ErrorMsgService} from "../../../shared/components/error-msg/error-msg.service";
import {DrawingBoardModes} from "../drawing-board.modes";
import {ServiceInstance} from "../../../shared/models/serviceInstance";

@Injectable()
export class DrawingBoardHeaderService{

  constructor(private route: ActivatedRoute, private store: NgRedux<AppState>, private errorMsgService: ErrorMsgService){}
  generateOldViewEditPath(): string{
    let query: string =
      `subscriberId=${this.route.snapshot.queryParams['subscriberId']}&` +
      `subscriberName=${this.route.snapshot.queryParams['subscriberName']}&` +
      `serviceType=${this.route.snapshot.queryParams['serviceType']}&` +
      `serviceInstanceId=${this.route.snapshot.queryParams['serviceInstanceId']}`;
    return '../../serviceModels.htm#/instantiate?' + query;
  }

  deleteService(serviceModelId: string, isDeleted: boolean ) {
    if(isDeleted){
      this.store.dispatch(addServiceAction(serviceModelId, ServiceInstanceActions.Delete));
      DrawingBoardTreeComponent.triggerDeleteActionService.next(serviceModelId);
    } else{
      this.store.dispatch(addServiceAction(serviceModelId, ServiceInstanceActions.None));
      DrawingBoardTreeComponent.triggerUndoDeleteActionService.next(serviceModelId);
    }
  }

  showAuditInfo(serviceModelId) : void {
    let instance: ServiceInstance = this.store.getState().service.serviceInstance[serviceModelId];
    let model =  new ServiceModel(this.store.getState().service.serviceHierarchy[serviceModelId]);
    AuditInfoModalComponent.openInstanceAuditInfoModal.next({instanceId : serviceModelId , type : 'SERVICE', model : model , instance : instance, trackById: instance.trackById});
  }


  /*************************************************
    should return true if deploy should be disabled
   *************************************************/
  deployShouldBeDisabled(serviceInstanceId: string, mode : string) : boolean {
    const serviceInstance = this.store.getState().service.serviceInstance[serviceInstanceId];
    if(!_.isNil(serviceInstance)){
      const validationCounter = serviceInstance.validationCounter;
      if (!_.isNil(this.errorMsgService.errorMsgObject)&& mode !== DrawingBoardModes.RETRY_EDIT) return true;
      if(validationCounter > 0) return true;
      if(serviceInstance.action !== ServiceInstanceActions.None) return false;
      if(mode === DrawingBoardModes.RETRY_EDIT) return false;
      return !serviceInstance.isDirty;
    }
    return true;
  }

  getModeButton(mode : string) : string {
    switch (mode) {
      case DrawingBoardModes.EDIT:
        return 'UPDATE';
      case DrawingBoardModes.RETRY_EDIT:
        return 'REDEPLOY';
      default: return 'DEPLOY';
    }
  }
  getButtonText(mode : DrawingBoardModes) : string {
    switch (mode) {
      case DrawingBoardModes.EDIT:
      case DrawingBoardModes.VIEW:
        return 'EDIT';
      case DrawingBoardModes.RETRY_EDIT:
      case DrawingBoardModes.RETRY:
        return 'REDEPLOY';
    }
  }

  showEditService(mode: DrawingBoardModes, serviceModelId: string): boolean{
    const serviceInstance = this.store.getState().service.serviceInstance;
    return mode === DrawingBoardModes.CREATE || ((mode === DrawingBoardModes.RETRY_EDIT || mode === DrawingBoardModes.EDIT)&&
      !_.isNil(serviceInstance) && !_.isNil(serviceInstance[serviceModelId])&& serviceInstance[serviceModelId].action === ServiceInstanceActions.Create);
  }
}
