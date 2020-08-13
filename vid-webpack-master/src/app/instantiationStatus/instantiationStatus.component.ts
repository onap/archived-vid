import {Component, OnInit, ViewChild} from '@angular/core';
import {ServiceInfoService} from '../shared/server/serviceInfo/serviceInfo.service';
import {ServiceInfoModel} from '../shared/server/serviceInfo/serviceInfo.model';
import {InstantiationStatusComponentService} from './instantiationStatus.component.service';
import {ContextMenuComponent, ContextMenuService} from 'ngx-contextmenu';
import {AuditInfoModalComponent} from "../shared/components/auditInfoModal/auditInfoModal.component";
import * as _ from 'lodash';
import {ScrollToConfigOptions, ScrollToService} from '@nicky-lenaers/ngx-scroll-to';
import {ConfigurationService} from "../shared/services/configuration.service";
import {LogService} from '../shared/utils/log/log.service';
import {AppState} from "../shared/store/reducers";
import {NgRedux} from '@angular-redux/store';
import {JobStatus, ServiceAction} from "../shared/models/serviceInstanceActions";
import {ActivatedRoute} from "@angular/router";
import {FeatureFlagsService, Features} from "../shared/services/featureFlag/feature-flags.service";

export interface MenuAction{
  name: string;
  dataTestId: string;
  className: string;
  tooltip?: string;
  click(item: ServiceInfoModel): void;
  enabled (item?: ServiceInfoModel): boolean;
  visible (item?: ServiceInfoModel): boolean;
}

@Component({
  selector : 'instantiation-status',
  templateUrl : './instantiationStatus.component.html',
  styleUrls : ['./instantiationStatus.component.scss']
})
export class InstantiationStatusComponent implements OnInit {

  TIMER_TIME_IN_SECONDS : number = 0;
  timer = null;
  dataIsReady : boolean = false;
  scroll : boolean = false;
  lastUpdatedDate: Date = null;
  instantiationStatusComponentService: InstantiationStatusComponentService;
  configurationService : ConfigurationService;
  serviceInfoData: ServiceInfoModel[] = null;
  @ViewChild(ContextMenuComponent, {static: false}) public contextMenu: ContextMenuComponent;

  public contextMenuActions: Array<MenuAction> = [
    {
      name: "Redeploy",
      dataTestId: "context-menu-retry",
      className: "fa-repeat",
      click: (item: ServiceInfoModel) => this.retryItem(item),
      enabled: () =>  true,
      visible: (item: ServiceInfoModel) =>  (item.isRetryEnabled && (item.jobStatus !== JobStatus.COMPLETED_AND_PAUSED)),
    },
    {
      name: "Resume",
      dataTestId: "context-menu-retry",
      className: "fa-repeat",
      click: (item: ServiceInfoModel) => this.resumeItem(item),
      enabled: () =>  true,
      visible: (item: ServiceInfoModel) =>  item.jobStatus === JobStatus.COMPLETED_AND_PAUSED,
    },
    {
      name: "Open",
      dataTestId: "context-menu-open",
      className: "fa-external-link",
      click: (item: ServiceInfoModel) => this.instantiationStatusComponentService.open(item),
      enabled: (item: ServiceInfoModel) =>  this.isOpenEnabled(item),
      visible: () =>  true,
    },
    {
      name: "New View/Edit",
      dataTestId: "context-menu-new-view-edit",
      className: "fa-pencil",
      click: (item: ServiceInfoModel) => this.instantiationStatusComponentService.forwardToNewViewEdit(item),
      enabled: () => true,
      visible: () => this.instantiationStatusComponentService.isNewViewEditVisible(),
    },
    {
      name: "Create another one",
      dataTestId: "context-menu-create-another-one",
      className: "fa-clone",
      click: (item: ServiceInfoModel) => this.instantiationStatusComponentService.recreate(item),
      enabled: (item: ServiceInfoModel) =>  this.instantiationStatusComponentService.isRecreateEnabled(item),
      visible: () =>  this.instantiationStatusComponentService.isRecreateVisible(),
    },
    {
      name: "Audit info",
      dataTestId: "context-menu-audit-info",
      className: "fa-info-circle",
      click: (item: ServiceInfoModel) => this.auditInfo(item),
      enabled: (item: ServiceInfoModel) =>  this.isAuditInfoEnabled(item),
      visible: () =>  true,
    },
    {
      name: "Delete",
      dataTestId: "context-menu-remove",
      className: "fa-trash-o",
      click: (item: ServiceInfoModel) => this.deleteItem(item),
      enabled: (item: ServiceInfoModel) =>  this.isDeleteEnabled(item),
      visible: () =>  true,
    },
    {
      name: "Hide request",
      dataTestId: "context-menu-hide",
      className: "fa-eye-slash",
      tooltip: "Hide this service from this table",
      click: (item: ServiceInfoModel) => this.hideItem(item),
      enabled: (item: ServiceInfoModel) =>  this.isHideEnabled(item),
      visible: () =>  true,
    }
  ];

  flags: any;
  filterText: string;
  constructor(private _serviceInfoService: ServiceInfoService,
              private _instantiationStatusComponentService : InstantiationStatusComponentService,
              private _contextMenuService: ContextMenuService,
              private _configurationService : ConfigurationService,
              private _scrollToService: ScrollToService,
              private _logService : LogService,
              private route: ActivatedRoute,
              private _store: NgRedux<AppState>) {
    this.instantiationStatusComponentService = _instantiationStatusComponentService;
    this.configurationService = this._configurationService;
    this.configurationService.getConfiguration("refreshTimeInstantiationDashboard").subscribe(response => {
      this.TIMER_TIME_IN_SECONDS = _.isNumber(response) ? response : 0;
      this.activateInterval();
      this.refreshData();
    });
  }

  ngOnInit() {
    let filterTextParam =  this.route.snapshot.queryParams["filterText"];
    this.filterText = filterTextParam ? filterTextParam : "" ;
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
    this._serviceInfoService.getServicesJobInfo(this.lastUpdatedDate === null)
      .subscribe((res: ServiceInfoModel[]) => {
        this._instantiationStatusComponentService.convertObjectToArray(res).subscribe((res) => {
          this._logService.info('refresh instantiation status table', res);
          this.dataIsReady = true;
          this.lastUpdatedDate = new Date();
          if (!_.isEqual(this.serviceInfoData, res)) {
            this.serviceInfoData = res;
            this.scrollToElement(this.findFirstVisibleJob());
          }
        });
      })
  }

  trackByFn(index: number, item: ServiceInfoModel){
    return _.isNil(item) ? null : item.jobId;
  }

  deleteItem(item: ServiceInfoModel): void {
    this._serviceInfoService.deleteJob(item.jobId).subscribe(() => {
      this.refreshData();
    });
  }

  hideItem(item: ServiceInfoModel): void {
    this._serviceInfoService.hideJob(item.jobId).subscribe(() => {
      this.refreshData();
    });
  }

  retryItem(item: ServiceInfoModel) : void {
    if (item.isRetryEnabled) {
      this._instantiationStatusComponentService.retry(item);
    }
  }

  resumeItem(item: ServiceInfoModel) : void {
    if(item.isRetryEnabled && item.jobStatus === JobStatus.COMPLETED_AND_PAUSED){
      this._instantiationStatusComponentService.resume(item);
    }
  }

  auditInfo(jobData : ServiceInfoModel): void {
    AuditInfoModalComponent.openModal.next(jobData);
  }

  isOpenEnabled(item: ServiceInfoModel):boolean {
    switch(item.action) {
      case ServiceAction.DELETE:
        return _.includes([ JobStatus.PENDING, JobStatus.COMPLETED_WITH_ERRORS, JobStatus.FAILED, JobStatus.FAILED_AND_PAUSED], item.jobStatus);
      case ServiceAction.UPDATE:
        return _.includes([JobStatus.PENDING, JobStatus.PAUSE, JobStatus.COMPLETED_WITH_ERRORS, JobStatus.COMPLETED, JobStatus.FAILED, JobStatus.FAILED_AND_PAUSED], item.jobStatus);
      default:
        return _.includes([JobStatus.COMPLETED, JobStatus.PAUSE, JobStatus.COMPLETED_WITH_ERRORS, JobStatus.FAILED_AND_PAUSED], item.jobStatus);
    }
  }

  isAuditInfoEnabled(item: ServiceInfoModel): boolean {
    if(item.action === ServiceAction.DELETE || item.action=== ServiceAction.UPDATE) {
      return _.includes([JobStatus.FAILED, JobStatus.IN_PROGRESS, JobStatus.COMPLETED_WITH_ERRORS, JobStatus.FAILED_AND_PAUSED, JobStatus.PAUSE, JobStatus.COMPLETED], item.jobStatus);
    }
    return true;// ServiceAction.INSTANTIATE
  }

  isDeleteEnabled(item: ServiceInfoModel):boolean {
    if( item.action === ServiceAction.DELETE || item.action === ServiceAction.UPDATE){
      return _.includes([JobStatus.PENDING], item.jobStatus);
    }
    return _.includes([JobStatus.PENDING, JobStatus.STOPPED], item.jobStatus);
  }

  isHideEnabled(item: ServiceInfoModel):boolean {
    return _.includes([JobStatus.COMPLETED, JobStatus.FAILED, JobStatus.STOPPED, JobStatus.COMPLETED_WITH_ERRORS, JobStatus.FAILED_AND_PAUSED], item.jobStatus);
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

  private getHeaderHeaderClientRect(): ClientRect {
    const element = document.querySelector("#instantiation-status thead") as HTMLElement;
    return element.getBoundingClientRect();
  }

  findFirstVisibleJob(): HTMLElement {
    const elements : any = document.querySelectorAll('#instantiation-status tr');
    const headerRect = this.getHeaderHeaderClientRect();
    if (headerRect) {
      const topEdge = headerRect.bottom;
      for (let i = 0; i < elements.length; i++) {
        if (elements[i].getBoundingClientRect().top >= topEdge)
          return elements[i];
      }
    }
    return null;
  }

  scrollToElement(currentJob: HTMLElement) {
    if (currentJob) {
      const config: ScrollToConfigOptions = {
        target: currentJob,
        duration: 0,
        offset: -1 * (this.getHeaderHeaderClientRect().height + 2),
      };

      // wait after render
      setTimeout(() => {
        this._scrollToService.scrollTo(config);
      }, 0)
    }
  }

  isInstantiationStatusFilterFlagOn() {
    return FeatureFlagsService.getFlagState(Features.FLAG_2004_INSTANTIATION_STATUS_FILTER, this._store);
  }
}
