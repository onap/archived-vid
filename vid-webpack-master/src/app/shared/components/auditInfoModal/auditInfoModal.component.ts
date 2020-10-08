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
import {FeatureFlagsService, Features} from "../../services/featureFlag/feature-flags.service";
import * as XLSX from 'xlsx';
import {DatePipe} from "@angular/common";
import {SpaceToUnderscorePipe} from "../../pipes/spaceToUnderscore/space-to-underscore.pipe";
import {ResizeEvent} from "angular-resizable-element";

@Component({
  selector: 'audit-info-modal',
  templateUrl: './auditInfoModal.component.html',
  styleUrls: ['./auditInfoModal.component.scss']
})
export class AuditInfoModalComponent {
  static openModal: Subject<ServiceInfoModel> = new Subject<ServiceInfoModel>();
  static openInstanceAuditInfoModal: Subject<{instanceId , type, model, instance}> = new Subject<{instanceId , type, model, instance}>();
  @ViewChild('auditInfoModal', {static: false}) public auditInfoModal: ModalDirective;
  title: string = 'Service Information';
  modelInfoItems: ModelInformationItem[] = [];
  serviceModel: ServiceModel;
  serviceModelName: string;
  serviceModelId: string;
  jobId: string;
  vidInfoData: AuditStatus[] = [];
  msoInfoData: AuditStatus[] = [];
  isAlaCarte: boolean;
  parentElementClassName = 'content';
  isLoading = true;
  model: any;
  instanceId: string;
  isALaCarteFlagOn: boolean;
  showMoreAuditInfoLink: boolean;
  type : string = "Service";
  showVidStatus : boolean = true;
  auditInfoModalComponentService : AuditInfoModalComponentService;
  serviceInstanceName : string;
  serviceModelVersion : any;
  exportMSOStatusFeatureEnabled: boolean;
  dataIsReady : boolean = false;
  jobDataLocal : any;
  constructor(private _serviceInfoService: ServiceInfoService, private _iframeService : IframeService,
              private _auditInfoModalComponentService : AuditInfoModalComponentService,
              private _featureFlagsService: FeatureFlagsService,
              private datePipe: DatePipe,
              private spacetoUnderscore: SpaceToUnderscorePipe,
              private store: NgRedux<AppState>) {
    this.auditInfoModalComponentService = this._auditInfoModalComponentService;
    AuditInfoModalComponent.openModal.subscribe((jobData: ServiceInfoModel) => {
      this.isALaCarteFlagOn = this.store.getState().global.flags['FLAG_A_LA_CARTE_AUDIT_INFO'];
      this.showMoreAuditInfoLink = _featureFlagsService.getFlagState(Features.FLAG_MORE_AUDIT_INFO_LINK_ON_AUDIT_INFO);
      this.exportMSOStatusFeatureEnabled = _featureFlagsService.getFlagState(Features.FLAG_2011_EXPORT_MSO_STATUS);
      this.initializeProperties();
      this.showVidStatus = true;
      if (jobData) {
        this.jobDataLocal = jobData;
        this.isAlaCarte = jobData.aLaCarte;
        this.openAuditInfoModal(jobData);
        _iframeService.addClassOpenModal(this.parentElementClassName);
        this.serviceModelName = jobData.serviceModelName ? jobData.serviceModelName : '';
        this.serviceModelId = jobData.serviceModelId;
        this.jobId = jobData.jobId;
        this.auditInfoModal.show();
        this.serviceInstanceName = jobData.serviceInstanceName;
        this.serviceModelVersion = jobData.serviceModelVersion;
      } else {
        _iframeService.removeClassCloseModal(this.parentElementClassName);
        this.auditInfoModal.hide();
      }
    });

    AuditInfoModalComponent.openInstanceAuditInfoModal.subscribe(({instanceId  , type ,  model, instance}) => {
      this.showVidStatus = false;
      this.showMoreAuditInfoLink = false;
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

  public style: object = {};
  validate(event: ResizeEvent): boolean {
    console.log("event : ", event);
    if(event.rectangle.width && event.rectangle.height &&
      ( event.rectangle.width < 800 || event.rectangle.width > 1240)
    ){
      return false;
    } else{
      return true;
    }
  }
  onResizeEnd(event: ResizeEvent): void {
    console.log('Element was resized', event);
    this.style = {
      position: 'fixed',
      left: `${event.rectangle.left}px`,
      top: `${event.rectangle.top}px`,
      width: `${event.rectangle.width}px`,
      height: `${event.rectangle.height}px`
    };
    console.log("stle : ", this.style);
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
        this.msoInfoData.sort(this.getSortOrder("startTime"));
        this.isLoading = false;
      });
  }

  exportMsoStatusTable(){
    let currentTime = new Date();
    let timestamp = this.datePipe.transform(currentTime, "MMMddyyyy")+'_'
      +currentTime.getHours()+":"+currentTime.getMinutes()+":"+currentTime.getSeconds()
    let fileName = this.spacetoUnderscore.transform(this.serviceInstanceName)+'_'+timestamp;
    let msoStatusTableElement = document.getElementById('service-instantiation-audit-info-mso');
    const ws: XLSX.WorkSheet = XLSX.utils.table_to_sheet(msoStatusTableElement);
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
    /* save to file */
    XLSX.writeFile(wb, fileName+'.csv');
    this._iframeService.addClassOpenModal(this.parentElementClassName);
  }

  onCancelClick() {
    this._iframeService.removeClassCloseModal(this.parentElementClassName);
    this.initializeProperties();
    this.auditInfoModal.hide();
  }


  onNavigate(){
    window.open("http://ecompguide.web.att.com:8000/#ecomp_ug/c_ecomp_ops_vid.htmll#r_ecomp_ops_vid_bbglossary", "_blank");
  }

  refreshData(): void {
    this.dataIsReady = false;
    this.initAuditInfoData(this.jobDataLocal);
    this.dataIsReady = true;

  }
  
  //Comparer Function
	getSortOrder(timestamp) {
	  return (obj1, obj2) =>{

		let firstObj = obj1[timestamp];
		let secondObj = obj2[timestamp];
		return ((secondObj < firstObj) ? -1 : ((secondObj > firstObj) ? 1 : 0));

	  }
	}

  readOnlyRetryUrl = (): string =>
    `../../serviceModels.htm?more#/servicePlanning/RETRY?serviceModelId=${this.serviceModelId}&jobId=${this.jobId}`
}

