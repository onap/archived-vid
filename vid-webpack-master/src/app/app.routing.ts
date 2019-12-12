import {RouterModule, Routes} from '@angular/router';
import {FlagsResolve} from "./shared/resolvers/flag/flag.resolver";
import {DrawingBoardRoutes} from "./drawingBoard/drawingBoard.routing";
import {GenericFormPopupComponent} from "./shared/components/genericFormPopup/generic-form-popup.component";
import {SupportComponent} from "./support/support.component";
import {HealthStatusRoutes} from "./healthStatus/health-status.routing";
import {VlanTaggingRoutes} from "./vlanTagging/vlan-tagging.routing";
import {InstantiationStatusRoutes} from "./instantiationStatus/InstantiationStatus.routing";
import {InstantiationTemplatesModalComponent} from "./shared/components/genericFormPopup/instantiationTemplatesModal/instantiation.templates.modal.component";
const routes: Routes = [
  ...DrawingBoardRoutes,
  ...HealthStatusRoutes,
  ...VlanTaggingRoutes,
  ...InstantiationStatusRoutes,
  {
    path: 'instantiationTemplatesPopup',
    component: InstantiationTemplatesModalComponent,
    resolve: {
      flags: FlagsResolve
    }
  },
  {
    path: 'servicePopup',
    component: GenericFormPopupComponent,
    resolve: {
      flags: FlagsResolve
    }
  },
  {path: 'support', component: SupportComponent},
];
export const routing = RouterModule.forRoot(routes);
