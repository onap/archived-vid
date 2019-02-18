import {Component, Input} from '@angular/core';
import {ModelInformationService} from "./model-information.service";


@Component({
  selector: 'model-information',
  templateUrl: 'model-information.html',
  styleUrls: ['model-information.scss']
})

export class ModelInformationComponent {

  constructor(private _modelInformationService : ModelInformationService){}

  private _modelInformationItems: ModelInformationItem[];

  @Input() itemClass: string = 'item'; //default class for item is "item"

  get modelInformationItems(): ModelInformationItem[] {
    return this._modelInformationItems;
  }

  @Input()
  set modelInformationItems(_modelInformationItems: ModelInformationItem[]) {
    if (_modelInformationItems) {
      this._modelInformationItems = this._modelInformationService.filterModelItems(_modelInformationItems);
    }
  }
}


export class ModelInformationItem {
  label: string;
  testsId: string;
  values: string[];
  toolTipText: string;
  mandatory: boolean;

  constructor(label: string, testsId: string, values: any[], toolTipText: string = "", mandatory: boolean = false) {
    this.label = label;
    this.testsId = testsId;
    this.values = values;
    this.toolTipText = toolTipText;
    this.mandatory = mandatory;
  }

  static createInstance(label: string, value: any):ModelInformationItem {
    return new ModelInformationItem(label, label, [value]);
  }

}
