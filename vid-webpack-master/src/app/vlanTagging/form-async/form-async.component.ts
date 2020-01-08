import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {NgRedux, select} from '@angular-redux/store';
import {Observable} from "rxjs";
import {AppState} from "../../shared/store/reducers";
import {
  loadAicZones,
  loadCategoryParameters,
  loadLcpTenant,
  loadProductFamiliesAction
} from '../../shared/services/aaiService/aai.actions';
import {LcpRegionsAndTenants} from "../../shared/models/lcpRegionsAndTenants";
import {NgForm} from "@angular/forms";
import {SelectOption} from "../../shared/models/selectOption";
import {VNFModel} from "../../shared/models/vnfModel";
import {Tenant} from "../../shared/models/tenant";
import {FormAsyncService} from "./form-async.service";
import {AaiService} from "../../shared/services/aaiService/aai.service";

@Component({
  selector: "formasync",
  templateUrl: "form-async.template.html",
  styleUrls: ["form-async.style.scss"],

})

export class Formasync implements OnInit {

  constructor(private store: NgRedux<AppState>, private _formAsyncService: FormAsyncService) { }

  @ViewChild('form', {static: false}) form: NgForm;
  @Input()
  set params(params: any) {
    if (params) {
      this.paramsInfo = params;
    }
  }
  @Input()
  set model(model: VNFModel) {
    if (model) {
      this.isEcompGeneratedNaming = model.isEcompGeneratedNaming;
    }
  };

  @select(['service', 'productFamilies'])
  readonly productFamilies: Observable<any>;

  @select(['service', 'lcpRegionsAndTenants'])
  readonly lcpRegionsAndTenants: Observable<any>;

  @select(['service', 'lcpRegionsAndTenants', 'lcpRegionList'])
  readonly lcpRegions: Observable<any>;

  @select(['service', 'aicZones'])
  readonly aicZones: Observable<any>;

  @select(['service', 'categoryParameters', 'platformList'])
  readonly platformList: Observable<any>;

  @select(['service', 'categoryParameters', 'lineOfBusinessList'])
  readonly lineOfBusinessList: Observable<any>;

  rollbackOnFailure = [
    new SelectOption({id: 'true', name: 'Rollback'}),
    new SelectOption({id: 'false', name: 'Don\'t Rollback'})
  ];
  tenants: Tenant[] = [];

  serviceInstance: any = {
    cloudOwner: null,
    rollback:'true'
  };

  isEcompGeneratedNaming: boolean = true;
  paramsInfo : any;

  onLcpSelect(newValue: string) {
    let value: LcpRegionsAndTenants = undefined;
    this.lcpRegionsAndTenants.subscribe(data => value = data);
    this.tenants = value.lcpRegionsTenantsMap[newValue];
    this.serviceInstance.tenantId = undefined;
  }



  onTenantSelect(newValue: string) {
    this.serviceInstance.cloudOwner = this._formAsyncService.onTenantSelect(this.tenants, newValue);
  }

  ngOnInit() {
    this.store.dispatch(loadProductFamiliesAction());
    this.store.dispatch(loadLcpTenant(this.paramsInfo['globalCustomerId'], this.paramsInfo['serviceType']));
    this.store.dispatch(loadAicZones());
    this.store.dispatch(loadCategoryParameters());
  }

  public formatCloudOwnerTrailer(cloudOwner: string):string {
    return AaiService.formatCloudOwnerTrailer(cloudOwner);
  }
}



