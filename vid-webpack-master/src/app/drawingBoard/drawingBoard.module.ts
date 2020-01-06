import {AvailableModelsTreeService} from './service-planning/available-models-tree/available-models-tree.service';
import {HighlightPipe} from '../shared/pipes/highlight/highlight-filter.pipe';
import {TreeModule} from 'angular-tree-component';
import {BrowserModule} from '@angular/platform-browser';
import {TooltipModule} from 'ngx-tooltip';
import {AvailableModelsTreeComponent} from './service-planning/available-models-tree/available-models-tree.component';
import {AaiService} from '../shared/services/aaiService/aai.service';
import {DrawingBoardTreeComponent} from './service-planning/drawing-board-tree/drawing-board-tree.component';
import {SharedModule} from '../shared/shared.module';
import {ContextMenuModule, ContextMenuService} from 'ngx-contextmenu';
import {CommonModule} from '@angular/common';
import {DrawingBoardHeader} from './service-planning/drawing-board-header/drawing-board-header.component';
import {ServicePlanningComponent, ServicePlanningEmptyComponent} from './service-planning/service-planning.component';
import {DuplicateVnfComponent} from './service-planning/duplicate/duplicate-vnf.component';
import {DuplicateService} from './service-planning/duplicate/duplicate.service';
import {FormsModule} from '@angular/forms';
import {DrawingBoardTreeService} from "./service-planning/drawing-board-tree/drawing-board-tree.service";
import {DrawingBoardHeaderService} from "./service-planning/drawing-board-header/drawing-board-header.service";
import {TreeNodeHeaderPropertiesComponent} from "./service-planning/drawing-board-tree/tree-node-header-properties/tree-node-header-properties.component";
import {SafePipe} from "../shared/pipes/safe/safe.pipe";
import {FeatureFlagModule} from "../featureFlag/featureFlag.module";
import {DynamicInputsService} from "./service-planning/objectsToTree/dynamicInputs.service";
import {InstanceTreeGenerator} from "./service-planning/drawing-board-tree/instance.tree.generator";
import {SharedTreeService} from "./service-planning/objectsToTree/shared.tree.service";
import {ObjectToModelTreeService} from "./service-planning/objectsToTree/objectToModelTree/objectToModelTree.service";
import {ObjectToInstanceTreeService} from "./service-planning/objectsToTree/objectToInstanceTree/objectToInstanceTree.service";
import {ObjectToTreeService} from "./service-planning/objectsToTree/objectToTree.service";
import {SearchComponent} from "./service-planning/search/search.component";
import {SdcUiComponentsModule} from "onap-ui-angular";
import {DrawingBoardPermissions} from "./guards/servicePlanningGuard/drawingBoardGuard";
import {NgModule} from '@angular/core';
import {DragAndDropService} from "./service-planning/drawing-board-tree/dragAndDrop/dragAndDrop.service";
import {SdcUiServices} from "onap-ui-angular/dist";
import {CreateDynamicComponentService} from "onap-ui-angular/dist/utils/create-dynamic-component.service";
import {ComponentInfoComponent} from './service-planning/component-info/component-info.component';
import {ComponentInfoService} from "./service-planning/component-info/component-info.service";
import {NetworkStepService} from "./service-planning/objectsToTree/models/vrf/vrfModal/networkStep/network.step.service";
import {VpnStepService} from "./service-planning/objectsToTree/models/vrf/vrfModal/vpnStep/vpn.step.service";
import {ModalModule} from "onap-ui-angular/dist/modals/modal.module";
import {ModalService} from "../shared/onapUI/sharedOnapServices";


@NgModule({
  imports: [
    TreeModule.forRoot(),
    BrowserModule,
    ContextMenuModule,
    FormsModule,
    TooltipModule,
    CommonModule,
    SdcUiComponentsModule,
    SharedModule.forRoot(),
    FeatureFlagModule.forRoot(),
    ModalModule],
  providers: [
    AaiService,
    ObjectToTreeService,
    AvailableModelsTreeService,
    ContextMenuService,
    DuplicateService,
    DrawingBoardTreeService,
    DrawingBoardHeaderService,
    DrawingBoardPermissions,
    SafePipe,
    ObjectToInstanceTreeService,
    ObjectToModelTreeService,
    DynamicInputsService,
    InstanceTreeGenerator,
    SharedTreeService,
    ModalService,
    SdcUiServices.LoaderService,
    CreateDynamicComponentService,
    ComponentInfoService,
    DragAndDropService,
    NetworkStepService,
    VpnStepService],
  declarations: [
    AvailableModelsTreeComponent,
    HighlightPipe,
    DrawingBoardTreeComponent,
    DrawingBoardHeader,
    ServicePlanningComponent,
    ServicePlanningEmptyComponent,
    DuplicateVnfComponent,
    TreeNodeHeaderPropertiesComponent,
    SearchComponent,
    ComponentInfoComponent],
  entryComponents: [DuplicateVnfComponent],
  exports: [AvailableModelsTreeComponent, DrawingBoardTreeComponent, DrawingBoardHeader, TreeNodeHeaderPropertiesComponent, SearchComponent, DuplicateVnfComponent]
})

export class DrawingBoardModule {
}
