import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {NgRedux, select} from "@angular-redux/store";
import {AppState} from "../../shared/store/reducers";
import {ModelInformationItem} from "../../shared/components/model-information/model-information.component";
import {Observable} from "rxjs";
import {NgForm} from "@angular/forms";
import * as _ from 'lodash';

@Component({
  selector: "app-network-selector",
  templateUrl: "./network-selector.component.html",
  styleUrls: ["./network-selector.component.scss"]
})
export class NetworkSelectorComponent implements OnInit {
  private localStore: NgRedux<AppState>;
  networkSelection: any;
  groupSelection :any;
  @Input() groups: Array<string>;
  @Input() cloudOwner : string;
  @Input() cloudRegionId : string;
  @select(['service', 'networkFunctions'])
  readonly networkFunctions: Observable<any>;

  @ViewChild('form', {static: false}) form: NgForm;

  constructor(store: NgRedux<AppState>) {
    this.localStore = store;
    this.groupSelection = {};
  }

  getValueOfLabelInGroup(group: any, label: string){
    let item = _.find(group, {"label": label});
    if (item) {
      return item["values"][0];
    }
  }

  getNetworksByNetworkFunctionRole(group: any): Observable<string[]> {
    let filteredItem = this.getValueOfLabelInGroup(group,"Network Collection Function");
    return this.networkFunctions.map(data => {
      return (data && data[filteredItem]) || [];
    });
  }

  getGroupName(group: ModelInformationItem[]) {
    return this.getValueOfLabelInGroup(group,"Group Name");
  }

  /**
   * [ncf=>aaiResonse]
   * groups => groupsModel
   */

  ngOnInit() {
    let store = this.localStore;
    this.networkSelection = {};

  }
}


//
/*
Info inside redux = {ncfNetworkMap }
*/
