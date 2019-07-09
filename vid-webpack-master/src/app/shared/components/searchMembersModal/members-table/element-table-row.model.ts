import {VnfMember} from "../../../models/VnfMember";
import {Observable} from "rxjs";
import {CustomTableColumnDefinition} from "./elements-table.component";

export class ElementTableRowModel extends VnfMember {
  isSelected: boolean = false;
}


/*******************************************************************************************************************************
                ModalInformation
 *  @type: popup type (VPN, NETWORK, VNFGROUP)
 *  @title: popup title
 *  @description: popup upper message
 *  @topButtonText: (optional)
 *    @text: button text
 *    @action: button action
 *  @backAction : arrow back button action (can close the modal/move to next step)
 *  @uniqObjectField: uniq object field that we can find in O(1)
 *  @maxSelectRow: max number of row that user can select (default = no limit)(optional)
 *  @getElements: function that should return Observable<any[]> of collection of elements to show in the table
 *  @noElementsMsg : when there are no element some message should shown
 *  @searchFields : extra information in the left section
 *  @criteria: extra criteria on table content (optional)
 *  @tableHeaders : table headers
 *  @tableContent: table td's information.

 ******************************************************************************************************************************/

export class  ModalInformation {
  type : string;
  currentCriteriaInfo? : Object;
  title ?: string;
  description ?: string;
  topButton?: {
    text ?: string,
    action ?: (...args) => any
  };
  searchButton?: {
    text ?: string,
    action ?: (...args)=> any
  };
  backAction? : (...args) => any;
  uniqObjectField : string;
  maxSelectRow ?: number;
  getElements : (...args) => Observable<any[]>;
  noElementsMsg : string;
  searchFields: ISearchField[];
  criteria ?: ICriteria[];
  tableHeaders : CustomTableColumnDefinition[];
  tableContent : ITableContent[];
  serviceModelId: string;
}


export interface ISearchField {
  title: string;
  value: any;
  dataTestId: string;
  type : string;
}


export interface ICriteria {
  label: string;
  defaultValue: any;
  onInit?: (...args) => Observable<string>;
  onChange? : (...arg) => void;
  type : string;
  dataTestId : string;
  isRequired ?: boolean;
  currentValue ?: any;
}


export interface ITableContent {
  id : string;
  contents : {id : string[], value : string[], prefix ?: string, type? : string}[];
}

export enum SearchFieldItemType {
  LABEL = 'LABEL',
  DROPDOWN = 'DROPDOWN'
}




