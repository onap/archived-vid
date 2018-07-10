import {Component, Input} from '@angular/core';
import * as _ from 'lodash';

@Component({
  selector: 'model-information',
  templateUrl: 'model-information.html',
  styleUrls: ['model-information.scss']
})

export class ModelInformationComponent {
  private _modelInformationItems: Array<ModelInformationItem>;


  get modelInformationItems(): Array<ModelInformationItem> {
    return this._modelInformationItems;
  }

  @Input()
  set modelInformationItems(_modelInformationItems: Array<ModelInformationItem>) {
    if (_modelInformationItems) {
      this._modelInformationItems = _modelInformationItems.filter(x => x.mandatory || (!_.isEmpty(x.values) && !_.isEmpty(x.values[0])));
    }
  }
}


export class ModelInformationItem {
  label: string;
  testsId: string;
  values: Array<string>;
  toolTipText: string;
  mandatory: boolean;

  constructor(label: string, testsId: string, values: Array<any>, toolTipText: string = "", mandatory: boolean = false,nested:boolean=false) {
    this.label = label;
    this.testsId = testsId;
    this.values = values;
    this.toolTipText = toolTipText;
    this.mandatory = mandatory;
  }

}
