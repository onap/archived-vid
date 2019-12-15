import {Component, OnDestroy, OnInit} from "@angular/core";
import {DialogComponent, DialogService} from "ng2-bootstrap-modal";
import {IframeService} from "../../../utils/iframe.service";
import {ActivatedRoute} from "@angular/router";
import {ServiceInfoService} from "../../../server/serviceInfo/serviceInfo.service";
import {InstantiationTemplatesModalService} from "./instantiation.templates.modal.service";
import {InstantiationTemplatesRowModel} from "./instantiation.templates.row.model";
import {DrawingBoardModes} from "../../../../drawingBoard/service-planning/drawing-board.modes";
import {InstantiationStatusComponentService} from "../../../../instantiationStatus/instantiationStatus.component.service";

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

  constructor(dialogService: DialogService,
              private _iframeService: IframeService,
              private _serviceInfoService: ServiceInfoService,
              private _templateModalComponentService: InstantiationTemplatesModalService,
              private _instantiationStatusComponentService: InstantiationStatusComponentService,
              private _route: ActivatedRoute) {
    super(dialogService);
    this.templateModalComponentService = _templateModalComponentService;
  }

  ngOnInit(): void {
    this.filterText = '';
    this._route
      .queryParams
      .subscribe(params => {
        this._serviceInfoService.getServicesJobInfo(true, params['serviceModelId']).subscribe((jobs) => {
          this.originalTableData = this._templateModalComponentService.convertResponseToUI(jobs);
          this.filterTableData = this.originalTableData;
        });
      });
  }

  loadTemplate = () => {
    this._instantiationStatusComponentService.navigateToNewViewEdit(this.selectedInstantiation, DrawingBoardModes.RECREATE)

  };


  closeModal(): void {
    this._iframeService.removeClassCloseModal('content');
    this.dialogService.removeDialog(this);
    setTimeout(() => {
      window.parent.postMessage("closeIframe", "*");
    }, 15);

  }
}
