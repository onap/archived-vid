import {AfterViewChecked, Component, ViewChild} from '@angular/core';
import {ServiceInfoService} from '../shared/server/serviceInfo/serviceInfo.service';
import {ServiceInfoModel} from '../shared/server/serviceInfo/serviceInfo.model';
import {InstantiationStatusComponentService} from './instantiationStatus.component.service';
import {ContextMenuComponent, ContextMenuService} from 'ngx-contextmenu';
import {AuditInfoModalComponent} from "./auditInfoModal/auditInfoModal.component";
import * as _ from 'lodash';
import {ScrollToConfigOptions, ScrollToService} from '@nicky-lenaers/ngx-scroll-to';
import {ConfigurationService} from "../services/configuration.service";
import {LogService} from '../shared/utils/log/log.service';


@Component({
  selector : 'instantiation-status',
  templateUrl : './instantiationStatus.component.html',
  styleUrls : ['./instantiationStatus.component.scss']
})
export class InstantiationStatusComponent implements AfterViewChecked{


  TIMER_TIME_IN_SECONDS : number = 0;
  timer = null;
  dataIsReady : boolean = false;
  scroll : boolean = false;
  lastUpdatedDate: Date = null;
  currentJobId: string = null;
  instantiationStatusComponentService: InstantiationStatusComponentService;
  configurationService : ConfigurationService;
  serviceInfoData: Array<ServiceInfoModel> = null;
  @ViewChild(ContextMenuComponent) public contextMenu: ContextMenuComponent;

  constructor(private _serviceInfoService: ServiceInfoService,
              private _instantiationStatusComponentService : InstantiationStatusComponentService,
              private _contextMenuService: ContextMenuService,
              private _configurationService : ConfigurationService,
              private _scrollToService: ScrollToService,
              private _logService : LogService) {
    this.instantiationStatusComponentService = _instantiationStatusComponentService;
    this.configurationService = this._configurationService;
    this.configurationService.getConfiguration("refreshTimeInstantiationDashboard").subscribe(response => {
      this.TIMER_TIME_IN_SECONDS = _.isNumber(response) ? response : 0;
      this.activateInterval();
      this.refreshData();
    });
  }

  activateInterval() {
    if (this.TIMER_TIME_IN_SECONDS > 0) {
      this.timer = setInterval(() => {
        this.refreshData();
      }, this.TIMER_TIME_IN_SECONDS * 1000);
    }
  }

  deactivateInterval() {
    clearInterval(this.timer);
  }

  refreshData(): void {
    this.dataIsReady = false;
    this._serviceInfoService.getServicesJobInfo(true)
      .subscribe((res: Array<ServiceInfoModel>) => {
        this._instantiationStatusComponentService.convertObjectToArray(res).subscribe((res) => {
          this._logService.info('refresh instantiation status table', res);
          this.dataIsReady = true;
          this.lastUpdatedDate = new Date();
          if (!_.isEqual(this.serviceInfoData, res)) {
            this.serviceInfoData = res;
            this.scroll = true;
          }
        });
      })
  }

  ngAfterViewChecked(){
    if (this.scroll) {
      this.scrollToElement();
      this.scroll = false;
    }
  }



  isDeleteEnabled(item):boolean {
    return _.includes(['PENDING', 'STOPPED'], item.jobStatus);
  }

  deleteItem(item): void {
    this._serviceInfoService.deleteJob(item.jobId).subscribe(() => {
      this.refreshData();
    });
  }

  hideItem(item): void {
    this._serviceInfoService.hideJob(item.jobId).subscribe(() => {
      this.refreshData();
    });
  }

  auditInfo(jobData : ServiceInfoModel): void {
    AuditInfoModalComponent.openModal.next(jobData);

  }

  isOpenVisible(item):boolean {
    return _.includes(['COMPLETED', 'PAUSE'], item.jobStatus);
  }

  open(item): void {
    let query =
      `subscriberId=${item['subscriberName']}&` +
      `serviceType=${item['serviceType']}&` +
      `serviceInstanceId=${item['serviceInstanceId']}`;

    window.parent.location.assign('../../serviceModels.htm#/instantiate?' + query);
  }

  public onContextMenu($event: MouseEvent, item: any): void {
    this._contextMenuService.show.next({
      contextMenu: this.contextMenu,
      event: $event,
      item: item,
    });
    $event.preventDefault();
    $event.stopPropagation();
  }

  getImagesSrc(imageName : string) : string {
    return './' + imageName + '.svg';
  }

  isHideEnabled(item: any):boolean {
    return _.includes(['COMPLETED', 'FAILED', 'STOPPED'], item.jobStatus);
  }
  scrollToElement() {
    if(this.currentJobId){
      const config: ScrollToConfigOptions = {
        target: this.currentJobId,
        duration: 50,
        offset: -35 //header height
      };
      this._scrollToService.scrollTo(config);
    }
  }
}
