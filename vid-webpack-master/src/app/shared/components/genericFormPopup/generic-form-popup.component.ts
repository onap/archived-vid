import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormPopupDetails} from "../../models/formControlModels/formPopupDetails.model";
import {DialogComponent, DialogService} from "ng2-bootstrap-modal";
import {FormGroup} from "@angular/forms";
import {IframeService} from "../../utils/iframe.service";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import * as _ from "lodash";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {ServicePopupService} from "./genericFormServices/service/service.popup.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AaiService} from "../../services/aaiService/aai.service";
import {GenericFormPopupService} from "./generic-form-popup.service";
import {FormControlModel} from "../../models/formControlModels/formControl.model";
import {FormGeneralErrorsService} from "../formGeneralErrors/formGeneralErrors.service";
import {updateCurrentModalModeAction} from "../../storeUtil/utils/global/global.actions";
import {DrawingBoardTreeService} from "../../../drawingBoard/service-planning/drawing-board-tree/drawing-board-tree.service";


export interface PopupModel {
  type: PopupType;
  uuidData: UUIDData;
  node: ITreeNode;
  isUpdateMode: boolean;
}

export enum PopupType {
  SERVICE = 'service',
  VNF = 'vnf',
  NETWORK = 'network',
  VF_MODULE = 'vf_module',
  VF_MODULE_UPGRADE = 'vf_module_upgrade',
  VNF_GROUP = 'vnf_group'
}


@Component({
  selector: 'generic-form-popup',
  templateUrl: 'generic-form-popup.component.html',
  styleUrls: ['generic-form-popup.component.scss']
})

export class GenericFormPopupComponent extends DialogComponent<PopupModel, boolean> implements OnInit, OnDestroy {
  formPopupDetails: FormPopupDetails = null;
  dynamicForm: FormGroup;
  type: PopupType;
  uuidData: UUIDData;
  showTemplateBtn: boolean = false;
  isUpdateMode: boolean;
  node: ITreeNode = null;
  hasGeneralApiError: boolean = false;
  parentElementClassName = 'content';
  errorMsg = 'Page contains errors. Please see details next to the relevant fields.';
  serviceModelId : string;
  servicesQty = 1;
  quantityOptions = _.range(1, 51)

  constructor(dialogService: DialogService,
              private _iframeService: IframeService,
              private _store: NgRedux<AppState>,
              private _servicePopupService: ServicePopupService,
              private _activatedRoute: ActivatedRoute,
              private _aaiService: AaiService,
              private _dialogService: DialogService,
              private _route: ActivatedRoute,
              private _router : Router,
              private _genericFormPopupService: GenericFormPopupService) {
    super(dialogService);
  }

  closeDialog(that): void {
    this._iframeService.closeIframe(this.dialogService, this);
  }

  shouldShowNotification(): boolean {
    return this.formPopupDetails && this.formPopupDetails.UUIDData['bulkSize'] > 1
  }

  clearModalIsUpdateMode() : void {
    this._store.dispatch(updateCurrentModalModeAction(null));
  }

  ngOnInit(): void {
    this._store.dispatch(updateCurrentModalModeAction(this.isUpdateMode));
    this._route
      .queryParams
      .subscribe(params => {
        this.serviceModelId = params['serviceModelId'];
        if (this.serviceModelId && params['isCreate'] == "true") {
          this.onInitForCreateNewServicePopup(params['isInstantiationTemplateExists']);
        }
      });

    FormGeneralErrorsService.checkForErrorTrigger.subscribe(() => {
      this.hasSomeError(this.formPopupDetails, this.dynamicForm);
    });

    if (!_.isNil(this.uuidData)) {
      this.uuidData.popupService.closeDialogEvent.subscribe((that) => {
        DrawingBoardTreeService.triggerCheckIsDirty.next(that.uuidData.serviceId);
        this.closeDialog(that);
      });

      this.uuidData['isMacro'] = this._store.getState().service.serviceHierarchy[this.uuidData['serviceId']].service.vidNotions.instantiationType === 'Macro';
      this.formPopupDetails = this._genericFormPopupService.getGenericFormDetails(this.uuidData, this.node, this.isUpdateMode);
    }
  }

  private onInitForCreateNewServicePopup(isInstantiationTemplateExists : boolean) {
    this._genericFormPopupService.initReduxOnCreateNewService().then((serviceModelId: string) => {
      this.uuidData = <any>{
        bulkSize: 1,
        isMacro: this._store.getState().service.serviceHierarchy[serviceModelId].service.vidNotions.instantiationType === 'Macro',
        type: PopupType.SERVICE,
        serviceId: serviceModelId,
        popupService: this._servicePopupService,
      };

      this.showTemplateBtn = this._genericFormPopupService.shouldShowTemplateBtn(isInstantiationTemplateExists);

      this.uuidData.popupService.closeDialogEvent.subscribe((that) => {
        this.closeDialog(that);
      });

      this.formPopupDetails = this.uuidData.popupService.getGenericFormPopupDetails(
        this.uuidData['serviceId'],
        null,
        null,
        this.node,
        this.uuidData,
        false
      );
    });
  }

  hasSomeError(formPopupDetails: FormPopupDetails, form: FormGroup): boolean {
    if (_.isNil(formPopupDetails)) return false;
    else {
      for (let controlName in form.controls) {
        if (form.controls[controlName].errors) {
          let error: string[] = Object.keys(form.controls[controlName].errors);
          if (error.length === 1 && error[0] === 'required') {
            continue;
          } else if (Object.keys(form.controls[controlName].errors).length > 0) {
            return true;
          }
        }
      }
    }

    return formPopupDetails.formControlList.filter((item: FormControlModel) => item.type === 'DROPDOWN' && item['hasEmptyOptions'] && item.isRequired()).length > 0
  }


  openTemplateModal = (): void => {
    this._router.navigate(['/instantiationTemplatesPopup'], { queryParams: { serviceModelId: this.serviceModelId}, queryParamsHandling: 'merge' });
  }

}


export class UUIDData extends Object {
  type: string;
  popupService: any;
}

