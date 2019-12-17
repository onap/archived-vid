import {Component, OnDestroy, OnInit} from "@angular/core";
import {DialogComponent, DialogService} from "ng2-bootstrap-modal";
import {IframeService} from "../../../utils/iframe.service";
import {ActivatedRoute} from "@angular/router";
import {ServiceInfoService} from "../../../server/serviceInfo/serviceInfo.service";
import {InstantiationTemplatesModalService} from "./instantiation.templates.modal.service";
import {InstantiationTemplatesRowModel} from "./instantiation.templates.row.model";
import {DrawingBoardModes} from "../../../../drawingBoard/service-planning/drawing-board.modes";
import {InstantiationStatusComponentService} from "../../../../instantiationStatus/instantiationStatus.component.service";
import {AaiService} from "../../../services/aaiService/aai.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../store/reducers";
import * as _ from 'lodash';
import {forkJoin} from "rxjs";
@Component({
  selector: 'template-modal',
  templateUrl: 'instantiation.templates.modal.component.html',
  styleUrls: ['instantiation.templates.modal.component.scss']
})

export class InstantiationTemplatesModalComponent extends DialogComponent<string, boolean> implements OnInit, OnDestroy {

  selectedInstantiation: InstantiationTemplatesRowModel = null;
  templateModalComponentService: InstantiationTemplatesModalService;
  originalTableData: InstantiationTemplatesRowModel[] = [];
  filterTableData : InstantiationTemplatesRowModel[] = [];
  filterText: string;
  filterByUserId: boolean = false;

  constructor(dialogService: DialogService,
              private _iframeService: IframeService,
              private _serviceInfoService: ServiceInfoService,
              private _templateModalComponentService: InstantiationTemplatesModalService,
              private _instantiationStatusComponentService: InstantiationStatusComponentService,
              private _aaiService: AaiService,
              private _store : NgRedux<AppState>,
              private _route: ActivatedRoute) {
    super(dialogService);
    this.templateModalComponentService = _templateModalComponentService;
  }

  ngOnInit(): void {
    this.filterText = '';
    this._route
      .queryParams
      .subscribe(params => {

        const getServiceJobInfoRoute = this._serviceInfoService.getServicesJobInfo(true, params['serviceModelId']);
        const getUserIdRoute = this._aaiService.getUserId();

        forkJoin([getServiceJobInfoRoute, getUserIdRoute]).subscribe(([jobs]) => {
          this.originalTableData = this._templateModalComponentService.convertResponseToUI(jobs);
          this.filterTableData = this.originalTableData;
        });
      });
  }

  loadTemplate = () => {
    this._instantiationStatusComponentService.navigateToNewViewEdit(this.selectedInstantiation, DrawingBoardModes.RECREATE)
  };

  filterByUserIdChanged = (value : boolean) : void => {
    this.filterByUserId = value;
    const userId: string = this._store.getState().service['userId'];
    if(!_.isNil(userId)){
      this.filterTableData = this.filterByUserId ? this._templateModalComponentService.filterByUserId(userId, this.originalTableData) : this.originalTableData;
    }
  };

  closeModal(): void {
    this.dialogService.removeDialog(this);
  }
}
