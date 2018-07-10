import {Component, ViewChild} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {ModalDirective} from 'ngx-bootstrap'
import {Constants} from '../../shared/utils/constants';
import {ModelInformationItem} from '../../shared/components/model-information/model-information.component';
import {ServiceModel} from '../../shared/models/serviceModel';
import {ServiceInfoService} from '../../shared/server/serviceInfo/serviceInfo.service';
import {ServiceInfoModel} from '../../shared/server/serviceInfo/serviceInfo.model';
import {AuditStatus} from '../../shared/server/serviceInfo/AuditStatus.model';
import {IframeService} from "../../shared/utils/iframe.service";

@Component({
  selector: 'audit-info-modal',
  templateUrl: './auditInfoModal.component.html',
  styleUrls: ['./auditInfoModal.component.scss']
})
export class AuditInfoModalComponent {
  static openModal: Subject<ServiceInfoModel> = new Subject<ServiceInfoModel>();
  @ViewChild('auditInfoModal') public auditInfoModal: ModalDirective;
  title: string = Constants.AuditInfoModal.TITLE;
  modelInfoItems: Array<ModelInformationItem> = [];
  serviceModel: ServiceModel;
  serviceModelName: string;
  vidInfoData: Array<AuditStatus> = [];
  msoInfoData: Array<AuditStatus> = [];
  parentElementClassName = 'content';
  isLoading = true;

  constructor(private _serviceInfoService: ServiceInfoService, private _iframeService : IframeService) {
    AuditInfoModalComponent.openModal.subscribe((jobData: ServiceInfoModel) => {
      this.initializeProperties();
      if (jobData) {
        this.openAuditInfoModal(jobData);
        _iframeService.addClassOpenModal(this.parentElementClassName);
        this.serviceModelName = jobData.serviceModelName ? jobData.serviceModelName : '';
        this.auditInfoModal.show();
      } else {
        _iframeService.removeClassCloseModal(this.parentElementClassName);
        this.auditInfoModal.hide();
      }
    })
  }

  initializeProperties() : void {
    this.modelInfoItems = null;
    this.vidInfoData = [];
    this.msoInfoData = [];
    this.isLoading = true;
  }

  openAuditInfoModal(jobData: ServiceInfoModel): void {
    this.modelInfoItems = this.createModelInformationItems(jobData);
    this.initAuditInfoData(jobData['jobId']);
    this.auditInfoModal.show();
  }

  initAuditInfoData(jobId: string) {
    this._serviceInfoService.getJobAuditStatus(jobId)
      .subscribe((res: Array<Array<AuditStatus>>) => {
        this.vidInfoData = res[0];
        this.msoInfoData = res[1];
        this.isLoading = false;
      });
  }

  createModelInformationItems(serviceModel: ServiceInfoModel): Array<ModelInformationItem> {
      return [
        new ModelInformationItem('Subscriber name', 'subscriberName', [serviceModel.subscriberName]),
        new ModelInformationItem('Service type', 'serviceType', [serviceModel.serviceType]),
        new ModelInformationItem('Service model version', 'serviceModelVersion', [serviceModel.serviceModelVersion]),
        new ModelInformationItem('Service instance name', 'serviceInstanceName', [serviceModel.serviceInstanceName], '', true),
        new ModelInformationItem('Service instance ID', 'serviceInstanceId', [serviceModel.serviceInstanceId]),
        new ModelInformationItem('Requestor User ID', 'userId', [serviceModel.userId]),
    ];
  }

  onCancelClick() {
    this._iframeService.removeClassCloseModal(this.parentElementClassName);
    this.initializeProperties();
    this.auditInfoModal.hide();
  }
}

