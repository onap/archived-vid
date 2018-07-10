import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputsModule } from '../modules/inputs.module';
import { DataTableModule } from 'angular2-datatable';
import { BootstrapModalModule } from 'ng2-bootstrap-modal';
import { TooltipModule } from 'ngx-tooltip';
import { InstantiationStatusComponent } from './instantiationStatus.component';
import { InstantiationStatusComponentService } from './instantiationStatus.component.service';
import { SharedModule } from '../shared/shared.module';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { ContextMenuModule, ContextMenuService } from 'ngx-contextmenu';
import {ModalModule, PopoverModule} from 'ngx-bootstrap';
import {AuditInfoModalComponent} from "./auditInfoModal/auditInfoModal.component";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    BootstrapModalModule,
    DataTableModule,
    TooltipModule,
    ModalModule,
    InputsModule,
    AngularSvgIconModule,
    ContextMenuModule,
    SharedModule.forRoot(),
    PopoverModule.forRoot()],
  declarations: [InstantiationStatusComponent, AuditInfoModalComponent],
  providers: [InstantiationStatusComponentService, ContextMenuService]
})
export class InstantiationStatusModule { }
