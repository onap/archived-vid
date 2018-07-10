import {Component, Input, OnInit} from '@angular/core';
import * as _ from 'lodash';
import {SdcService} from '../services/sdc.service';
import {DialogService} from 'ng2-bootstrap-modal';
import {Constants} from '../shared/utils/constants';
import {CustomTableColumnDefinition, CustomTableOptions} from './vid-table/vid-table.component';
import {ServicePopupComponent} from "../components/service-popup/service-popup.component";
import { PreviousVersionsComponent } from './previous-versions/previous-versions.component';

@Component({
  selector: 'browse-sdc',
  templateUrl: './browseSdc.component.html',
  styleUrls: ['./browseSdc.component.scss']
})


export class BrowseSdcComponent implements OnInit {

  isSpinnerVisible = false;
  isProgressVisible = false;
  error: boolean;
  status: string;
  // table

  private basicColumns: CustomTableColumnDefinition[];
  @Input () filterQuery = '';
  tableOptions: CustomTableOptions;
  private wholeData: any[];

  constructor(private _sdcService: SdcService, private dialogService: DialogService) {}

  initTableOptions() {
    this.basicColumns = [
      { displayName: 'Action', key: 'action', type: 'button' , text: 'deploy', action: 'deploy' },
      { displayName: 'UUID', key: 'uuid', filter: 'text'},
      { displayName: 'invariantUUID', key: 'invariantUUID', filter: 'text'},
      { displayName: 'Name', key: 'name', filter: 'text'},
      { displayName: 'Version', key: 'version', filter: 'text'},
      { displayName: 'Category', key: 'category', filter: 'text'},
      { displayName: 'Distribution Status', key: 'distributionStatus', filter: 'text'},
      { displayName: 'Last Updated By', key: 'lastUpdaterUserId', filter: 'text'},
      { displayName: 'Tosca Model', key: 'toscaModelUrl', filter: 'text'}
    ];

    let columns: CustomTableColumnDefinition[] = this.basicColumns.concat(
      {displayName: 'Action', key: 'actions', type: 'button', text: 'Previous Versions',
        showCondition: 'hasPreviousVersion', action: 'loadPreviousData' }
    );

    this.tableOptions = {
      data: [],
      columns: columns,
      config: {
        sortBy: 'name',
        sortOrder: 'asc',
        pageSize: 10,
        pageNumber: 1,
        totalCount: 0,
        totalPages: 0,
        maxSize: 10,
        showSelectCheckbox: true,
        showSelectAll: true,
        showSort: true,
        clientSort: true,
        clientPaging: true,
        // displayPager: true,
        // displayPageSize: true,
        stickyHeader: true,
        stickyHeaderOffset: 0,
        stickyContainer: '.table1-container'
      },
    };
  }
  private deploy(service: any): void {
    if (service) {
      console.log('this row uuid:' + service.uuid);
    }

    this.dialogService.addDialog(ServicePopupComponent, {
    })
  }

  private filterDataWithHigherVersion(serviceData) {
    let delimiter = '$$';
    let filterDataServices = {};
    for (let i = 0; i < serviceData.length; i++) {
      let index = serviceData[i].invariantUUID.trim() + delimiter + serviceData[i].name.trim();
      if (!filterDataServices[index]) {
        filterDataServices[index] = {
          service: serviceData[i],
          hasPreviousVersion: false
        };
      } else {
        filterDataServices[index].hasPreviousVersion = true;
        if (parseFloat(serviceData[i].version.trim()) > parseFloat(filterDataServices[index].service.version.trim())) {
          filterDataServices[index].service = serviceData[i];
        }
      }
    }
    return Object.keys(filterDataServices).map(function (key) {
      let service = filterDataServices[key].service;
      service.hasPreviousVersion = filterDataServices[key].hasPreviousVersion;
      return service;
    });
  }

  private initData() {
    this.isProgressVisible = true;
    this.isSpinnerVisible = true;
    console.log('getServicesModels: ');
    this._sdcService.getServicesModels().subscribe(
      // onNext() function
      value => this.getServiceCallback(value),
      // onError() function
      error => this.getServiceOnError(error),
      // onComplete() function
      () => console.log('completed')
    );
  }

  private getServiceCallback(responseBody: any): void {
    console.log('response is ' , responseBody);
    this.wholeData = responseBody.services;
    this.tableOptions.data = this.filterDataWithHigherVersion(responseBody.services);
    this.isSpinnerVisible = false;
    this.isProgressVisible = false;
  }
  private getServiceOnError(error: any): void {
    console.log('error is ' , error);
    this.status =  Constants.Status.FAILED_SERVICE_MODELS_ASDC;
    this.error = true;
    this.isSpinnerVisible = false;
  }

  private loadPreviousVersionData(service): void {
    let previousVersionData: any[] = _.filter(this.wholeData, function(item) {
      return item.invariantUUID === service.invariantUUID && item.name === service.name && service.version !== item.version;
    });

    let modalTableOptions: CustomTableOptions = {
      data: previousVersionData,
      columns: this.basicColumns,
      config: {
        sortBy: 'version',
        sortOrder: 'desc'}
    };
    // open modal
    this.dialogService.addDialog(PreviousVersionsComponent, {
      title: service.name + ' - Previous Version',
      tableOptions: modalTableOptions
    })
      .subscribe( service => {
        if (service) {
          this.deploy(service);
        }
    });
  }


  public clickAction(row) {
    switch (row.actionName) {
      case 'loadPreviousData':
        this.loadPreviousVersionData(row);
        break;
      case 'deploy':
        this.deploy(row);
        break;
    }
  }

  ngOnInit() {
    console.log('Browse SDC Service Models');
    this.initTableOptions();
    this.initData();
  }
}
