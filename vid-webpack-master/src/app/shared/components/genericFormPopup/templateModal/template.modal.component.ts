import {Component, OnDestroy, OnInit} from "@angular/core";
import {DialogComponent, DialogService} from "ng2-bootstrap-modal";
import {IframeService} from "../../../utils/iframe.service";
import {ActivatedRoute} from "@angular/router";
import {ServiceInfoService} from "../../../server/serviceInfo/serviceInfo.service";
import {TemplateModalComponentService} from "./template.modal.component.service";
import {TemplateTableRowModel} from "./template-table-row.model";

@Component({
  selector: 'template-modal',
  templateUrl: 'template.modal.component.html',
  styleUrls: ['template.modal.component.scss']
})

export class TemplateModalComponent extends DialogComponent<string, boolean> implements OnInit, OnDestroy {

  selectedJobId : string = null;
  templateModalComponentService: TemplateModalComponentService;
  tableData: TemplateTableRowModel[] = [];

  constructor(dialogService: DialogService,
              private _iframeService: IframeService,
              private _serviceInfoService: ServiceInfoService,
              private _templateModalComponentService: TemplateModalComponentService,
              private _route: ActivatedRoute) {
    super(dialogService);
    this.templateModalComponentService = _templateModalComponentService;
  }

  ngOnInit(): void {
    this._route
      .queryParams
      .subscribe(params => {
        this._serviceInfoService.getServicesJobInfo(true, params['serviceModelId']).subscribe((jobs) => {
          this.tableData = this._templateModalComponentService.convertResponseToUI(jobs);
        });
      });
  }

  loadTemplate = () => {

  };

  closeModal(): void {
    this.dialogService.removeDialog(this);
  }
}
