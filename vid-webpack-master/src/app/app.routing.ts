import {RouterModule, Routes} from '@angular/router';

import {HomeComponent} from './home/home.component';
import {BrowseSdcComponent} from './browseSdc/browseSdc.component';
import {ServicePlanningComponent, ServicePlanningEmptyComponent} from './drawingBoard/service-planning/service-planning.component';
import {VlanTaggingComponent} from './vlanTagging/vlan-tagging.component';
import {ServicePopupComponent} from './components/service-popup/service-popup.component';
import { InstantiationStatusComponent } from './instantiationStatus/instantiationStatus.component';
import {HealthStatusComponent} from "./healthStatus/health-status.component";
import {FlagsResolve} from "./services/flags.resolve";

const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' ,resolve : {
    flags : FlagsResolve,
  }},
  { path: 'browseSdc', component: BrowseSdcComponent, resolve : {
    flags : FlagsResolve,
  }},
  { path: 'servicePlanning', component: ServicePlanningComponent, resolve : {
    flags : FlagsResolve,
  }},
  { path: 'servicePlanningEmpty', component: ServicePlanningEmptyComponent, resolve : {
    flags : FlagsResolve,
  }},
  { path: 'servicePopup', component: ServicePopupComponent, resolve : {
    flags : FlagsResolve,
  }},
  { path :'vlan', component : VlanTaggingComponent, resolve : {
    flags : FlagsResolve,
  }},
  { path: 'instantiationStatus', component: InstantiationStatusComponent, resolve : {
     flags : FlagsResolve,
    }},
  { path: 'healthStatus', component: HealthStatusComponent, resolve : {
    flags : FlagsResolve,
  }}

];

export const routing = RouterModule.forRoot(routes);
