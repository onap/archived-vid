import { Injectable } from '@angular/core';

@Injectable()
export class DataService {

  private static _availableVolumeGroupList;
  private static _cloudRegionTenantList;
  private static _globalCustomerId;
  private static _customizationUUID;
  private static _rescustomizationUUID;
  private static _inventoryItem;
  private static _modelId;
  private static _modelInstanceName;
  private static _modelInfo;
  private static _networkInstanceId;
  private static _serviceIdList;
  private static _aicZones;
  private static _aicZone;
  private static _serviceInstanceId;
  private static _serviceInstanceName;
  private static _serviceName;
  private static _serviceType;
  private static _serviceUuid;
  private static _serviceTypeName;
  private static _createSubscriberName;
  private static _uploadSupplementoryDataFile;
  private static _supplementoryDataFile;
  private static _subscriberId;
  private static _loggedInUserId;
  private static _subscriberName;
  private static _subscribers;
  private static _subscriptionServiceTypeList;
  private static _userParams;
  private static _userServiceInstanceName;
  private static _vfModuleInstanceId;
  private static _vnfInstanceId;
  private static _vfModuleInstanceName;
  private static _volumeGroupInstanceId;
  private static _lcpRegion;
  private static _tenant;
  private static _treeHandle;
  private static _serviceInstanceToCustomer;
  private static _aLaCarte: boolean;
  private static _macro: boolean;
  private static _resources;
  private static _syspropProvStatusList;
  private static _updatedvnfProvStatus;
  private static _arbitraryParameters;
  private static _hideServiceFields;
  private static _serviceProxies;
  private static _sourceServiceProxies;
  private static _collectorServiceProxies;
  private static _configurationByPolicy;
  private static _suppressRollback;
  private static _portMirroningConfigFields;
  private static _configurationInstanceId: string;
  private static _configurationStatus: string;
  private static _portStatus: string;
  private static _portId: string;
  private static _pnf;
  private static _owningEntityProperties;

  static get availableVolumeGroupList() {
    return this._availableVolumeGroupList;
  }

  static set availableVolumeGroupList(value) {
    this._availableVolumeGroupList = value;
  }

  static get cloudRegionTenantList() {
    return this._cloudRegionTenantList;
  }

  static set cloudRegionTenantList(value) {
    this._cloudRegionTenantList = value;
  }

  static get globalCustomerId() {
    return this._globalCustomerId;
  }

  static set globalCustomerId(value) {
    this._globalCustomerId = value;
  }

  static get customizationUUID() {
    return this._customizationUUID;
  }

  static set customizationUUID(value) {
    this._customizationUUID = value;
  }

  static get rescustomizationUUID() {
    return this._rescustomizationUUID;
  }

  static set rescustomizationUUID(value) {
    this._rescustomizationUUID = value;
  }

  static get inventoryItem() {
    return this._inventoryItem;
  }

  static set inventoryItem(value) {
    this._inventoryItem = value;
  }

  static get modelId() {
    return this._modelId;
  }

  static set modelId(value) {
    this._modelId = value;
  }

  static get modelInstanceName() {
    return this._modelInstanceName;
  }

  static set modelInstanceName(value) {
    this._modelInstanceName = value;
  }

  static get modelInfo() {
    return this._modelInfo;
  }

  static set modelInfo(value) {
    this._modelInfo = value;
  }

  static getModelInfo(componentId) {
    return this._modelInfo[componentId];
  }

  static setModelInfo(componentId, modelInfo) {
    if (!this._modelInfo) {
      this._modelInfo = {};
    }
    this._modelInfo[componentId] = modelInfo;
  }

  static get networkInstanceId() {
    return this._networkInstanceId;
  }

  static set networkInstanceId(value) {
    this._networkInstanceId = value;
  }

  static get serviceIdList() {
    return this._serviceIdList;
  }

  static set serviceIdList(value) {
    this._serviceIdList = value;
  }

  static get aicZones() {
    return this._aicZones;
  }

  static set aicZones(value) {
    this._aicZones = value;
  }

  static get aicZone() {
    return this._aicZone;
  }

  static set aicZone(value) {
    this._aicZone = value;
  }

  static get serviceInstanceId() {
    return this._serviceInstanceId;
  }

  static set serviceInstanceId(value) {
    this._serviceInstanceId = value;
  }

  static get serviceInstanceName() {
    return this._serviceInstanceName;
  }

  static set serviceInstanceName(value) {
    this._serviceInstanceName = value;
  }

  static get serviceName() {
    return this._serviceName;
  }

  static set serviceName(value) {
    this._serviceName = value;
  }

  static get serviceType() {
    return this._serviceType;
  }

  static set serviceType(value) {
    this._serviceType = value;
  }

  static get serviceUuid() {
    return this._serviceUuid;
  }

  static set serviceUuid(value) {
    this._serviceUuid = value;
  }

  static get serviceTypeName() {
    return this._serviceTypeName;
  }

  static set serviceTypeName(value) {
    this._serviceTypeName = value;
  }

  static get createSubscriberName() {
    return this._createSubscriberName;
  }

  static set createSubscriberName(value) {
    this._createSubscriberName = value;
  }

  static get uploadSupplementoryDataFile() {
    return this._uploadSupplementoryDataFile;
  }

  static set uploadSupplementoryDataFile(value) {
    this._uploadSupplementoryDataFile = value;
  }

  static get supplementoryDataFile() {
    return this._supplementoryDataFile;
  }

  static set supplementoryDataFile(value) {
    this._supplementoryDataFile = value;
  }

  static get subscriberId() {
    return this._subscriberId;
  }

  static set subscriberId(value) {
    this._subscriberId = value;
  }

  static get loggedInUserId() {
    return this._loggedInUserId;
  }

  static set loggedInUserId(value) {
    this._loggedInUserId = value;
  }

  static get subscriberName() {
    return this._subscriberName;
  }

  static set subscriberName(value) {
    this._subscriberName = value;
  }

  static get subscribers() {
    return this._subscribers;
  }

  static set subscribers(value) {
    this._subscribers = value;
  }

  static get subscriptionServiceTypeList() {
    return this._subscriptionServiceTypeList;
  }

  static set subscriptionServiceTypeList(value) {
    this._subscriptionServiceTypeList = value;
  }

  static get userParams() {
    return this._userParams;
  }

  static set userParams(value) {
    this._userParams = value;
  }

  static get userServiceInstanceName() {
    return this._userServiceInstanceName;
  }

  static set userServiceInstanceName(value) {
    this._userServiceInstanceName = value;
  }

  static get vfModuleInstanceId() {
    return this._vfModuleInstanceId;
  }

  static set vfModuleInstanceId(value) {
    this._vfModuleInstanceId = value;
  }

  static get vnfInstanceId() {
    return this._vnfInstanceId;
  }

  static set vnfInstanceId(value) {
    this._vnfInstanceId = value;
  }

  static get vfModuleInstanceName() {
    return this._vfModuleInstanceName;
  }

  static set vfModuleInstanceName(value) {
    this._vfModuleInstanceName = value;
  }

  static get volumeGroupInstanceId() {
    return this._volumeGroupInstanceId;
  }

  static set volumeGroupInstanceId(value) {
    this._volumeGroupInstanceId = value;
  }

  static get lcpRegion() {
    return this._lcpRegion;
  }

  static set lcpRegion(value) {
    this._lcpRegion = value;
  }

  static get tenant() {
    return this._tenant;
  }

  static set tenant(value) {
    this._tenant = value;
  }

  static get treeHandle() {
    return this._treeHandle;
  }

  static set treeHandle(value) {
    this._treeHandle = value;
  }

  static get serviceInstanceToCustomer() {
    return this._serviceInstanceToCustomer;
  }

  static set serviceInstanceToCustomer(value) {
    this._serviceInstanceToCustomer = value;
  }

  static get aLaCarte() {
    if (!this._aLaCarte) {
      return true;
    }
    return this._aLaCarte;
  }

  static set aLaCarte(value: boolean) {
    this._aLaCarte = value;
  }

  static get macro() {
    if (!this._macro) {
      return false;
    }
    return this._macro;
  }

  static set macro(value: boolean) {
    this._macro = value;
  }

  static get resources() {
    return this._resources;
  }

  static set resources(value) {
    this._resources = value;
  }

  static get syspropProvStatusList() {
    return this._syspropProvStatusList;
  }

  static set syspropProvStatusList(value) {
    this._syspropProvStatusList = value;
  }

  static get updatedvnfProvStatus() {
    return this._updatedvnfProvStatus;
  }

  static set updatedvnfProvStatus(value) {
    this._updatedvnfProvStatus = value;
  }

  static get arbitraryParameters() {
    return this._arbitraryParameters;
  }

  static set arbitraryParameters(value) {
    this._arbitraryParameters = value;
  }

  static get hideServiceFields() {
    return this._hideServiceFields;
  }

  static set hideServiceFields(value) {
    this._hideServiceFields = value;
  }

  static get serviceProxies() {
    return this._serviceProxies;
  }

  static set serviceProxies(value) {
    this._serviceProxies = value;
  }

  static get sourceServiceProxies() {
    return this._sourceServiceProxies;
  }

  static set sourceServiceProxies(value) {
    this._sourceServiceProxies = value;
  }

  static get collectorServiceProxies() {
    return this._collectorServiceProxies;
  }

  static set collectorServiceProxies(value) {
    this._collectorServiceProxies = value;
  }

  static get configurationByPolicy() {
    return this._configurationByPolicy;
  }

  static set configurationByPolicy(value) {
    this._configurationByPolicy = value;
  }

  static get suppressRollback() {
    return this._suppressRollback;
  }

  static set suppressRollback(value) {
    this._suppressRollback = value;
  }

  static get portMirroningConfigFields() {
    return this._portMirroningConfigFields;
  }

  static set portMirroningConfigFields(value) {
    this._portMirroningConfigFields = value;
  }

  static get configurationInstanceId(): string {
    return this._configurationInstanceId;
  }

  static set configurationInstanceId(value: string) {
    this._configurationInstanceId = value;
  }

  static get configurationStatus(): string {
    return this._configurationStatus;
  }

  static set configurationStatus(value: string) {
    this._configurationStatus = value;
  }

  static get portStatus(): string {
    return this._portStatus;
  }

  static set portStatus(value: string) {
    this._portStatus = value;
  }

  static get portId(): string {
    return this._portId;
  }

  static set portId(value: string) {
    this._portId = value;
  }

  static get pnf() {
    return this._pnf;
  }

  static set pnf(value) {
    this._pnf = value;
  }

  static get owningEntityProperties() {
    return this._owningEntityProperties;
  }

  static set owningEntityProperties(value) {
    this._owningEntityProperties = value;
  }

}
