import {Component, ViewChild} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {ModalDirective} from 'ngx-bootstrap'
import {ModelInformationItem} from '../model-information/model-information.component';
import {ServiceModel} from '../../models/serviceModel';
import {ServiceInfoService} from '../../server/serviceInfo/serviceInfo.service';
import {ServiceInfoModel} from '../../server/serviceInfo/serviceInfo.model';
import {AuditStatus} from '../../server/serviceInfo/AuditStatus.model';
import {IframeService} from "../../utils/iframe.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {AuditInfoModalComponentService} from "./auditInfoModal.component.service";

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
  modelInfoItems: ModelInformationItem[] = [];
  serviceModel: ServiceModel;
  serviceModelName: string;
  vidInfoData: AuditStatus[] = [];
  msoInfoData: AuditStatus[] = [];
  isAlaCarte: boolean;
  parentElementClassName = 'content';
  isLoading = true;
  model: any;
  instanceId: string;
  isALaCarteFlagOn: boolean;
  type : string = "Service";
  showVidStatus : boolean = true;
  auditInfoModalComponentService : AuditInfoModalComponentService;
  constructor(private _serviceInfoService: ServiceInfoService, private _iframeService : IframeService,
              private _auditInfoModalComponentService : AuditInfoModalComponentService,
              private store: NgRedux<AppState>) {
    this.auditInfoModalComponentService = this._auditInfoModalComponentService;
    AuditInfoModalComponent.openModal.subscribe((jobData: ServiceInfoModel) => {
      this.isALaCarteFlagOn = this.store.getState().global.flags['FLAG_A_LA_CARTE_AUDIT_INFO'];
      this.initializeProperties();
      this.showVidStatus = true;
      if (jobData) {
        this.isAlaCarte = jobData.aLaCarte;
        this.openAuditInfoModal(jobData);
        _iframeService.addClassOpenModal(this.parentElementClassName);
        this.serviceModelName = jobData.serviceModelName ? jobData.serviceModelName : '';
        this.auditInfoModal.show();
      } else {
        _iframeService.removeClassCloseModal(this.parentElementClassName);
        this.auditInfoModal.hide();
      }
    });

    AuditInfoModalComponent.openInstanceAuditInfoModal.subscribe(({instanceId  , type ,  model, instance}) => {
      this.showVidStatus = false;
      this.initializeProperties();
      this.setModalTitles(type);
      this.serviceModelName = AuditInfoModalComponentService.getInstanceModelName(model);

      if (instance.isFailed) {
        this._serviceInfoService.getAuditStatusForRetry(instance.trackById).subscribe((res: AuditStatus) => {
          this.msoInfoData = [res];
        });
      }else{
        this._serviceInfoService.getInstanceAuditStatus(instanceId, type).subscribe((res : AuditStatus[]) =>{
          this.msoInfoData = res;
       });
      }
      this.modelInfoItems = this.auditInfoModalComponentService.getModelInfo(model, instance, instanceId);
      _iframeService.addClassOpenModal(this.parentElementClassName);
      this.auditInfoModal.show();
    });
  }


  setModalTitles(type : string) : void{
    this.type = AuditInfoModalComponentService.setModalTitlesType(type) ;
    this.title = AuditInfoModalComponentService.setModalTitle(type);
  }

  initializeProperties() : void {
    this.modelInfoItems = null;
    this.vidInfoData = [];
    this.msoInfoData = [];
    this.isLoading = true;
  }

  openAuditInfoModal(jobData: ServiceInfoModel): void {
    this.modelInfoItems = AuditInfoModalComponentService.createModelInformationItemsJob(jobData);
    this.initAuditInfoData(jobData);
    this.auditInfoModal.onHide.subscribe(()=>{
      this._iframeService.removeClassCloseModal(this.parentElementClassName);
      this.initializeProperties();
    });
    this.auditInfoModal.show();
  }

  initAuditInfoData(jobData: ServiceInfoModel) {
    this._serviceInfoService.getJobAuditStatus(jobData)
      .subscribe((res: AuditStatus[][]) => {
        this.vidInfoData = res[0];
        this.msoInfoData = res[1];
        this.isLoading = false;
      });
  }

  onCancelClick() {
    this._iframeService.removeClassCloseModal(this.parentElementClassName);
    this.initializeProperties();
    this.auditInfoModal.hide();
  }


  onNavigate(){
    window.open("http://ecompguide.web.att.com:8000/#ecomp_ug/c_ecomp_ops_vid.htmll#r_ecomp_ops_vid_bbglossary", "_blank");
  }
}

