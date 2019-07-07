import {Injectable} from "@angular/core";;
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../store/reducers";
import {DataFilterPipe} from "../../../pipes/dataFilter/data-filter.pipe";
import {ElementTableRowModel, ModalInformation} from "./element-table-row.model";
import {Level1Instance} from "../../../models/level1Instance";
import * as _ from 'lodash';
import {Subject} from "rxjs";
import {CustomTableColumnDefinition} from "./elements-table.component";
import {
  deleteGenericModalhelper,
  deleteGenericModalTableDataHelper,
  updateGenericModalhelper, updateGenericModalTableDataHelper
} from "../../../storeUtil/utils/global/global.actions";

@Injectable()
export class ElementsTableService {
  allElementsStatusMap : { [key:string]: ElementTableRowModel; };
  filteredMembers :  any[];
  allCheckboxAreSelected : boolean;
  numberOfNotHideRows : number;
  numberOfSelectedRows : number;
  numberOfSelectedAndNotHideRows : number;
  numberOfNotSelectedAndNotHideRows : number;
  maxSelectedRow : number;
  modalInformation : ModalInformation;

  static uniqObjectField : string;
  static changeFnTableDataTrigger : Subject<any> = new Subject();
  static changeModalInformationDataTrigger : Subject<{modalInformation, selectedRowsIds}> = new Subject();
  static selectRowsTrigger : Subject<string[]> = new Subject();

  get staticUniqObjectField() { return ElementsTableService.uniqObjectField; }

  constructor(private _store: NgRedux<AppState>, private dataFilter: DataFilterPipe){
    this.resetAll(ElementsTableService.uniqObjectField, this.maxSelectedRow);
  }

  updateAmountsAndCheckAll = (uniqObjectField: string, modalInformation : ModalInformation, maxSelectedRow? : number) : void => {
    this.maxSelectedRow = maxSelectedRow;
    this.modalInformation = modalInformation;
    ElementsTableService.uniqObjectField = uniqObjectField;
    this.numberOfSelectedRows = this.calculateSelectedRows();
    this.numberOfNotHideRows = this.calculateNotHideRows();
    this.numberOfSelectedAndNotHideRows = this.calculateSelectedAndNotHide();
    this.numberOfNotSelectedAndNotHideRows = this.calculateNotSelectedAndNotHide();
    this.allCheckboxAreSelected = this.numberOfNotHideRows > 0 && ((this.numberOfNotHideRows === this.numberOfSelectedAndNotHideRows) ||  (this.numberOfSelectedAndNotHideRows  === this.maxSelectedRow));
  };

  resetAll = (uniqObjectField: string, maxSelectedRow? : number) : void => {
    this.allElementsStatusMap = {};
    this.filteredMembers = [];
    this.numberOfSelectedRows = 0;
    this.numberOfNotHideRows = 0;
    this.numberOfSelectedAndNotHideRows = 0;
    this.numberOfNotSelectedAndNotHideRows = 0;
    this.allCheckboxAreSelected = false;
    this.maxSelectedRow = maxSelectedRow;
    ElementsTableService.uniqObjectField = uniqObjectField;
  };

  changeAllCheckboxStatus = (status : boolean) : void =>{
    for(const member of this.filteredMembers){
        this.allElementsStatusMap[member[this.modalInformation.uniqObjectField]].isSelected = status;
        if(status){
          this._store.dispatch(updateGenericModalhelper(`selected${this.modalInformation.type}`, this.allElementsStatusMap[member[this.modalInformation.uniqObjectField]], this.modalInformation.uniqObjectField));
        }else {
          this._store.dispatch(deleteGenericModalhelper(`selected${this.modalInformation.type}`,this.allElementsStatusMap[member[this.modalInformation.uniqObjectField]][this.modalInformation.uniqObjectField]));
        }
    }
    this.updateAmountsAndCheckAll(ElementsTableService.uniqObjectField, this.modalInformation, this.maxSelectedRow);
  };

  changeCheckboxStatus = (vnfInstanceId : string, tableData) : void => {
    if(_.isNil(this.allElementsStatusMap[vnfInstanceId].isSelected)){
      this.allElementsStatusMap[vnfInstanceId].isSelected = true;
      this._store.dispatch(updateGenericModalhelper(`selected${this.modalInformation.type}`, this.allElementsStatusMap[vnfInstanceId], this.modalInformation.uniqObjectField));
      this._store.dispatch(updateGenericModalTableDataHelper(`${this.modalInformation.type}_TABLE_DATA`, tableData));
    }else {
      this.allElementsStatusMap[vnfInstanceId].isSelected = !this.allElementsStatusMap[vnfInstanceId].isSelected;
      if(this.allElementsStatusMap[vnfInstanceId].isSelected){
        this._store.dispatch(updateGenericModalhelper(`selected${this.modalInformation.type}`, this.allElementsStatusMap[vnfInstanceId], this.modalInformation.uniqObjectField));
        this._store.dispatch(updateGenericModalTableDataHelper(`${this.modalInformation.type}_TABLE_DATA`, tableData));
      }else {
        this._store.dispatch(deleteGenericModalhelper(`selected${this.modalInformation.type}`, this.modalInformation.uniqObjectField));
        this._store.dispatch(deleteGenericModalhelper(`selected${this.modalInformation.type}`, vnfInstanceId));

        this._store.dispatch(deleteGenericModalTableDataHelper(`${this.modalInformation.type}_TABLE_DATA`));
      }
    }

    this.updateAmountsAndCheckAll(ElementsTableService.uniqObjectField, this.modalInformation,  this.maxSelectedRow);
  };

  filterMembers(searchStr: string, type :string): void {
    const keys: string[][] =  this.getDataKeys(type);
    const types :string[] = this.getDataType(type);
    this.filteredMembers = this.dataFilter.transform(_.values(this.allElementsStatusMap), searchStr || '', keys, types);
    this.updateAmountsAndCheckAll(ElementsTableService.uniqObjectField, this.modalInformation, this.maxSelectedRow);
  }

  /**************************************************
   generate elements data for select/ unselect rows
   **************************************************/
   generateAllMembersStatus(tableData : Level1Instance[]) : { [key:string]: ElementTableRowModel; }{
    tableData.map((item) => {
      item['isSelected'] = false
    });
    return _.keyBy(tableData as ElementTableRowModel[],this.staticUniqObjectField);
  }

   sortElementsByName(list : Level1Instance[], keyName : string) :Level1Instance[]{
    if(!_.isNil(list) && !_.isNil(keyName)) {
      return list.sort(function(itemA, itemB) { return itemA[keyName]- itemB[keyName];})
    }
    return [];
  }

  /********************************
   table columns headers and key's
   ********************************/
  static getHeaders(type: string) : CustomTableColumnDefinition[] {
    return  [
      {displayName: `${type} instance name`, key: ['instanceName']},
      {displayName: `${type} version`, key: ['modelInfo', 'modelVersion']},
      {displayName: `${type} model name`, key: ['modelInfo', 'modelName']},
      {displayName: 'Prov Status', key: ['provStatus']},
      {displayName: 'Service instance name', key: ['serviceInstanceName']},
      {displayName: 'Cloud Region', key: ['lcpCloudRegionId']},
      {displayName: 'Tenant Name', key: ['tenantName']}
    ];
  }

  getDataKeys(type: string): string[][]{
    const headers = (!_.isNil(this.modalInformation) && !_.isNil(this.modalInformation.tableHeaders)) ? this.modalInformation.tableHeaders : ElementsTableService.getHeaders(type);
    return headers.map((header)=> header.key).concat([[ElementsTableService.uniqObjectField]],[['serviceInstanceId']]);
  }

  getDataType(type: string): string[]{
    const headers = (!_.isNil(this.modalInformation) && !_.isNil(this.modalInformation.tableHeaders)) ? this.modalInformation.tableHeaders : ElementsTableService.getHeaders(type);
    return headers.map((header)=> header.type);

  }

  /*************************************************************************************
   calculate the number of selected vnf members - include not visible and visible rows
   @allElementsStatusMap: current vnf member status
   *************************************************************************************/
  calculateSelectedRows() : number {
    const flatObject = _.values(this.allElementsStatusMap);
    return  _.filter(flatObject, (item) => { if (item.isSelected) return item }).length;
  }

  /************************************************
   calculate the number of display vnf members
   @allElementsStatusMap: current vnf member status
   ************************************************/
  calculateNotHideRows() : number {
    return  this.filteredMembers ? this.filteredMembers.length : 0;
  }

  /************************************************
   calculate the number of display vnf members
   @allElementsStatusMap: current vnf member status
   ************************************************/
  calculateSelectedAndNotHide() : number {
    return  _.filter(this.filteredMembers, (item) => { if ( this.allElementsStatusMap[item[ElementsTableService.uniqObjectField]].isSelected) return item }).length;
  }

  calculateNotSelectedAndNotHide() : number {
    return  _.filter(this.filteredMembers, (item) => { if ( !this.allElementsStatusMap[item[ElementsTableService.uniqObjectField]].isSelected) return item }).length;
  }


  isRowDisabled(currentRowIsSelected : boolean, maxSelectRow?: number) : boolean {
    return _.isNil(maxSelectRow) || currentRowIsSelected || maxSelectRow === 1 ? false : maxSelectRow <= this.calculateSelectedRows();
  }

  isCheckAllDisabled(maxSelectRow?: number) : boolean{
    if(_.isNil(maxSelectRow)) return false;
    else {
      return this.numberOfNotSelectedAndNotHideRows  > maxSelectRow;
    }
  }


}
