import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {RouterModule} from '@angular/router';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {ServiceInfoService} from './server/serviceInfo/serviceInfo.service';
import {ModalModule, PopoverModule} from 'ngx-bootstrap';
import {PopoverComponent} from './components/popover/popover.component';
import {EllipsisComponent} from './components/ellipsis/ellipsis.component';
import {MessageBoxComponent} from './components/messageBox/messageBox.component';
import {MessageBoxService} from './components/messageBox/messageBox.service';
import {HttpInterceptorService} from './utils/httpInterceptor/httpInterceptor.service';
import {FormControlErrorComponent} from './components/formControlError/formControlError.component';
import {DropdownFormControlComponent} from "./components/formControls/component/dropdown/dropdown.formControl.component";
import {InputPreventionPatternDirective} from './directives/inputPrevention/inputPreventionPattern.directive';
import {FormGeneralErrorsComponent} from './components/formGeneralErrors/formGeneralErrors.component';
import {SpinnerComponent} from './components/spinner/spinner.component';
import {NoContentMessageAndIconComponent} from './components/no-content-message-and-icon/no-content-message-and-icon.component';
import {ModelInformationComponent} from './components/model-information/model-information.component';
import {TooltipModule} from 'ngx-tooltip';
import {IframeService} from "./utils/iframe.service";
import {CapitalizeAndFormatPipe} from "./pipes/capitalize/capitalize-and-format.pipe";
import {DefaultDataGeneratorService} from './services/defaultDataServiceGenerator/default.data.generator.service';
import {ServiceInfoPipe} from "./pipes/serviceInfo/serviceInfo.pipe";
import {ConfigurationService} from "./services/configuration.service";
import {InputFormControlComponent} from "./components/formControls/component/input/input.formControl.component";
import {MultiselectFormControlComponent} from "./components/formControls/component/multiselect/multiselect.formControl.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {FormControlMessageErrorComponent} from "./components/formControls/errorMessage/formControlMessageError.component";
import {GenericFormPopupComponent} from "./components/genericFormPopup/generic-form-popup.component";
import {CheckboxFormControlComponent} from "./components/formControls/component/checkbox/checkbox.formControl.component";
import {GenericFormService} from "./components/genericForm/generic-form.service";
import {GenericFormComponent} from "./components/genericForm/generic-form.component";
import {ServiceControlGenerator} from "./components/genericForm/formControlsServices/service.control.generator";
import {ControlGeneratorUtil} from "./components/genericForm/formControlsServices/control.generator.util.service";
import {CustomValidators} from "./validators/uniqueName/uniqueName.validator";
import {FileFormControlComponent} from "./components/formControls/component/file/file.formControl.component";
import {NumberFormControlComponent} from "./components/formControls/component/number/number.formControl.component";
import {AngularMultiSelectModule} from 'angular2-multiselect-dropdown';
import {VnfControlGenerator} from "./components/genericForm/formControlsServices/vnfGenerator/vnf.control.generator";
import {NetworkPopupService} from "./components/genericFormPopup/genericFormServices/network/network.popup.service";
import {NetworkControlGenerator} from "./components/genericForm/formControlsServices/networkGenerator/network.control.generator";
import {BasicPopupService} from "./components/genericFormPopup/genericFormServices/basic.popup.service";
import {VfModulePopupService} from "./components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {VfModuleUpgradePopupService} from "./components/genericFormPopup/genericFormServices/vfModuleUpgrade/vfModule.upgrade.popuop.service";
import {VfModuleControlGenerator} from "./components/genericForm/formControlsServices/vfModuleGenerator/vfModule.control.generator";
import {OrderByPipe} from "./pipes/order/orderBy.pipe";
import {ServicePopupService} from "./components/genericFormPopup/genericFormServices/service/service.popup.service";
import {GenericFormPopupService} from "./components/genericFormPopup/generic-form-popup.service";
import {FormGeneralErrorsService} from "./components/formGeneralErrors/formGeneralErrors.service";
import {VnfPopupService} from "./components/genericFormPopup/genericFormServices/vnf/vnf.popup.service";
import {SafePipe} from "./pipes/safe/safe.pipe";
import {ViewEditResolver} from "./resolvers/viewEdit/viewEdit.resolver";
import {FlagsResolve} from "./resolvers/flag/flag.resolver";
import {FeatureFlagModule} from "../featureFlag/featureFlag.module";
import {VnfGroupPopupService} from "./components/genericFormPopup/genericFormServices/vnfGroup/vnfGroup.popup.service";
import {VnfGroupControlGenerator} from "./components/genericForm/formControlsServices/vnfGroupGenerator/vnfGroup.control.generator";
import {AuditInfoModalComponent} from "./components/auditInfoModal/auditInfoModal.component";
import {BootstrapModalModule} from 'ng2-bootstrap-modal';
import {DataTableModule} from "angular2-datatable";
import {AuditInfoModalComponentService} from "./components/auditInfoModal/auditInfoModal.component.service";
import {SearchElementsModalComponent} from "./components/searchMembersModal/search-elements-modal.component";
import {ElementsTableComponent} from "./components/searchMembersModal/members-table/elements-table.component";
import {ElementsTableService} from "./components/searchMembersModal/members-table/elements-table.service";
import {ObjectToArrayPipe} from "./pipes/objectToArray/objectToArray.pipe";
import {DataFilterPipe} from "./pipes/dataFilter/data-filter.pipe";
import {SvgComponent} from "./components/svg/svg-component";
import {ErrorMsgComponent} from './components/error-msg/error-msg.component';
import {ErrorMsgService} from "./components/error-msg/error-msg.service";
import {RetryResolver} from "./resolvers/retry/retry.resolver";
import {ClickOutsideDirective} from "./directives/clickOutside/clickOutside.directive";
import {DynamicInputsComponent} from "./components/dynamic-inputs/dynamic-inputs.component";
import {DynamicInputLabelPipe} from "./pipes/dynamicInputLabel/dynamic-input-label.pipe";
import {ModelInformationService} from "./components/model-information/model-information.service";
import {MultiselectFormControlService} from "./components/formControls/component/multiselect/multiselect.formControl.service";
import {InstantiationTemplatesModalComponent} from "./components/genericFormPopup/instantiationTemplatesModal/instantiation.templates.modal.component";
import {InstantiationTemplatesModalService} from "./components/genericFormPopup/instantiationTemplatesModal/instantiation.templates.modal.service";
import {SearchFilterPipe} from "./pipes/searchFilter/search-filter.pipe";
import {RecreateResolver} from "./resolvers/recreate/recreate.resolver";
import {InstantiationTemplatesService} from "./services/templateService/instantiationTemplates.service";
import {SharedControllersService} from "./components/genericForm/formControlsServices/sharedControlles/shared.controllers.service";
import {DuplicateVnfComponent} from "../drawingBoard/service-planning/duplicate/duplicate-vnf.component";
import {ModalService} from "./components/customModal/services/modal.service";
import {CreateDynamicComponentService} from "./components/customModal/services/create-dynamic-component.service";
import {ModalComponent} from "./components/customModal/modal.component";
import {ModalCloseButtonComponent} from './components/customModal/components/modalCloseButton/modal-close-button.component';
import {CustomButtonComponent} from "./components/customButton/custom-button.component";
import {CustomModalButtonComponent} from "./components/customModal/components/modalButton/modal-button.component";
import {CustomRippleClickAnimationDirective} from "./components/customModal/directives/ripple-click.animation.directive";
import {LoaderComponent} from "./components/customLoader/custom-loader.component";
import {LoaderService} from "./components/customLoader/custom-loader.service";
import {SvgIconComponent} from "./components/customIcon/custom-icon.component";
import {TooltipTemplateComponent} from "./components/customTooltip/custom-tooltip.component";
import {TooltipDirective} from "./components/customTooltip/tooltip.directive";
import {SdcUiComponentsModule} from "onap-ui-angular";
import {UploadFilesLinkComponent} from "./components/genericForm/genericFormSharedComponent/uploadFiles/upload-files-link.component";
import { FileUploadModule } from 'ng2-file-upload';
import {MessageModal} from "./components/messageModal/message-modal.service";
import {SpaceToUnderscorePipe} from "./pipes/spaceToUnderscore/space-to-underscore.pipe";


@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule,
    CommonModule,
    RouterModule,
    PopoverModule.forRoot(),
    FeatureFlagModule.forRoot(),
    FormsModule,
    ReactiveFormsModule,
    SdcUiComponentsModule,
    TooltipModule,
    AngularMultiSelectModule,
    BootstrapModalModule,
    DataTableModule,
    ModalModule.forRoot(),
    FileUploadModule,
    SpaceToUnderscorePipe
  ],
  declarations: [
    PopoverComponent,
    EllipsisComponent,
    MessageBoxComponent,
    FormControlErrorComponent,
    DropdownFormControlComponent,
    MultiselectFormControlComponent,
    FileFormControlComponent,
    NumberFormControlComponent,
    InputPreventionPatternDirective,
    ClickOutsideDirective,
    TooltipDirective,
    CustomRippleClickAnimationDirective,
    FormGeneralErrorsComponent,
    SpinnerComponent,
    NoContentMessageAndIconComponent,
    ModelInformationComponent,
    CapitalizeAndFormatPipe,
    ServiceInfoPipe,
    OrderByPipe,
    SafePipe,
    ObjectToArrayPipe,
    DataFilterPipe,
    SearchFilterPipe,
    InputFormControlComponent,
    FormControlMessageErrorComponent,
    GenericFormPopupComponent,
    SearchElementsModalComponent,
    AuditInfoModalComponent,
    GenericFormComponent,
    CheckboxFormControlComponent,
    ElementsTableComponent,
    SvgComponent,
    ErrorMsgComponent,
    DynamicInputsComponent,
    DynamicInputLabelPipe,
    InstantiationTemplatesModalComponent,
    ModalComponent,
    ModalCloseButtonComponent,
    CustomButtonComponent,
    CustomModalButtonComponent,
    LoaderComponent,
    SvgIconComponent,
    TooltipTemplateComponent,
    UploadFilesLinkComponent
  ],
  exports: [
    PopoverComponent,
    EllipsisComponent,
    MessageBoxComponent,
    FormControlErrorComponent,
    DropdownFormControlComponent,
    InputPreventionPatternDirective,
    CustomRippleClickAnimationDirective,
    ClickOutsideDirective,
    TooltipDirective,
    FormGeneralErrorsComponent,
    SpinnerComponent,
    NoContentMessageAndIconComponent,
    ModelInformationComponent,
    CapitalizeAndFormatPipe,
    ServiceInfoPipe,
    OrderByPipe,
    SafePipe,
    ObjectToArrayPipe,
    DataFilterPipe,
    SearchFilterPipe,
    InputFormControlComponent,
    FormControlMessageErrorComponent,
    GenericFormPopupComponent,
    SearchElementsModalComponent,
    AuditInfoModalComponent,
    GenericFormComponent,
    CheckboxFormControlComponent,
    ElementsTableComponent,
    ErrorMsgComponent,
    SvgComponent,
    DynamicInputsComponent,
    DynamicInputLabelPipe,
    ModalComponent,
    ModalCloseButtonComponent,
    CustomButtonComponent,
    CustomModalButtonComponent,
    LoaderComponent,
    SvgIconComponent,
    TooltipTemplateComponent,
    UploadFilesLinkComponent
  ],
  entryComponents : [
    GenericFormPopupComponent,
    SearchElementsModalComponent,
    InstantiationTemplatesModalComponent,
    DuplicateVnfComponent,
    ModalComponent
  ],
  providers: [
    ServiceInfoService,
    MessageBoxService,
    CreateDynamicComponentService,
    ModalService,
    LoaderService,
    HttpInterceptorService,
    IframeService,
    DefaultDataGeneratorService,
    ConfigurationService,
    GenericFormService,
    FlagsResolve,
    ViewEditResolver,
    RetryResolver,
    RecreateResolver,
    InstantiationTemplatesService,
    ServiceControlGenerator,
    ServicePopupService,
    VnfControlGenerator,
    VfModuleControlGenerator,
    ControlGeneratorUtil,
    SharedControllersService,
    CustomValidators,
    NetworkPopupService,
    VfModulePopupService,
    VfModuleUpgradePopupService,
    NetworkControlGenerator,
    VnfGroupControlGenerator,
    VnfGroupPopupService,
    BasicPopupService,
    GenericFormPopupService,
    FormGeneralErrorsService,
    VnfPopupService,
    AuditInfoModalComponentService,
    VnfPopupService,
    ElementsTableService,
    ErrorMsgService,
    DataFilterPipe,
    SearchFilterPipe,
    ModelInformationService,
    MultiselectFormControlService,
    InstantiationTemplatesModalService,
    LoaderService,
    MessageModal
  ]
})
export class SharedModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: SharedModule,
      providers: [MessageBoxService,  DatePipe, SpaceToUnderscorePipe]
    };
  }
}
