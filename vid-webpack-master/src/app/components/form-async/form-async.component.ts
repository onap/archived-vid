import {Component, Input, OnInit, ViewChild} from '@angular/core';
import { NgRedux, select } from '@angular-redux/store';
import { Observable } from "rxjs/Observable";
import { updateProductFamilies } from "../../service.actions";
import { AppState } from "../../store/reducers";
import {
  loadProductFamiliesAction, loadLcpTenant, loadAicZones,
  loadCategoryParameters
} from '../../services/aaiService/aai.actions';
import { LcpRegionsAndTenants } from "../../shared/models/lcpRegionsAndTenants";
import {NgForm} from "@angular/forms";
import {SelectOption} from "../../shared/models/selectOption";
import {VNFModel} from "../../shared/models/vnfModel";

@Component({
  selector: "formasync",
  templateUrl: "form-async.template.html",
  styleUrls: ["form-async.style.scss"],

})

export class formasync implements OnInit {

  constructor(private store: NgRedux<AppState>) { }

  @ViewChild('form') form: NgForm;

  @Input()
  set model(model: VNFModel) {
    if (model) {
      this.isUserProvidedNaming = model.isUserProvidedNaming;
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
  tenants = [];

  serviceInstance: any = {
    rollback:'true'
  };

  isUserProvidedNaming: boolean = false;

  onLcpSelect(newValue: string) {
    let value: LcpRegionsAndTenants = undefined;
    this.lcpRegionsAndTenants.subscribe(data => value = data);
    this.tenants = value.lcpRegionsTenantsMap[newValue];
  }

  ngOnInit() {
    this.store.dispatch(loadProductFamiliesAction());
    this.store.dispatch(loadLcpTenant());
    this.store.dispatch(loadAicZones());
    this.store.dispatch(loadCategoryParameters());
  }
}



