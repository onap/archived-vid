import {NgModule, ModuleWithProviders} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ServiceInfoService } from './server/serviceInfo/serviceInfo.service';
import { PopoverModule } from 'ngx-bootstrap';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { SvgDirective } from './directives/svg/svg.directive';
import { PopoverComponent } from './components/popover/popover.component';
import { EllipsisComponent } from './components/ellipsis/ellipsis.component';
import { MessageBoxComponent } from './components/messageBox/messageBox.component';
import { MessageBoxService } from './components/messageBox/messageBox.service';
import { SdcUiComponentsModule , SdcUiComponents} from 'sdc-ui/lib/angular';
import { HttpInterceptorService } from './utils/httpInterceptor/httpInterceptor.service';
import { FormControlErrorComponent } from './components/formControlError/formControlError.component';
import { InputPreventionPatternDirective } from './directives/inputPrevention/inputPreventionPattern.directive';
import { FormGeneralErrorsComponent } from './components/formGeneralErrors/formGeneralErrors.component';
import { NumbersLettersUnderscoreValidator } from './components/validators/numbersLettersUnderscore/numbersLettersUnderscore.validator';
import { SpinnerComponent } from './components/spinner/spinner.component';
import { NoContentMessageAndIconComponent } from './components/no-content-message-and-icon/no-content-message-and-icon.component';
import { ModelInformationComponent } from './components/model-information/model-information.component';
import { TooltipModule } from 'ngx-tooltip';
import {IframeService} from "./utils/iframe.service";
import {CapitalizeAndFormatPipe} from "./pipes/capitalize/capitalize-and-format.pipe";
import { DefaultDataGeneratorService } from './services/defaultDataServiceGenerator/default.data.generator.service';
import {ServiceInfoPipe} from "./pipes/serviceInfo/serviceInfo.pipe";
import {HealthStatusService} from "./server/healthStatusService/health-status.service";
import {ConfigurationService} from "../services/configuration.service";
import {FlagsResolve} from "../services/flags.resolve";


@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule,
    CommonModule,
    RouterModule,
    PopoverModule.forRoot(),
    AngularSvgIconModule,
    TooltipModule,
    SdcUiComponentsModule,
  ],
  declarations: [
    PopoverComponent,
    SvgDirective,
    EllipsisComponent,
    MessageBoxComponent,
    FormControlErrorComponent,
    InputPreventionPatternDirective,
    FormGeneralErrorsComponent,
    SpinnerComponent,
    NoContentMessageAndIconComponent,
    ModelInformationComponent,
    CapitalizeAndFormatPipe,
    ServiceInfoPipe,
  ],
  exports: [
    PopoverComponent,
    SvgDirective,
    EllipsisComponent,
    MessageBoxComponent,
    FormControlErrorComponent,
    InputPreventionPatternDirective,
    FormGeneralErrorsComponent,
    SpinnerComponent,
    NoContentMessageAndIconComponent,
    ModelInformationComponent,
    CapitalizeAndFormatPipe,
    ServiceInfoPipe,
  ],
  providers: [
    ServiceInfoService,
    MessageBoxService,
    SdcUiComponents.ModalService,
    HttpInterceptorService,
    IframeService,
    NumbersLettersUnderscoreValidator,
    DefaultDataGeneratorService,
    HealthStatusService,
    ConfigurationService,
    FlagsResolve
  ]
})
export class SharedModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: SharedModule,
      providers: [MessageBoxService]
    };
  }
}
