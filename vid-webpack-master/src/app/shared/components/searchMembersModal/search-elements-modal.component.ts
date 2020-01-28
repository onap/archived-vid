import {Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {DialogComponent, DialogService} from "ng2-bootstrap-modal";
import {IframeService} from "../../utils/iframe.service";
import {AaiService} from "../../services/aaiService/aai.service";
import {VnfGroupModel} from "../../models/vnfGroupModel";
import {ElementsTableService} from "./members-table/elements-table.service";
import {Level1Instance} from "../../models/level1Instance";
import {ModalInformation} from "./members-table/element-table-row.model";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {FormGroup} from "@angular/forms";
import * as _ from "lodash";
import {clearAllGenericModalhelper} from "../../storeUtil/utils/global/global.actions";
@Component({
  selector: 'search-members-modal',
  templateUrl: 'search-elements-modal.component.html',
  styleUrls: ['search-elements-modal.component.scss']
})

export class SearchElementsModalComponent extends DialogComponent<{ modalInformation: ModalInformation }, boolean> implements OnInit, OnDestroy {
  modalInformation: ModalInformation;
  parentElementClassName = 'content';
  elementsData: Level1Instance[];
  vnfGroupModel: VnfGroupModel;
  disableSetElements: boolean = true;
  disableSearchByNetworkRole: boolean = false;
  dynamicFormGroup: FormGroup = null;
  isIframe : boolean = true;
  modalIsOpen : boolean = false;

  constructor(dialogService: DialogService,
              private _iframeService: IframeService,
              private _aaiService: AaiService,
              private _membersTableService: ElementsTableService,
              private _store: NgRedux<AppState>) {
    super(dialogService);
    ElementsTableService.changeFnTableDataTrigger.subscribe((triggerRes) => {
      this._membersTableService.resetAll(this.modalInformation.uniqObjectField, this.modalInformation.maxSelectRow);
      this.elementsData = triggerRes;
    });

    ElementsTableService.changeModalInformationDataTrigger.subscribe(({modalInformation, selectedRowsIds}) => {
      this.disableSetElements = true;
      this.modalInformation = modalInformation;
      this.ngOnInit(selectedRowsIds);
    })
  }

  @ViewChild('ElementsTableComponent', {static: false}) membersTable;

  ngOnInit(selectedRowsIds?: string[]): void {
    this.isIframe = IframeService.isIframe();
    const genericModalHelper = this._store.getState().global.genericModalHelper;
    if(!_.isNil(genericModalHelper) && !_.isNil(genericModalHelper[`${this.modalInformation.type}_TABLE_DATA`]) && !_.isNil(genericModalHelper[`selected${this.modalInformation.type}`])){
      this.elementsData = this._store.getState().global.genericModalHelper[`${this.modalInformation.type}_TABLE_DATA`];
    } else {
      this.modalInformation.getElements()
        .subscribe((result) => {
          this.elementsData = result;
        });
    }
  };

  closeDialog(): void {
    this._iframeService.removeFullScreen();
    this._iframeService.closeIframe(this.dialogService, this);
  }

  selectedMembersAmountChange(selectedMembersAmount: number): void {
    this.disableSetElements = selectedMembersAmount == 0;
  }

  doneAction(): void {
    this.modalInformation.topButton.action.call(this, this);
  }

  searchByCriteriaAction(): void {
    this.modalInformation.searchButton.action.call(this, this);
  }

  backAction(): void {
    if (this.modalInformation.backAction) {
      this.modalInformation.backAction.call(this, this);
    } else {
      this.closeDialog();
    }
  }
}


