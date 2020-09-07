import {Component, ViewChild} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {ModalDirective} from 'ngx-bootstrap'
import {ModelInformationItem} from '../model-information/model-information.component';
import {ServiceInfoService} from '../../server/serviceInfo/serviceInfo.service';
import {ServiceInfoModel} from '../../server/serviceInfo/serviceInfo.model';
import {AuditStatus} from '../../server/serviceInfo/AuditStatus.model';
import {IframeService} from "../../utils/iframe.service";
import {AuditInfoModalComponentService} from "./auditInfoModal.component.service";
import {FeatureFlagsService, Features} from "../../services/featureFlag/feature-flags.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {AuditInformationItem} from '../../models/auditInfoControlModels/auditInformationItems.model';

@Component({
  selector: 'audit-info-modal',
  templateUrl: './auditInfoModal.component.html',
  styleUrls: ['./auditInfoModal.component.scss']
})
export class AuditInfoModalComponent {
  static openModal: Subject<ServiceInfoModel> = new Subject<ServiceInfoModel>();
  static openInstanceAuditInfoModal: Subject<{instanceId , type, model, instance}> = new Subject<{instanceId , type, model, instance}>();
  @ViewChild('auditInfoModal', {static: false}) public auditInfoModal: ModalDirective;
  title: string = 'Service Instantiation Information';
  type : string = "Service";
  parentElementClassName = 'content';
  modelInfoItems: ModelInformationItem[];
  auditInfoItems: AuditInformationItem;
  serviceModelName: string;
  auditInfoModalComponentService : AuditInfoModalComponentService;
  hiddenSidebar: boolean = false;

  constructor(private _serviceInfoService: ServiceInfoService, private _iframeService : IframeService,
              private _auditInfoModalComponentService : AuditInfoModalComponentService,
              private _featureFlagsService: FeatureFlagsService,
              private store: NgRedux<AppState>) {

    this.auditInfoModalComponentService = this._auditInfoModalComponentService;

    AuditInfoModalComponent.openModal.subscribe((jobData: ServiceInfoModel) => {
      this.initializeProperties();
      if (jobData) {
        this.openAuditInfoModal(jobData);
        _iframeService.addClassOpenModal(this.parentElementClassName);
        this.auditInfoModal.show();
      } else {
        _iframeService.removeClassCloseModal(this.parentElementClassName);
        this.auditInfoModal.hide();
      }
    });

    AuditInfoModalComponent.openInstanceAuditInfoModal.subscribe(({instanceId  , type ,  model, instance}) => {
      this.initializeProperties();
      this.setModalTitles(type);
      this.setMsoInfoData(instance, instanceId, type);
      this.serviceModelName = AuditInfoModalComponentService.getInstanceModelName(model);
      this.modelInfoItems = this.auditInfoModalComponentService.getModelInfo(model, instance, instanceId);
      this.auditInfoItems.showVidStatus = false;
      _iframeService.addClassOpenModal(this.parentElementClassName);
      this.auditInfoModal.show();
    });
  }

  initializeProperties() : void {
    this.modelInfoItems = [];
    this.auditInfoItems = AuditInfoModalComponentService.initAuditInfoItems();
  }

  setMsoInfoData(instance, instanceId, type): void {
    if (instance.isFailed) {
      this._serviceInfoService.getAuditStatusForRetry(instance.trackById).subscribe((res: AuditStatus) => {
        this.auditInfoItems.msoInfoData = [res];
        });
      } else {
      this._serviceInfoService.getInstanceAuditStatus(instanceId, type).subscribe((res: AuditStatus[]) => {
        this.auditInfoItems.msoInfoData = res;
        });
      }
    this.auditInfoItems.isLoading = false;
  }

  setModalTitles(type : string) : void{
    this.type = AuditInfoModalComponentService.setModalTitlesType(type) ;
    this.title = AuditInfoModalComponentService.setModalTitle(type);
  }

  openAuditInfoModal(jobData: ServiceInfoModel): void {
    this.initAuditInfoData(jobData);
    this.modelInfoItems = AuditInfoModalComponentService.createModelInformationItemsJob(jobData);
    this.serviceModelName = jobData.serviceModelName ? jobData.serviceModelName : '';
    this.auditInfoItems.isALaCarteFlagOn = this.store.getState().global.flags['FLAG_A_LA_CARTE_AUDIT_INFO'];
    this.auditInfoItems.showMoreAuditInfoLink = this._featureFlagsService.getFlagState(Features.FLAG_MORE_AUDIT_INFO_LINK_ON_AUDIT_INFO);
    this.auditInfoItems.isAlaCarte = jobData.aLaCarte;
    this.auditInfoItems.serviceModelId = jobData.serviceModelId;
    this.auditInfoItems.jobId = jobData.jobId;
    this.auditInfoModal.onHide.subscribe(()=>{
      this._iframeService.removeClassCloseModal(this.parentElementClassName);
      this.initializeProperties();
    });
    this.auditInfoModal.show();
  }

  initAuditInfoData(jobData: ServiceInfoModel): void {
    this._serviceInfoService.getJobAuditStatus(jobData).subscribe((res: AuditStatus[][]) => {
      this.auditInfoItems.vidInfoData = res[0];
      this.auditInfoItems.msoInfoData = res[1];
      this.auditInfoItems.isLoading = false;
      });
  }

  onCancelClick() {
    this._iframeService.removeClassCloseModal(this.parentElementClassName);
    this.initializeProperties();
    this.auditInfoModal.hide();
  }

  hideSidebar()
  {
    this.hiddenSidebar = !this.hiddenSidebar;
  }
}

