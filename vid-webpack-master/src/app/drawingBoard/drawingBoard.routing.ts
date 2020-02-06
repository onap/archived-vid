import {Route} from '@angular/router';
import {ServicePlanningComponent, ServicePlanningEmptyComponent} from "./service-planning/service-planning.component";
import {FlagsResolve} from "../shared/resolvers/flag/flag.resolver";
import {ViewEditResolver} from "../shared/resolvers/viewEdit/viewEdit.resolver";
import {DrawingBoardGuard} from "./guards/servicePlanningGuard/drawingBoardGuard";
import {RetryResolver} from "../shared/resolvers/retry/retry.resolver";
import {RecreateResolver} from "../shared/resolvers/recreate/recreate.resolver";
import {SideMenuResolver} from "../shared/resolvers/sideMenu/sideMenu.resolver";

export const DrawingBoardRoutes: Route[] = [
  {
    path: 'servicePlanning',
    children: [
      {
        path: 'EDIT',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          viewEditResolver: ViewEditResolver,
          sideMenu : SideMenuResolver
        },
        canActivate: [DrawingBoardGuard]
      },
      {
        path: 'VIEW',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          viewEditResolver: ViewEditResolver,
          sideMenu : SideMenuResolver
        }
      },
      {
        path: 'RECREATE',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          viewEditResolver: RecreateResolver,
          sideMenu : SideMenuResolver
        }
      },
      {
        path: 'RETRY_EDIT',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          viewEditResolver: RetryResolver,
          sideMenu : SideMenuResolver
        },
        canActivate: [DrawingBoardGuard]
      },
      {
        path: 'RETRY',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          viewEditResolver: RetryResolver,
          sideMenu : SideMenuResolver
        }
      },
      {
        path: '',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          sideMenu : SideMenuResolver
        },
      }
    ]
  },
  {
    path: 'servicePlanningEmpty',
    component: ServicePlanningEmptyComponent,
    resolve: {
      flags: FlagsResolve,
      sideMenu : SideMenuResolver
    }
  },
];

