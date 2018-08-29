import {AfterContentChecked, Directive, ElementRef, Input} from '@angular/core';
import {FeatureFlagService} from "../../service/featureFlag.service";
import * as _ from 'lodash';

/************************************************************************
    Feature Flag Directive
    Example:
    <div featureFlag [flagName]='"<flag name>"'></div>
 ************************************************************************/
@Directive({
  selector: '[featureFlag]'
})
export class BasicFeatureFlagDirective implements AfterContentChecked {
  @Input() flagName: string;
  element: ElementRef;

  constructor(el: ElementRef, private _featureToggleService: FeatureFlagService) {
    this.element = el;
  }

  ngAfterContentChecked(): void {
    if (!_.isNil(this.element)) {
      const isFeatureOn: boolean = this._featureToggleService.isFeatureOn(this.flagName);
        if(!isFeatureOn){
          this._featureToggleService.hideElement(this.element)
        }
    }
  }
}
