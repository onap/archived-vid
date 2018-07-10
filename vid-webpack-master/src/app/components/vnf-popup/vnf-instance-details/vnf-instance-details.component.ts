import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {VNFPopupDataModel} from './vnfPopupDataModel';
import {AaiService} from '../../../services/aaiService/aai.service';
import { createVFModuleInstance, updateVFModuleInstance, updateVNFInstance } from '../../../service.actions';
import {VnfInstance} from "../../../shared/models/vnfInstance";
import {ServiceInstance} from "../../../shared/models/serviceInstance";
import {VNFModel} from "../../../shared/models/vnfModel";
import {InputType} from "../../../shared/models/inputTypes";
import {ModelInfo} from "../../../shared/models/modelInfo";
import {VfModuleInstance} from "../../../shared/models/vfModuleInstance";
import {NgRedux, select} from "@angular-redux/store";
import {AppState} from "../../../store/reducers";
import {SelectOptionInterface} from "../../../shared/models/selectOption";
import {Observable} from "rxjs/Observable";
import {loadProductFamiliesAction} from "../../../services/aaiService/aai.actions";
import {VnfInstanceDetailsService} from "./vnf-instance-details.service";
import {isNullOrUndefined} from 'util';
import {NumbersLettersUnderscoreValidator} from '../../../shared/components/validators/numbersLettersUnderscore/numbersLettersUnderscore.validator';
import * as _ from "lodash";
import {ServiceNodeTypes} from "../../../shared/models/ServiceNodeTypes";

@Component({
  selector: 'vnf-instance-details',
  templateUrl: 'vnf-instance-details.html',
  styleUrls: ['vnf-instance-details.scss'],
  providers: [AaiService]
})

export class VnfInstanceDetailsComponent implements OnInit {
  @ViewChild('vnfForm') vnfForm: 'VnfForm';
  _vnfModel: VNFModel;
  @Input ()
  set vnfModel(vnfModel: VNFModel) {
    this._vnfModel = vnfModel;
    this.updateFormGroupControlsFromVNFModel();
  }
  @Input() vnfInstance: any;
  @Input() serviceInstance: ServiceInstance;
  @Input() dynamicInputs;
  @Input() modelName: string;
  @Input() serviceUuid: string;
  @Input() userProvidedNaming: boolean;
  _modelType: string;
  @Input()
  set modelType(modelType: string) {
    this._modelType = modelType;
    this.updateFormGroupControlsFromVNFModel();
  }

  @Input() parentModelName: string;
  @Input() isNewVfModule : boolean;


  @Output() onSubmitClick: EventEmitter<any> = new EventEmitter<any>();
  @Output() onServiceInstanceNameChanged :  EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() onVolumeGroupNameChanged :  EventEmitter<boolean> = new EventEmitter<boolean>();

@Output() onDataChanged: EventEmitter<any> = new EventEmitter<any>();
  @select(['service','productFamilies'])
  readonly productFamilies : Observable<SelectOptionInterface[]>;

  vnfPopupDataModel: VNFPopupDataModel = new VNFPopupDataModel();
  lcpRegionsThatEnableLegacyRegionField = ['AAIAIC25', 'rdm3', 'rdm5a'];
  shouldShowLegacyRegion: boolean;
  instanceFormGroup: FormGroup = null;
  inputType = InputType;
  isNotUniqueInstanceName : boolean = false;
  isNotUniqueVolumeGroupName : boolean = false;

  constructor(private _aaiService: AaiService, private store: NgRedux<AppState>,
              private _vnfInstanceDetailsService : VnfInstanceDetailsService) {
    this.store.subscribe(() => {
      this.updateFormData()
    });
  }

  ngOnInit() {
    this.updateFormGroup();
    this.subscribeToFormChanges();
    this._aaiService.getCategoryParameters(null).subscribe();
    this._aaiService.getLcpRegionsAndTenants(this.serviceInstance.globalSubscriberId, this.serviceInstance.subscriptionServiceType).subscribe();
    this.updateLegacyRegionVisibility();
    this.store.dispatch(loadProductFamiliesAction());
  }

  isInputShouldBeShown(inputType: any) {
    let vnfInputs = [InputType.LCP_REGION, InputType.LOB, InputType.TENANT, InputType.PRODUCT_FAMILY, InputType.PLATFORM, InputType.ROLLBACK];
    let vfInputs = [InputType.VG];
    let exist = false;
    if (this._modelType === 'VF') {
      exist = vnfInputs.indexOf(inputType) > -1;
    }
    else {
      exist = vfInputs.indexOf(inputType) > -1;
    }
    return exist;
  }

  updateFormGroupControlsFromVNFModel() {
    if (this._vnfModel && this._modelType) {
      if (this._modelType === ServiceNodeTypes.VF) {
        const vnfInstance = <VnfInstance>this.vnfInstance;
        if (this.instanceFormGroup && this.userProvidedNaming
          && !this.instanceFormGroup.get('instanceName')) {
          const initialInstanceName = vnfInstance.instanceName || (!isNullOrUndefined(this._vnfModel.name) ? this._vnfModel.name.replace(/[-]/g, "") : this._vnfModel.name);
          this.instanceFormGroup.addControl('instanceName', new FormControl(initialInstanceName, Validators.compose([Validators.required, NumbersLettersUnderscoreValidator.valid])))
        }
      }
      else if (this._modelType === ServiceNodeTypes.VFmodule) {
        const vfInstance = <VfModuleInstance>this.vnfInstance;
        if (this.instanceFormGroup && this.userProvidedNaming && !this.instanceFormGroup.get('instanceName')) {
          this.instanceFormGroup.addControl('instanceName', new FormControl(vfInstance.instanceName, Validators.required));

          let vfModule = this.extractVfAccordingToVfModuleUuid(this.store.getState(), this._vnfModel.uuid);
          if (vfModule.volumeGroupAllowed && !this.instanceFormGroup.get('volumeGroupName')) {
            this.instanceFormGroup.addControl('volumeGroupName', new FormControl(vfInstance.volumeGroupName));
          }
        }
      }
    }
  }

  updateFormGroup() {
    const tenantDisabled = !this.vnfInstance.lcpCloudRegionId;

    if (this._modelType === ServiceNodeTypes.VF) {
      const vnfInstance = <VnfInstance>this.vnfInstance;
      this.instanceFormGroup = new FormGroup({
        productFamilyId: new FormControl(vnfInstance.productFamilyId),
        lcpCloudRegionId: new FormControl(vnfInstance.lcpCloudRegionId, Validators.required),
        tenantId: new FormControl({value: vnfInstance.tenantId, disabled: tenantDisabled}, Validators.required),
        legacyRegion: new FormControl(vnfInstance.legacyRegion),
        lineOfBusiness: new FormControl(vnfInstance.lineOfBusiness),
        platformName: new FormControl(vnfInstance.platformName, Validators.required),
      });
    }
    else if (this._modelType === ServiceNodeTypes.VFmodule) {
      const vfInstance = <VfModuleInstance>this.vnfInstance;
      this.instanceFormGroup = new FormGroup({
      });
    }

    this.instanceFormGroup.valueChanges.subscribe(()=> {
      this.checkForUniqueInstanceName();
      this.onDataChanged.next();
    });

    this.updateFormGroupControlsFromVNFModel();
  }

  private getParentVnfModel(): VNFModel {
    const rawModel = _.get(this.store.getState().service.serviceHierarchy[this.serviceUuid], ['vnfs', this.parentModelName]);
    return new VNFModel(rawModel);
  }

  extractVfAccordingToVfModuleUuid(state : any,vfModuleUuid : string) {
    const vnfs = this.store.getState().service.serviceHierarchy[this.serviceUuid].vnfs;
    const vnfsArray = Object.values(vnfs);
    for (let i = 0; i<vnfsArray.length;i++){
      let vfModules = Object.values(vnfsArray[i].vfModules);
      for (let j = 0; j<vfModules.length;j++){
        if (vfModules[j].uuid === vfModuleUuid){
          return vfModules[j];
        }
      }
    }
  }

  updateFormData() {
    let service = this.store.getState().service;
    this.vnfPopupDataModel.lcpRegions = service.lcpRegionsAndTenants.lcpRegionList;
    if (this.vnfInstance && this.vnfInstance.lcpCloudRegionId) {
      this.vnfPopupDataModel.tenants = service.lcpRegionsAndTenants.lcpRegionsTenantsMap[this.vnfInstance.lcpCloudRegionId];
      console.log('setting vnf instances tenant: ' + JSON.stringify(this.vnfPopupDataModel.tenants));
    }
    this.vnfPopupDataModel.platforms = service.categoryParameters.platformList;
    this.vnfPopupDataModel.lineOfBusinesses = service.categoryParameters.lineOfBusinessList;
    this.onDataChanged.next();
  }

  subscribeToFormChanges(): void {
    if (this.instanceFormGroup.get('lcpCloudRegionId') !== null) {
      this.instanceFormGroup.get('lcpCloudRegionId').valueChanges.subscribe(val => {
        this.setDisabledState(val, 'tenantId');
        this.updateTenantList(val);
        this.updateLegacyRegionVisibility();
        this.onDataChanged.next();
      });
    }
  }

  setDisabledState(val, field: string): void {
    if (val) {
      this.instanceFormGroup.controls[field].enable();
    }
  }

  updateLegacyRegionVisibility() {
    if (this.instanceFormGroup.get('lcpCloudRegionId') !== null) {
      this.shouldShowLegacyRegion = this.lcpRegionsThatEnableLegacyRegionField.indexOf(this.instanceFormGroup.get('lcpCloudRegionId').value) > -1;
      if (!this.shouldShowLegacyRegion) {
        this.instanceFormGroup.controls.legacyRegion.setValue(undefined);
      }
    }
  }

  updateTenantList(cloudRegionId) {
    this.resetTenantSelection();
    const tenantsForCloudRegionId = this.store.getState().service.lcpRegionsAndTenants.lcpRegionsTenantsMap[cloudRegionId];
    console.log('tenants for selected cloud region id: ' + JSON.stringify(tenantsForCloudRegionId));
    this.vnfPopupDataModel.tenants = tenantsForCloudRegionId;
  }

  resetTenantSelection() {
    this.instanceFormGroup.controls.tenantId.setValue(undefined);
  }

  checkForUniqueInstanceName() {
    let currentName = !isNullOrUndefined(this.instanceFormGroup.get('instanceName')) ? this.instanceFormGroup.get('instanceName').value : null;

    if(currentName && !this._vnfInstanceDetailsService.isUnique(this.store.getState().service.serviceInstance, this.serviceUuid, currentName, currentName === this.serviceInstance.instanceName) && this.userProvidedNaming){
      this.isNotUniqueInstanceName = true;
      this.onServiceInstanceNameChanged.emit(true);
    }else {
      this.isNotUniqueInstanceName = false;
      this.onServiceInstanceNameChanged.emit(false);
    }
  }

  checkForUniqueGroupName(){
    let currentName = this.instanceFormGroup.get('volumeGroupName').value;
    if( !this._vnfInstanceDetailsService.isUnique(this.store.getState().service.serviceInstance, this.serviceUuid, currentName, currentName === this.serviceInstance['volumeGroupName'])){
      this.isNotUniqueVolumeGroupName = true;
      this.onVolumeGroupNameChanged.emit(true);
    }else {
      this.isNotUniqueVolumeGroupName = false;
      this.onVolumeGroupNameChanged.emit(false);
    }
  }

  onSubmit(formValues): void {
    formValues.modelInfo = new ModelInfo(this._vnfModel);
    if (this._modelType === 'VFmodule') {
      let dynamicFields: { [dynamicField: string]: string; };
      dynamicFields = {};
      if(!_.isEmpty(this.dynamicInputs)) {
        this.dynamicInputs.map(function (x) {
          let dynamicField: string = x.id;
          dynamicFields[dynamicField] = formValues[dynamicField];
          delete formValues[dynamicField];
        });
      }
      formValues.instanceParams = [];
      formValues.instanceParams.push(dynamicFields);
      if(this.isNewVfModule){
        this.store.dispatch(createVFModuleInstance(formValues, this.modelName, this.serviceUuid));
      }else {
        this.store.dispatch(updateVFModuleInstance(formValues, this.modelName, this.serviceUuid));
      }

    }
    else {
      formValues.isUserProvidedNaming = this.userProvidedNaming;
      this.store.dispatch(updateVNFInstance(formValues, this.modelName, this.serviceUuid));
    }
    window.parent.postMessage({
      eventId: 'submitIframe',
      data: {
        serviceModelId: this.serviceUuid
      }
    }, "*");
    this.onSubmitClick.emit(this.serviceUuid);
  }
}
