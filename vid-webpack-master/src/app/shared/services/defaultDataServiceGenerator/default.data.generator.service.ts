import { Injectable } from '@angular/core';
import * as _ from 'lodash';
import { createVFModuleInstance, updateVNFInstance } from '../../../service.actions';
import { NgRedux } from '@angular-redux/store';
import { AppState } from '../../../store/reducers';

@Injectable()
export class DefaultDataGeneratorService {
  static controlsFieldsStatus = {};

  constructor(private store: NgRedux<AppState>) { }

  updateReduxOnFirstSet(serviceId: string, formServiceValues: any): void {
    const serviceHierarchy = this.store.getState().service.serviceHierarchy[serviceId];
    if (serviceHierarchy && !_.isEmpty(serviceHierarchy.vnfs)) {
      for (let vnfUUID in serviceHierarchy.vnfs) {
        for (let vnfModuleUUID in serviceHierarchy.vnfs[vnfUUID].vfModules) {
          if (serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].properties.minCountInstances > 0) {

            let vfModule = this.generateVFModule(serviceHierarchy, vnfUUID, vnfModuleUUID);
            this.updateVNFInstanceRedux(
              serviceHierarchy.vnfs[vnfUUID].modelName,
              serviceId,
              serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].properties.initialCount,
              vfModule,
              this.generateVNFData(serviceHierarchy, vnfUUID, vnfModuleUUID, formServiceValues),
              vnfModuleUUID
            );
          }
        }
      }
    }
  }

  updateVNFInstanceRedux(vnfModelName: string, serviceId: string, numberOfVfModules: number, vfModuleData: any, vnfData: any, vfModuleName : string): void {
    if (numberOfVfModules > 0) {
      this.store.dispatch(updateVNFInstance(vnfData, vnfData.modelInfo.modelCustomizationName, serviceId));
      for (let i = 0; i < numberOfVfModules; i++) {
        this.store.dispatch(createVFModuleInstance(vfModuleData, vfModuleName, serviceId));
      }
    }
  }


  generateVFModule(serviceHierarchy: any, vnfUUID: string, vnfModuleUUID: string) {
    return {
      'sdncPreReload': null,
      'modelInfo': {
        'modelType': 'VFmodule',
        'modelInvariantId': serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].invariantUuid,
        'modelVersionId': serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].uuid,
        'modelName': serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].name,
        'modelVersion': serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].version,
        'modelCustomizationId': serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].customizationUuid,
        'modelCustomizationName': serviceHierarchy.vnfs[vnfUUID].vfModules[vnfModuleUUID].modelCustomizationName
      },
      'instanceParams': [
        {}
      ]
    };
  }

  generateVNFData(serviceHierarchy: any, vnfName: string, vnfUUID: string, formValues: any) {
    return {
      'productFamilyId': formValues.productFamilyId,
      'lcpCloudRegionId': null,
      'tenantId': null,
      'lineOfBusiness': null,
      'platformName': null,
      'modelInfo': {
        'modelType': 'VF',
        'modelInvariantId': serviceHierarchy.vnfs[vnfName].invariantUuid,
        'modelVersionId': formValues.modelInfo.modelVersionId,
        'modelName': serviceHierarchy.vnfs[vnfName].name,
        'modelVersion': serviceHierarchy.vnfs[vnfName].version,
        'modelCustomizationId': serviceHierarchy.vnfs[vnfName].modelCustomizationId,
        'modelCustomizationName': serviceHierarchy.vnfs[vnfName].modelCustomizationName
      },
      'isUserProvidedNaming': null
    }
  }
}
