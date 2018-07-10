
import { formasync } from './../components/form-async/form-async.component';
import {NgModule,} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { VlanTaggingComponent } from './vlan-tagging.component';
import {CommonModule} from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { NgReduxModule } from '@angular-redux/store';
import { SharedModule } from '../shared/shared.module';
import { NetworkSelectorComponent } from './network-selector/network-selector.component';
import { TooltipModule } from 'ngx-tooltip';



@NgModule({

imports: [
    CommonModule,
    NgReduxModule,
    FormsModule,
    BrowserModule,
    TooltipModule,
    SharedModule.forRoot()

  ],
  providers: [  ],
  declarations: [VlanTaggingComponent,formasync,NetworkSelectorComponent],
  entryComponents: [],
  exports: [formasync]

})

export class VlanTaggingModule { }
