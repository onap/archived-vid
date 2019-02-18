import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BrowserModule} from "@angular/platform-browser";
import {HttpClientModule} from "@angular/common/http";
import {FeatureFlagService} from "./service/featureFlag.service";
import {BasicFeatureFlagDirective} from "./directive/basic/basic.featureFlag.directive";


@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule,
    CommonModule
  ],
  declarations: [
    BasicFeatureFlagDirective
  ],
  exports: [
    BasicFeatureFlagDirective
  ],
  providers: [
    FeatureFlagService
  ]
})
export class FeatureFlagModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: FeatureFlagModule,
      providers: []
    };
  }
}
