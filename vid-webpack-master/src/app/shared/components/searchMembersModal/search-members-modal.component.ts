import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {DialogComponent, DialogService} from "ng2-bootstrap-modal";
import {IframeService} from "../../utils/iframe.service";
import {AaiService} from "../../services/aaiService/aai.service";
import {VnfMember} from "../../models/VnfMember";
import {VnfGroupModel} from "../../models/vnfGroupModel";
import {MembersTableService} from "./members-table/members-table.service";
import {VnfGroupInstance} from "../../models/vnfGroupInstance";


export interface PopupModel {
  title: string;
  serviceModelId : string;
  searchFields: ISearchField[];
  description : string;
  subscriberId: string,
  serviceType: string,
  node: VnfGroupInstance,
  vnfGroupModel: VnfGroupModel;

}

export interface ISearchField {
  title: string;
  value: any;
  dataTestId: string;
}

@Component({
  selector : 'search-members-modal',
  templateUrl : 'search-members-modal.component.html',
  styleUrls : ['search-members-modal.component.scss']
})

export class SearchMembersModalComponent extends DialogComponent<PopupModel, boolean> implements OnInit, OnDestroy {
  title: string;
  serviceModelId : string;
  parentElementClassName = 'content';
  membersData: VnfMember[];
  description : string;
  searchFields: ISearchField[];
  vnfGroupModel: VnfGroupModel;
  subscriberId: string;
  serviceType: string;
  node: VnfGroupInstance;
  disableSetMembers: boolean = true;
    constructor(dialogService:  DialogService ,
              private _iframeService : IframeService,
              private _aaiService : AaiService,
              private _membersTableService: MembersTableService){
    super(dialogService);

  }
  @ViewChild('MembersTableComponent') membersTable;

   ngOnInit() : void{
    this._aaiService.getOptionalGroupMembers(this.serviceModelId, this.subscriberId, this.serviceType, (Object.values(this.vnfGroupModel.members))[0].sourceModelInvariant, this.vnfGroupModel.properties.type, this.vnfGroupModel.properties.role)
      .subscribe((result: VnfMember[])=>{
        this.membersData = this._membersTableService.filterUsedVnfMembers(this.serviceModelId, result);
      });
    };


  closeDialog() : void{
    this._iframeService.removeClassCloseModal(this.parentElementClassName);
    this.dialogService.removeDialog(this);
    setTimeout(() => {
      window.parent.postMessage("closeIframe", "*");
    }, 15);
  }
  selectedMembersAmountChange(selectedMembersAmount: number) : void {
    this.disableSetMembers = selectedMembersAmount==0;
  }


  setMembers() : void {
    this._membersTableService.setMembers({serviceId : this.serviceModelId, vnfGroupStoreKey : this.node.vnfGroupStoreKey});
    this.closeDialog();
  }
}


