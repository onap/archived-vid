import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DataTableModule } from 'angular2-datatable';
import { BootstrapModalModule } from 'ng2-bootstrap-modal';
import { TooltipModule } from 'ngx-tooltip';
import { InstantiationStatusComponent } from './instantiationStatus.component';
import { InstantiationStatusComponentService } from './instantiationStatus.component.service';
import { SharedModule } from '../shared/shared.module';
import { ContextMenuModule, ContextMenuService } from 'ngx-contextmenu';
import {ModalModule, PopoverModule} from 'ngx-bootstrap';
import {SdcUiComponentsModule} from "onap-ui-angular";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SdcUiComponentsModule,
    BootstrapModalModule,
    DataTableModule,
    TooltipModule,
    ModalModule,
    ContextMenuModule,
    SharedModule.forRoot(),
    PopoverModule.forRoot()],
  declarations: [InstantiationStatusComponent, ],
  providers: [InstantiationStatusComponentService, ContextMenuService]
})
export class InstantiationStatusModule { }
