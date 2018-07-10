import { AvailableModelsTreeService } from './available-models-tree/available-models-tree.service';
import { NgModule } from '@angular/core';
import { HighlightPipe } from '../shared/pipes/highlight-filter.pipe';
import { TreeModule } from 'angular-tree-component';
import { BrowserModule } from '@angular/platform-browser';
import { TooltipModule } from 'ngx-tooltip';
import { AvailableModelsTreeComponent } from './available-models-tree/available-models-tree.component';
import { ServicePlanningService } from '../services/service-planning.service';
import { AaiService } from '../services/aaiService/aai.service';
import { DrawingBoardTreeComponent } from './drawing-board-tree/drawing-board-tree.component';
import { SharedModule } from '../shared/shared.module';
import { ContextMenuModule, ContextMenuService } from 'ngx-contextmenu';
import { CommonModule } from '@angular/common';
import { DrawingBoardHeader } from './drawing-board-header/drawing-board-header.component';
import { ServicePlanningComponent, ServicePlanningEmptyComponent } from './service-planning/service-planning.component';

@NgModule({
  imports: [
    TreeModule,
    BrowserModule,
    ContextMenuModule,
    TooltipModule,
    CommonModule,
    SharedModule.forRoot()],
  providers: [
    ServicePlanningService,
    AaiService,
    AvailableModelsTreeService ,
    ContextMenuService,
    ServicePlanningService],
  declarations: [
    AvailableModelsTreeComponent,
    HighlightPipe,
    DrawingBoardTreeComponent,
    DrawingBoardHeader,
    ServicePlanningComponent,
    ServicePlanningEmptyComponent],
  exports: [ AvailableModelsTreeComponent, DrawingBoardTreeComponent, DrawingBoardHeader]
})

export class DrawingBoardModule { }
