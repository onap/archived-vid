import {Injectable} from "@angular/core";
import {VnfMember} from "../../../models/VnfMember";
import {CustomTableColumnDefinition} from "./members-table.component";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../store/reducers";
import {createRelatedVnfMemberInstance} from "../../../storeUtil/utils/relatedVnfMember/relatedVnfMember.actions";
import * as _ from 'lodash';
import {DataFilterPipe} from "../../../pipes/dataFilter/data-filter.pipe";
import {MemberTableRowModel} from "./member-table-row.model";

@Injectable()
export class MembersTableService {
  allMemberStatusMap : { [key:string]: MemberTableRowModel; };
  filteredMembers :  VnfMember[];
  allCheckboxAreSelected : boolean;
  numberOfNotHideVnfMembers : number;
  numberOfSelectedVnfMembers : number;
  numberOfSelectedAndNotHideVnfMembers : number;

  constructor(private _store: NgRedux<AppState>, private dataFilter: DataFilterPipe){
    this.resetAll();
  }

   filterUsedVnfMembers = (serviceModelId: string, result: VnfMember[]): VnfMember[] => {
    const allMembersMap =  _.keyBy(result as VnfMember[], 'instanceId');
    const vnfGroupsData = this._store.getState().service.serviceInstance[serviceModelId].vnfGroups;
    const vnfMembersArr = _.flatMap(vnfGroupsData).map((vnfGroup) =>vnfGroup.vnfs );
    for( let vnf of vnfMembersArr ){
      for(let member in vnf){
        delete allMembersMap[member];
      }
    }
    return _.flatMap(allMembersMap);
  };

  updateAmountsAndCheckAll = () : void => {
    this.numberOfSelectedVnfMembers = this.calculateSelectedVnfMembers();
    this.numberOfNotHideVnfMembers = this.calculateNotHideVnfMembers();
    this.numberOfSelectedAndNotHideVnfMembers = this.calculateSelectedAndNotHide();
    this.allCheckboxAreSelected = this.numberOfNotHideVnfMembers > 0 && this.numberOfNotHideVnfMembers === this.numberOfSelectedAndNotHideVnfMembers;
  };

  resetAll = () : void => {
    this.allMemberStatusMap = {};
    this.filteredMembers = [];
    this.numberOfSelectedVnfMembers = 0;
    this.numberOfNotHideVnfMembers = 0;
    this.numberOfSelectedAndNotHideVnfMembers = 0;
    this.allCheckboxAreSelected = false;
  };

  changeAllCheckboxStatus = (status : boolean) : void =>{
    for(const member of this.filteredMembers){
        this.allMemberStatusMap[member.instanceId].isSelected = status;
      }
    this.updateAmountsAndCheckAll();
  };

  changeCheckboxStatus = (vnfInstanceId : string ) : void =>{
    this.allMemberStatusMap[vnfInstanceId].isSelected = !this.allMemberStatusMap[vnfInstanceId].isSelected;
    this.updateAmountsAndCheckAll();
  };

  /************************************************
   iterate over all current vnf members:
   1) if vnf member is selected then update REDUX store
   2) if vnf member is not selected then delete member
   @allMemberStatusMap: current vnf member status
   @vnfGroupStoreKey: vnf group store key
   @serviceId: service model id
   ************************************************/
  setMembers = (data : {serviceId : string, vnfGroupStoreKey : string}) : void =>{
    let tmpMembers = this.allMemberStatusMap;
    for(let key in tmpMembers){
      if(tmpMembers[key].isSelected){
        this._store.dispatch(createRelatedVnfMemberInstance( data.vnfGroupStoreKey, data.serviceId, tmpMembers[key]));
      }
    }
  };

  filterMembers(searchStr: string): void {
    const keys: string[][] =  MembersTableService.getDataKeys();
    this.filteredMembers = this.dataFilter.transform(_.values(this.allMemberStatusMap), searchStr || '', keys);
    this.updateAmountsAndCheckAll();
  }

  /************************************
   generate  vnf member data for select/ unselect rows
   ************************************/
  static generateAllMembersStatus(tableData : VnfMember[]) : { [key:string]: MemberTableRowModel; }{

    tableData.map((vnf) => {
      vnf['isSelected'] = false
    });
    return _.keyBy(tableData as MemberTableRowModel[], 'instanceId');
  }


  static sortVnfMembersByName(list : VnfMember[], keyName : string) :VnfMember[]{
    if(!_.isNil(list) && !_.isNil(keyName)) {
      return list.sort(function(itemA, itemB) { return itemA[keyName]- itemB[keyName];})
    }
    return [];

  }

  /********************************
   table columns headers and key's
   ********************************/
  static getHeaders() : CustomTableColumnDefinition[] {
    return  [
      {displayName: 'VNF instance name', key: ['instanceName']},
      {displayName: 'VNF version', key: ['modelInfo', 'modelVersion']},
      {displayName: 'VNF model name', key: ['modelInfo', 'modelName']},
      {displayName: 'Prov Status', key: ['provStatus']},
      {displayName: 'Service instance name', key: ['serviceInstanceName']},
      {displayName: 'Cloud Region', key: ['lcpCloudRegionId']},
      {displayName: 'Tenant Name', key: ['tenantName']}
    ];
  }

  static getDataKeys(): string[][]{
    const headers = MembersTableService.getHeaders();
    return headers.map((header)=> header.key).concat([['instanceId']],[['serviceInstanceId']]);
  }

  /*************************************************************************************
   calculate the number of selected vnf members - include not visible and visible rows
   @allMemberStatusMap: current vnf member status
   *************************************************************************************/
  calculateSelectedVnfMembers() : number {
    const flatObject = _.values(this.allMemberStatusMap);
    return  _.filter(flatObject, (item) => { if (item.isSelected) return item }).length;
  }

  /************************************************
   calculate the number of display vnf members
   @allMemberStatusMap: current vnf member status
   ************************************************/
  calculateNotHideVnfMembers() : number {
    return this.filteredMembers.length;
  }

  /************************************************
   calculate the number of display vnf members
   @allMemberStatusMap: current vnf member status
   ************************************************/
  calculateSelectedAndNotHide() : number {
    return  _.filter(this.filteredMembers, (item) => { if ( this.allMemberStatusMap[item.instanceId].isSelected) return item }).length;
  }


}
