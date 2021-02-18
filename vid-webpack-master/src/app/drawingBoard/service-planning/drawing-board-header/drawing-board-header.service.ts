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
import {FeatureFlagsService, Features} from "../../../shared/services/featureFlag/feature-flags.service";
import {ServiceInfoModel} from "../../../shared/server/serviceInfo/serviceInfo.model";

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

  upgradeService(serviceModelId: string, isUpgraded: boolean ) {
    if(isUpgraded){
      this.store.dispatch(addServiceAction(serviceModelId, ServiceInstanceActions.Upgrade));
    }
    else{
      this.store.dispatch(addServiceAction(serviceModelId, ServiceInstanceActions.None));
    }
  }

  serviceInfoModel: ServiceInfoModel;
  showAuditInfo(serviceModelId) : void {
    let instance: ServiceInstance = this.store.getState().service.serviceInstance[serviceModelId];
    let model =  new ServiceModel(this.store.getState().service.serviceHierarchy[serviceModelId]);
    this.serviceInfoModel = this.store.getState().service.serviceInfoModel;
    console.log("ShowAuditInfo : ServiceInfoModel from drawing-board-header service : ", this.serviceInfoModel);
    if(this.serviceInfoModel == null || this.serviceInfoModel == undefined){
      console.log("serviceInfoModel is null or undefined");
      this.serviceInfoModel['serviceInstanceName'] = instance.instanceName;
      this.serviceInfoModel['serviceInstanceId'] = instance.instanceId;
      this.serviceInfoModel['serviceModelVersion'] = instance.modelInfo.modelVersion;
      this.serviceInfoModel['serviceModelName'] = instance.modelInfo.modelName;
      this.serviceInfoModel['aLaCarte'] = true;
      console.log("ServiceInfoModel in if : ", this.serviceInfoModel);
    // AuditInfoModalComponent.openInstanceAuditInfoModal.next({instanceId : serviceModelId , type : 'SERVICE', model : model , instance : instance});
    }
    console.log("ServiceInfoModel : ", this.serviceInfoModel);
    AuditInfoModalComponent.openModal.next(this.serviceInfoModel);
  }

  toggleResumeService(serviceModelId, isResume: boolean) : void {
    const action: ServiceInstanceActions = isResume ? ServiceInstanceActions.Resume : ServiceInstanceActions.None;
    this.store.dispatch(addServiceAction(serviceModelId, action));

  }


  /*************************************************
    should return true if deploy should be disabled
   *************************************************/
  deployShouldBeDisabled(serviceInstanceId: string, mode : string) : boolean {
    const serviceInstance = this.store.getState().service.serviceInstance[serviceInstanceId];
    if(!_.isNil(serviceInstance)){
      const validationCounter = serviceInstance.validationCounter;
      if (!_.isNil(this.errorMsgService.errorMsgObject) && mode !== DrawingBoardModes.RETRY_EDIT) return true;
      if(validationCounter > 0) return true;
      if(serviceInstance.action !== ServiceInstanceActions.None) return false;
      if(mode === DrawingBoardModes.RETRY_EDIT || mode === DrawingBoardModes.RESUME) return false;
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
      case DrawingBoardModes.RESUME:
        return 'RESUME';
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
      case DrawingBoardModes.RESUME:
        return 'RESUME';
    }
  }

  showEditService(mode: DrawingBoardModes, serviceModelId: string): boolean{
    const serviceInstance = this.store.getState().service.serviceInstance;
    return mode === DrawingBoardModes.CREATE || mode === DrawingBoardModes.RECREATE || ((mode === DrawingBoardModes.RETRY_EDIT || mode === DrawingBoardModes.EDIT)&&
      !_.isNil(serviceInstance) && !_.isNil(serviceInstance[serviceModelId])&& serviceInstance[serviceModelId].action === ServiceInstanceActions.Create);
  }

  showResumeService(serviceModelId: string): boolean {
    const serviceInstance: ServiceInstance = !_.isNil(this.store.getState().service.serviceInstance)? this.store.getState().service.serviceInstance[serviceModelId] : null;
    if (serviceInstance)
    {
      return FeatureFlagsService.getFlagState(Features.FLAG_1908_RESUME_MACRO_SERVICE, this.store) &&
        serviceInstance.vidNotions.instantiationType.toLowerCase() === "macro" &&
        serviceInstance.subscriptionServiceType.toLowerCase() !== "transport" &&
        serviceInstance.orchStatus &&
        (serviceInstance.orchStatus.toLowerCase() === "assigned" ||
          serviceInstance.orchStatus.toLowerCase() === "inventoried");
    }
    return false;
  }
}
