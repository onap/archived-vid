
import { NgModule } from '@angular/core';
import {BrowseSdcComponent} from './browseSdc.component';
import {VidTableComponent} from './vid-table/vid-table.component';
import {SdcService} from '../services/sdc.service';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {DataTableModule} from 'angular2-datatable';
import {BootstrapModalModule} from 'ng2-bootstrap-modal';
import {PreviousVersionsComponent} from './previous-versions/previous-versions.component';
import {ServicePopupComponent} from '../components/service-popup/service-popup.component';
import {ServiceInstanceDetailsComponent} from "../components/service-popup/service-instance-details/service-instance-details.component";
import {TooltipModule} from 'ngx-tooltip';
import {InputsModule} from "../modules/inputs.module";
import {VnfPopupComponent} from "../components/vnf-popup/vnf-popup.components";
import {VnfInstanceDetailsComponent} from "../components/vnf-popup/vnf-instance-details/vnf-instance-details.component";
import { VnfInstanceDetailsService } from '../components/vnf-popup/vnf-instance-details/vnf-instance-details.service';
import { SharedModule } from '../shared/shared.module';
import { ServiceInstanceDetailsService } from '../components/service-popup/service-instance-details/service-instance-details.service';
import { ServicePopupService } from '../components/service-popup/service-popup.service';
import { DataFilterPipe } from '../shared/pipes/data-filter.pipe';


@NgModule({
  imports: [CommonModule, FormsModule, ReactiveFormsModule, BootstrapModalModule, DataTableModule, TooltipModule, InputsModule, SharedModule.forRoot()],
  providers: [SdcService, VnfInstanceDetailsService, ServiceInstanceDetailsService, ServicePopupService],
  declarations: [BrowseSdcComponent, VidTableComponent, DataFilterPipe, PreviousVersionsComponent,
    ServicePopupComponent, ServiceInstanceDetailsComponent, VnfPopupComponent, VnfInstanceDetailsComponent
    ],
  entryComponents: [BrowseSdcComponent, PreviousVersionsComponent, ServicePopupComponent, VnfPopupComponent],
  exports: [ ]

})

export class BrowseSdcModule { }
