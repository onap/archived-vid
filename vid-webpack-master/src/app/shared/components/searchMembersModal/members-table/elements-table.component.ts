import {Component, Input, OnChanges, Output, SimpleChanges, EventEmitter} from '@angular/core';
import {ElementsTableService} from "./elements-table.service";
import {ModalInformation} from "./element-table-row.model";
import * as _ from 'lodash';
import {Level1Instance} from "../../../models/level1Instance";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../store/reducers";
import {
  deleteGenericModalhelper,
  deleteGenericModalTableDataHelper
} from "../../../storeUtil/utils/global/global.actions";

export class CustomTableColumnDefinition {
  public displayName = '';
  public key : any = '';
  public type? = 'text';
  public filter? = '';
}

@Component({
  selector: 'app-members-table',
  templateUrl: './elements-table.component.html',
  styleUrls: ['./members-table.component.scss']
})

export class ElementsTableComponent implements OnChanges{
  filterValue: string = null;
  allMemberStatusMap = null;
  membersTableService : ElementsTableService;
  headers: CustomTableColumnDefinition[] = [];
  searchQuery = null;

  @Input() modalInformation : ModalInformation;
  @Input() data: Level1Instance[];
  @Output() selectedMembersAmountChange : EventEmitter<number> = new EventEmitter();
  constructor(private _membersTableService : ElementsTableService, private _store : NgRedux<AppState>){
    this.membersTableService = this._membersTableService;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(_.isNil(this.data)){
      this._membersTableService.resetAll(this.modalInformation.uniqObjectField, this.modalInformation.maxSelectRow);
    }else {
      ElementsTableService.uniqObjectField = this.modalInformation.uniqObjectField;
      this.headers = this.modalInformation.tableHeaders;
      const genericModalHelper = this._store.getState().global.genericModalHelper;
      if(!_.isNil(genericModalHelper) && !_.isNil(genericModalHelper[`${this.modalInformation.type}_TABLE_DATA`]) && !_.isNil(genericModalHelper[`selected${this.modalInformation.type}`])){
        this.updateTablWithDefaultData(this._store.getState().global.genericModalHelper[`${this.modalInformation.type}_TABLE_DATA`]);
      }else {
        this.modalInformation.getElements().subscribe((res)=>{
          this.updateTablWithDefaultData(res);
        });
      }
    }
  }

  updateTablWithDefaultData(tableData) : void{
    this._membersTableService.allElementsStatusMap = this._membersTableService.generateAllMembersStatus(tableData);
    this._membersTableService.filteredMembers = this._membersTableService.sortElementsByName(tableData, "instanceName");
    this._membersTableService.updateAmountsAndCheckAll(this.modalInformation.uniqObjectField, this.modalInformation, this.modalInformation.maxSelectRow);
    this.updateDefaultSelectedRows();
  }
  
  search(searchStr: string): void {
    this.filterValue = searchStr;
    this._membersTableService.filterMembers(this.filterValue, this.modalInformation.type);
  }

  selectItem(item , maxNumberOfRows : number) : void {
    if (maxNumberOfRows === 1) {
      for (let currentItem in this.membersTableService.allElementsStatusMap) {
        if (this.membersTableService.allElementsStatusMap[currentItem].isSelected) {
          this.membersTableService.allElementsStatusMap[currentItem].isSelected = false;
          this.membersTableService.allElementsStatusMap[item[this.membersTableService.staticUniqObjectField]].isSelected = !this.membersTableService.allElementsStatusMap[item[this.membersTableService.staticUniqObjectField]].isSelected;
          return;
        }
      }
      this.membersTableService.allElementsStatusMap[item[this.membersTableService.staticUniqObjectField]].isSelected = !this.membersTableService.allElementsStatusMap[item[this.membersTableService.staticUniqObjectField]].isSelected;
    }
  }

  updateDefaultSelectedRows(): void {
    if(this._store.getState().global.genericModalHelper && this._store.getState().global.genericModalHelper[`selected${this.modalInformation.type}`]){
      const selectedIds = this._store.getState().global.genericModalHelper[`selected${this.modalInformation.type}`];
      for(const id in selectedIds){
        if(!_.isNil(this._membersTableService.allElementsStatusMap[id])){
          this._membersTableService.allElementsStatusMap[id].isSelected = true;
        }
      }
      this._membersTableService.updateAmountsAndCheckAll(this.modalInformation.uniqObjectField, this.modalInformation, this.modalInformation.maxSelectRow);
      this.selectedMembersAmountChange.emit(this._membersTableService.numberOfSelectedRows);
    }
  }

  changeAllCheckboxStatus(status: boolean) : void {
    this._membersTableService.changeAllCheckboxStatus(status);
    this.selectedMembersAmountChange.emit(this._membersTableService.numberOfSelectedRows);
  }


  changeCheckboxStatus(vnfInstanceId: string) : void {
    if (this.modalInformation.maxSelectRow === 1) {
      for (let currentItem in this.membersTableService.allElementsStatusMap) {
        if (this.membersTableService.allElementsStatusMap[currentItem].isSelected) {
          this.membersTableService.allElementsStatusMap[currentItem].isSelected = false;
          this._store.dispatch(deleteGenericModalhelper(`selected${this.modalInformation.type}`, this.membersTableService.allElementsStatusMap[currentItem][this.modalInformation.uniqObjectField]));
          this._store.dispatch(deleteGenericModalTableDataHelper(`${this.modalInformation.type}_TABLE_DATA`));
        }
      }
    }
    this._membersTableService.changeCheckboxStatus(vnfInstanceId, this.data);
    this.selectedMembersAmountChange.emit(this._membersTableService.numberOfSelectedRows);
  }


  getTdInformationItemId(data : {id : string[], value : string[], prefix ?: string}, item) : string {
    let result = item;
    for(const idVal of data.id){
      if(_.isNil(result)) return null;
      result = result[idVal];
    }
    return result;
  }

  getTdInformationItemValue(data : {id : string[], value : string[], prefix ?: string}, item) : string {
    let result = item;
    for(const idVal of data.value){
      if(_.isNil(result)) return null;
      result = result[idVal];
    }
    return !_.isNil(data.prefix) ? data.prefix + result : result;
  }


  getTdListInformationItemValue(data : {id : string[], value : string[], prefix ?: string}, item) : string[] {
    let result = item;

    for(let i = 0 ; i < data.value.length -1 ; i++){
      if(_.isNil(result)) return null;
      result = result[data.value[i]];
    }
    return _.map(result, _.last(data.value));
  }
}
