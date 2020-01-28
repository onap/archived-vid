import {Component, ViewChild} from '@angular/core';
import {ContextMenuComponent, ContextMenuService} from 'ngx-contextmenu';
import {DialogService} from 'ng2-bootstrap-modal';
import {MsoService} from '../../../shared/services/msoService/mso.service'
import * as _ from 'lodash';
import {ActivatedRoute} from '@angular/router';
import {ServiceInstance} from "../../../shared/models/serviceInstance";
import {OwningEntity} from "../../../shared/models/owningEntity";
import {MessageBoxData} from "../../../shared/components/messageBox/messageBox.data";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../shared/store/reducers";
import {IframeService} from "../../../shared/utils/iframe.service";
import {
  GenericFormPopupComponent,
  PopupType
} from "../../../shared/components/genericFormPopup/generic-form-popup.component";
import {ServicePopupService} from "../../../shared/components/genericFormPopup/genericFormServices/service/service.popup.service";
import {SdcUiCommon} from "onap-ui-angular";
import {DrawingBoardModes} from "../drawing-board.modes";
import {DrawingBoardHeaderService} from "./drawing-board-header.service";
import {ServiceInstanceActions} from "../../../shared/models/serviceInstanceActions";
import {DrawingBoardPermissions} from "../../guards/servicePlanningGuard/drawingBoardGuard";
import {MessageBoxService} from "../../../shared/components/messageBox/messageBox.service";

@Component({
  selector: 'drawing-board-header',
  providers: [MsoService],
  templateUrl: './drawing-board-header.component.html',
  styleUrls: ['./drawing-board-header.component.scss']
})

export class DrawingBoardHeader {
  serviceName: string;
  numServicesToDeploy: number;
  status: string = 'Designing a new service';
  serviceModelId: string;
  jobId: string;
  parentElementClassName = 'content';
  mode : DrawingBoardModes = DrawingBoardModes.CREATE;
  serviceOrchStatus: string;
  isDeleted: boolean = false;
  isUpgrade: boolean = false;
  isResume: boolean = false;
  store : NgRedux<AppState>;
  drawingBoardPermissions : DrawingBoardPermissions;
  drawingBoardHeaderService : DrawingBoardHeaderService;
  isServiceFailed: boolean;
  serviceStatusMessage: string;
  private readonly action: string;
  private presentedAction: string;

  constructor(private _contextMenuService: ContextMenuService, private dialogService: DialogService,
              private _iframeService : IframeService,
              private route: ActivatedRoute, private msoService: MsoService,
              private _servicePopupService : ServicePopupService,
              private _drawingBoardHeaderService : DrawingBoardHeaderService,
              private _store: NgRedux<AppState>,
              private _drawingBoardPermissions : DrawingBoardPermissions) {
    this.store = _store;
    this.drawingBoardPermissions = _drawingBoardPermissions;
    this.drawingBoardHeaderService = _drawingBoardHeaderService;
    this.mode = (!_.isNil(this.route.routeConfig.path) && this.route.routeConfig.path !== "") ?   this.route.routeConfig.path as DrawingBoardModes : DrawingBoardModes.CREATE;
    this.route
      .queryParams
      .subscribe((params) => {
        this.serviceModelId = params['serviceModelId'];
        this.jobId = params['jobId'];
        if (_.has(this.store.getState().service.serviceHierarchy, this.serviceModelId)) {
          this.setValuesFromStore();
          this.store.subscribe(() => {
            this.setValuesFromStore();
          });
        }
      });
    if (!_.isNil(this.store.getState().service.serviceInstance[this.serviceModelId].action)){
      if (this.store.getState().service.serviceInstance[this.serviceModelId].action.includes("Upgrade")) {
        this.isUpgrade = true;
      }
    }
  }


  @ViewChild(ContextMenuComponent, {static: false}) public contextMenu: ContextMenuComponent;

  editViewEdit(): void {
     window.parent.location.assign(this._drawingBoardHeaderService.generateOldViewEditPath());
  }

  isPermitted() : boolean {
    return this.drawingBoardPermissions.isEditPermitted;
}

  getModeName():string {
    switch (this.mode) {
      case DrawingBoardModes.CREATE:
        return 'IN DESIGN';
      case DrawingBoardModes.VIEW:
      case DrawingBoardModes.RETRY:
        return 'VIEW ONLY';
      case DrawingBoardModes.EDIT:
      case DrawingBoardModes.RETRY_EDIT:
        return 'IN EDITING';
      default:
        return 'IN DESIGN';
    }

  }
  public onContextMenu($event: MouseEvent, item: any): void {
    this._contextMenuService.show.next({
      contextMenu: this.contextMenu,
      event: $event,
      item: item,
    });
    $event.preventDefault();
    $event.stopPropagation();
  }

  private setValuesFromStore() {
    if(!_.isNil(this.store.getState().service.serviceInstance) && !_.isNil(this.store.getState().service.serviceInstance[this.serviceModelId])){
      const serviceInstance = this.store.getState().service.serviceInstance[this.serviceModelId];
      this.numServicesToDeploy = serviceInstance.bulkSize;
      this.serviceName = serviceInstance.instanceName || '<Automatically Assigned>';
      this.serviceOrchStatus =  serviceInstance.orchStatus || "";
      this.isServiceFailed = serviceInstance.isFailed;
      this.serviceStatusMessage = serviceInstance.statusMessage;
      this.isUpgrade = serviceInstance.isUpgraded;
    }
  }

  public editService(): void {
    if(IframeService.isIframe()){
      this._iframeService.addClassOpenModal(this.parentElementClassName);
    }
    this.dialogService.addDialog(GenericFormPopupComponent, {
      type: PopupType.SERVICE,
      uuidData: <any>{
        type : PopupType.SERVICE,
        isMacro : this.store.getState().service.serviceHierarchy[this.serviceModelId].service.vidNotions.instantiationType === 'Macro',
        serviceId: this.serviceModelId,
        popupService: this._servicePopupService
      },
      isUpdateMode: true
    });
  }



  onDeleteUndoDeleteClick(){
    this.cancelResume(this.serviceModelId);
    this.isDeleted = !this.isDeleted;
    this._drawingBoardHeaderService.deleteService(this.serviceModelId, this.isDeleted)
  }

  determineDataTestId() :string {
    switch(true) {
      case this.isResume:
        return'resume-status-type-header';
      case this.isDeleted:
        return 'delete-status-type-header';
      case this.isUpgrade:
        return 'upgrade-status-type-header';
    }
  }

  onResumeUndoResumeClick(){
    this.cancelDelete(this.serviceModelId);
    this.isResume = !this.isResume;
    this._drawingBoardHeaderService.toggleResumeService(this.serviceModelId, this.isResume);
  }

  cancelDelete(serviceModelId: string) {
    if (this.isDeleted) {
      this.isDeleted = false;
      this._drawingBoardHeaderService.deleteService(serviceModelId,this.isDeleted);
    }
  }

  cancelResume(serviceModelId: string) {
    if (this.isResume) {
      this.isResume = false;
      this._drawingBoardHeaderService.toggleResumeService(serviceModelId,this.isResume);
    }
  }

  extractOwningEntityNameAccordingToId(id:String): string {
    let owningEntityName;
    _.forEach(this.store.getState().service.categoryParameters.owningEntityList,(owningEntity: OwningEntity) => {
      if (owningEntity.id === id) {
        owningEntityName = owningEntity.name;

      }});
    return owningEntityName;
  }

  private extractSubscriberNameByGlobalSubscriberId(globalSubscriberId: string) {
    return this.store.getState().service.subscribers.find(sub => sub.id === globalSubscriberId).name;
  }

  extractServiceFields(): any {
    let instanceFields : ServiceInstance;
    instanceFields = this.store.getState().service.serviceInstance[this.serviceModelId];
    if (instanceFields.action === ServiceInstanceActions.Create) {
      if(_.isNil(instanceFields.subscriberName)) {
        instanceFields.subscriberName = this.extractSubscriberNameByGlobalSubscriberId(instanceFields.globalSubscriberId);
      }
      if (_.isNil(instanceFields.owningEntityName)) {
        instanceFields.owningEntityName = this.extractOwningEntityNameAccordingToId(instanceFields.owningEntityId);
      }
    }
    return _.omit(instanceFields,['optionalGroupMembersMap', 'upgradedVFMSonsCounter', 'isUpgraded', 'latestAvailableVersion']);
  }

  private getAction(): string {
    if(!_.isNil(this.store.getState().service.serviceInstance[this.serviceModelId].action))
      return this.store.getState().service.serviceInstance[this.serviceModelId].action.split('_').pop();
    return;
  }

  public deployService(): void {
      let instanceFields = this.extractServiceFields();
      if (this.mode !== DrawingBoardModes.RETRY_EDIT) {
        instanceFields.rollbackOnFailure = instanceFields.rollbackOnFailure === 'true';
        this.msoService.submitMsoTask(instanceFields).subscribe((result) => {
          window.parent.postMessage("navigateToInstantiationStatus", '*');
        });
      } else {
        this.msoService.retryBulkMsoTask(this.jobId, instanceFields).subscribe((result) => {
          window.parent.postMessage("navigateToInstantiationStatus", '*');
        });
      }
  }

  closePage() {
    let messageBoxData : MessageBoxData = new MessageBoxData(
         "Delete Instantiation",  // modal title
      "You are about to stop the instantiation process of this service. \nAll data will be lost. Are you sure you want to stop?",
      SdcUiCommon.ModalType.warning,
      SdcUiCommon.ModalSize.medium,
             [
      {text:"Stop Instantiation", size:"large",  callback: this.navigate.bind(this), closeModal:true},
      {text:"Cancel", size:"medium", closeModal:true}
    ]);

    MessageBoxService.openModal.next(messageBoxData);
  }



  navigate(){
    window.parent.postMessage("navigateTo", "*");
  }
}
