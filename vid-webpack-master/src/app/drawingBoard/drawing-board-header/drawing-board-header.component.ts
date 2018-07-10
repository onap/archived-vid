import {Component, ViewChild} from '@angular/core';
import {ContextMenuComponent, ContextMenuService} from 'ngx-contextmenu';
import {DialogService} from 'ng2-bootstrap-modal';
import {ServicePopupComponent} from '../../components/service-popup/service-popup.component';
import {MsoService} from '../../services/msoService/mso.service'
import * as _ from 'lodash';
import {ActivatedRoute} from '@angular/router';
import {ServiceInstance} from "../../shared/models/serviceInstance";
import {OwningEntity} from "../../shared/models/owningEntity";
import {MessageBoxData, ModalSize, ModalType} from "../../shared/components/messageBox/messageBox.data";
import {MessageBoxService} from "../../shared/components/messageBox/messageBox.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {IframeService} from "../../shared/utils/iframe.service";

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
  parentElementClassName = 'content';

  constructor(private _contextMenuService: ContextMenuService, private dialogService: DialogService,
              private _iframeService : IframeService,
              private route: ActivatedRoute, private msoService: MsoService,
              private store: NgRedux<AppState>) {
    this.route
      .queryParams
      .subscribe(params => {
        this.serviceModelId = params['serviceModelId'];
        if (_.has(this.store.getState().service.serviceHierarchy, this.serviceModelId)) {
          this.setValuesFromStore();
          this.store.subscribe(() => {
            this.setValuesFromStore();
          });
        }
      });
  }


  @ViewChild(ContextMenuComponent) public contextMenu: ContextMenuComponent;

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
    const serviceInstance = this.store.getState().service.serviceInstance[this.serviceModelId];
    this.numServicesToDeploy = serviceInstance.bulkSize;
    this.serviceName = serviceInstance.instanceName || '<Automatically Assigned>';

  }

  public editService(): void {
    this._iframeService.addClassOpenModal(this.parentElementClassName);
    this.dialogService.addDialog(ServicePopupComponent, {})

  }


  extractOwningEntityNameAccordingtoId(id:String): string {
    let owningEntityName;
    _.forEach(this.store.getState().service.categoryParameters.owningEntityList,function(owningEntity: OwningEntity) {
      if (owningEntity.id === id) {
        owningEntityName = owningEntity.name;

      }})

    return owningEntityName;
  }

  extractServiceFields(): any {
    let instanceFields : ServiceInstance;
    instanceFields = this.store.getState().service.serviceInstance[Object.keys(this.store.getState().service.serviceInstance)[0]];
    instanceFields.subscriberName = this.store.getState().service.subscribers.find(sub => sub.id === instanceFields.globalSubscriberId).name;
    instanceFields.owningEntityName = this.extractOwningEntityNameAccordingtoId(instanceFields.owningEntityId);
    return instanceFields;
  }

  public deployMacroservice(): void {
    var instanceFields = this.extractServiceFields();
    instanceFields.rollbackOnFailure = instanceFields.rollbackOnFailure === 'true';
    this.msoService.submitMsoTask(instanceFields).subscribe((result) => {
      window.parent.postMessage("navigateToInstantiationStatus", '*');
      })
  }

  closePage() {
    let messageBoxData : MessageBoxData = new MessageBoxData(
         "Delete Instantiation",  // modal title
      "You are about to stop the instantiation process of this service. \nAll data will be lost. Are you sure you want to stop?",

              ModalType.alert,
              ModalSize.medium,
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
