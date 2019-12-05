import {Component, OnDestroy, OnInit} from "@angular/core";
import {DialogComponent, DialogService} from "ng2-bootstrap-modal";
import {IframeService} from "../../../utils/iframe.service";
import {ActivatedRoute} from "@angular/router";
import {ServiceInfoService} from "../../../server/serviceInfo/serviceInfo.service";
import {InstantiationTemplatesModalService} from "./instantiation.templates.modal.service";
import {InstantiationTemplatesRowModel} from "./instantiation.templates.row.model";

@Component({
  selector: 'template-modal',
  templateUrl: 'instantiation.templates.modal.component.html',
  styleUrls: ['instantiation.templates.modal.component.scss']
})

export class InstantiationTemplatesModalComponent extends DialogComponent<string, boolean> implements OnInit, OnDestroy {

  selectedJobId : string = null;
  templateModalComponentService: InstantiationTemplatesModalService;
  originalTableData: InstantiationTemplatesRowModel[] = [];

  filterText : string = '';
  constructor(dialogService: DialogService,
              private _iframeService: IframeService,
              private _serviceInfoService: ServiceInfoService,
              private _templateModalComponentService: InstantiationTemplatesModalService,
              private _route: ActivatedRoute) {
    super(dialogService);
    this.templateModalComponentService = _templateModalComponentService;
  }

  ngOnInit(): void {
    this._route
      .queryParams
      .subscribe(params => {
        this._serviceInfoService.getServicesJobInfo(true, params['serviceModelId']).subscribe((jobs) => {
          this.originalTableData = this._templateModalComponentService.convertResponseToUI(jobs);
          this.filterText = '';
        });
      });
  }

  loadTemplate = () => {

  };


  onFilterKeypress = (event : KeyboardEvent) => {
    //event.target.value
      console.log(event.altKey);
  };

  closeModal(): void {
    this.dialogService.removeDialog(this);
  }
}
