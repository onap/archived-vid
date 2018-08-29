import {Component, Input, OnChanges, Output, SimpleChanges, EventEmitter} from '@angular/core';
import {VnfMember} from "../../../models/VnfMember";
import {MembersTableService} from "./members-table.service";
import * as _ from 'lodash';

export class CustomTableColumnDefinition {
  public displayName = '';
  public key : any = '';
  public type? = 'text';
  public filter? = '';
}

@Component({
  selector: 'app-members-table',
  templateUrl: './members-table.component.html',
  styleUrls: ['./members-table.component.scss']
})

export class MembersTableComponent implements OnChanges{
  filterValue: string = null;
  allMemberStatusMap = null;
  membersTableService : MembersTableService;
  headers: CustomTableColumnDefinition[] = MembersTableService.getHeaders();
  @Input() data: VnfMember[];
  @Input() description: string;
  @Output() selectedMembersAmountChange : EventEmitter<number> = new EventEmitter();
  constructor(private _membersTableService : MembersTableService){
    this.membersTableService = this._membersTableService;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(_.isNil(this.data)){
      this._membersTableService.resetAll();
    }else {
      this._membersTableService.allMemberStatusMap = MembersTableService.generateAllMembersStatus(this.data);
      this._membersTableService.filteredMembers = MembersTableService.sortVnfMembersByName(this.data, "instanceName");
      this._membersTableService.updateAmountsAndCheckAll();
    }
  }
  
  search(searchStr: string): void {
    this.filterValue = searchStr;
    this._membersTableService.filterMembers(this.filterValue);
  }

  changeAllCheckboxStatus(status: boolean) : void {
    this._membersTableService.changeAllCheckboxStatus(status);
    this.selectedMembersAmountChange.emit(this._membersTableService.numberOfSelectedVnfMembers);
  }


  changeCheckboxStatus(vnfInstanceId: string) : void {
    this._membersTableService.changeCheckboxStatus(vnfInstanceId);
    this.selectedMembersAmountChange.emit(this._membersTableService.numberOfSelectedVnfMembers);
  }

}
