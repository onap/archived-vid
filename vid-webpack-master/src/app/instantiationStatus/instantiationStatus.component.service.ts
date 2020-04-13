import {Injectable} from '@angular/core';
import {ServiceInfoModel, ServiceInfoUiModel} from '../shared/server/serviceInfo/serviceInfo.model';
import * as _ from 'lodash';
import {Observable} from 'rxjs/Observable';
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../shared/store/reducers";
import {AaiService} from "../shared/services/aaiService/aai.service";
import {ServiceModel} from "../shared/models/serviceModel";
import {FeatureFlagsService, Features} from "../shared/services/featureFlag/feature-flags.service";
import {DrawingBoardModes} from "../drawingBoard/service-planning/drawing-board.modes";
import {updateDrawingBoardStatus} from "../shared/storeUtil/utils/global/global.actions";
import {Router, UrlTree} from "@angular/router";
import {of} from "rxjs";
import {MsoService} from "../shared/services/msoService/mso.service";
import {ServiceAction} from "../shared/models/serviceInstanceActions";
import {InstantiationBase} from "../shared/models/InstantiationBase";

export let PENDING : string = "pending";
export let INPROGRESS : string = "in_progress";
export let PAUSE : string = "pause";
export let X_O : string = "x-circle-o";
export let SUCCESS_CIRCLE : string = "success-circle-o";
export let STOPPED : string = "stop";
export let COMPLETED_WITH_ERRORS : string = "success_with_warning";
export let UNKNOWN : string = "question-mark-circle-o";


@Injectable()
export class InstantiationStatusComponentService {
  constructor( private _aaiService: AaiService,
               private _msoService: MsoService,
               private _router : Router,
               private _store: NgRedux<AppState>,
               private _featureFlagsService:FeatureFlagsService) {
  }

  generateServiceInfoDataMapping(arr: ServiceInfoModel[]) : { [serviceInstanceId: string]: ServiceInfoModel[]}{
    let serviceInfoData: { [serviceInstanceId: string]: ServiceInfoModel[]; } = {};
    for(let item of arr){
      if(_.isNil(serviceInfoData[item.templateId])){
        serviceInfoData[item.templateId] = [item];
      }else {
        serviceInfoData[item.templateId].push(item);
      }
    }
    return serviceInfoData;
  }

  convertObjectToArray(arr: ServiceInfoModel[]) : Observable<ServiceInfoUiModel[]>{
    const obj = this.generateServiceInfoDataMapping(arr);
    let index:number = 0;
    let result = [];
    for(let item in obj) {
      obj[item].map(item => {
        item['serviceStatus'] = this.getStatus(item.jobStatus);
        item['serviceIndex'] = index;
      });
      index++;
      result = result.concat(obj[item]);
    }

    console.log(result);
    return of(result);
  }

  isDrawingBoardViewEdit(serviceModel: ServiceModel): boolean {
    if (!_.isNil(serviceModel.vidNotions) && !_.isNil(serviceModel.vidNotions.viewEditUI)
      && serviceModel.vidNotions.viewEditUI !== 'legacy'){
      return true;
    }
    return false;
  }

  open(item: ServiceInfoModel): void {
    if (this._featureFlagsService.getFlagState(Features.FLAG_1902_VNF_GROUPING)) {
      this._aaiService.getServiceModelById(item['serviceModelId']).subscribe((result)=>{
        const serviceModel =  new ServiceModel(result);

        if (this.isDrawingBoardViewEdit(serviceModel)) {
          this.navigateToNewViewEdit(item, DrawingBoardModes.EDIT);
          return;
        }

        this.navigateToNewViewOnlyOrOldEditView(item);

      });
    }

    /*this else is here only to save time in case we don't need to retrieve service model
    it can be removed once it service model is always needed, and it doesn't save time*/
    else {
      this.navigateToNewViewOnlyOrOldEditView(item);
    }
  }

  navigateToNewViewOnlyOrOldEditView(item: ServiceInfoModel) {
    if (this._featureFlagsService.getFlagState(Features.FLAG_1902_NEW_VIEW_EDIT)) {
      this.navigateToNewViewEdit(item, DrawingBoardModes.VIEW);
    }
    else {
      this.navigateToOldViewEdit(item);
    }
  }

  navigateToOldViewEdit(item: ServiceInfoModel) {
    let query =
      `subscriberId=${item.subscriberId}&` +
      `subscriberName=${item.subscriberName}&` +
      `serviceType=${item.serviceType}&` +
      `serviceInstanceId=${item.serviceInstanceId}`;

    this._store.dispatch(updateDrawingBoardStatus(DrawingBoardModes.OLD_VIEW_EDIT));
    window.parent.location.assign('../../serviceModels.htm#/instantiate?' + query);
  }

  navigateToNewViewEdit(item: InstantiationBase, mode: DrawingBoardModes): void {
    this._store.dispatch(updateDrawingBoardStatus(mode));
    const viewEditUrlTree:UrlTree = this.getNewViewEditUrlTree(item, mode);
    this._router.navigateByUrl(viewEditUrlTree);
    window.parent.location.assign(this.getViewEditUrl(viewEditUrlTree));
  }

  getNewViewEditUrlTree(item: InstantiationBase, mode: DrawingBoardModes): UrlTree {
    return this._router.createUrlTree(
      ['/servicePlanning/' + mode],
      {
        queryParams:
        mode==DrawingBoardModes.RECREATE ?
          this.getRecreateQueryParams(item) :
          this.getDefaultViewEditQueryParams(<ServiceInfoModel> item)
      });
  }

  private getDefaultViewEditQueryParams(item: ServiceInfoModel) {
    return {
      serviceModelId: item.serviceModelId,
      serviceInstanceId: item.serviceInstanceId,
      serviceType: item.serviceType,
      subscriberId: item.subscriberId,
      jobId: item.jobId
    };
  }

  private getRecreateQueryParams(item: InstantiationBase) {
    return {
      serviceModelId: item.serviceModelId,
      jobId: item.jobId
    };
  }

  getViewEditUrl(viewEditUrlTree:UrlTree): string {
    return '../../serviceModels.htm#' + viewEditUrlTree.toString();
  }

  getStatus(status : string) : ServiceStatus {
    switch(`${status}`.toUpperCase()) {
      case  'PENDING' :
        return new ServiceStatus(PENDING, 'primary', 'Pending: The action required will be sent as soon as possible.');
      case  'IN_PROGRESS' :
        return new ServiceStatus(INPROGRESS, 'primary', 'In-progress: the service is in process of the action required.');
      case  'PAUSED' :
        return new ServiceStatus(PAUSE, 'primary', 'Paused: Service has paused and waiting for your action.\n Select actions from the menu to the right.');
      case  'FAILED' :
        return new ServiceStatus(X_O, 'error', 'Failed: All planned actions have failed.');
      case  'COMPLETED' :
        return new ServiceStatus(SUCCESS_CIRCLE, 'success', 'Completed successfully: Service is successfully instantiated, updated or deleted.');
      case  'STOPPED' :
        return new ServiceStatus(STOPPED, 'error', 'Stopped: Due to previous failure, will not be instantiated.');
      case  'COMPLETED_WITH_ERRORS' :
        return new ServiceStatus(COMPLETED_WITH_ERRORS, 'success', 'Completed with errors: some of the planned actions where successfully committed while other have not.\n Open the service to check it out.');

      default:
        return new ServiceStatus(UNKNOWN, 'primary', `Unexpected status: "${status}"`);
    }
  }

  retry(item: ServiceInfoModel): void {
      this.navigateToNewViewEdit(item, DrawingBoardModes.RETRY_EDIT);
  }

  recreate(item: ServiceInfoModel): void {
    this.navigateToNewViewEdit(item, DrawingBoardModes.RECREATE);
  }

  isRecreateEnabled(item: ServiceInfoModel): boolean {
    return item.action === ServiceAction.INSTANTIATE;
  }

  isRecreateVisible(): boolean {
    return this._featureFlagsService.getFlagState(Features.FLAG_2004_CREATE_ANOTHER_INSTANCE_FROM_TEMPLATE);
  }

  forwardToNewViewEdit(item: ServiceInfoModel): void {
    this.navigateToNewViewEdit(item, DrawingBoardModes.EDIT);
  }
}


export class ServiceStatus {
  iconClassName : string;
  color : string;
  tooltip : string;

  constructor(_iconClassName : string, _color : string, _tooltip : string){
    this.iconClassName = _iconClassName;
    this.color = _color;
    this.tooltip = _tooltip;
  }
}
