import {Injectable} from "@angular/core";
import * as _ from 'lodash';
import {ModelInformationItem} from "./model-information.component";

@Injectable()
export class ModelInformationService {

  filterModelItems(_modelInformationItems: ModelInformationItem[]) {
    return _modelInformationItems.filter(x => x.mandatory || (
      !_.isEmpty(x.values)
      && !_.isNil(x.values[0])
      && x.values[0].toString().trim()!=""
      ));
  }
}

