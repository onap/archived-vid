import {Route} from '@angular/router';
import {ServicePlanningComponent, ServicePlanningEmptyComponent} from "./service-planning/service-planning.component";
import {FlagsResolve} from "../shared/resolvers/flag/flag.resolver";
import {ViewEditResolver} from "../shared/resolvers/viewEdit/viewEdit.resolver";
import {DrawingBoardGuard} from "./guards/servicePlanningGuard/drawingBoardGuard";
import {RetryResolver} from "../shared/resolvers/retry/retry.resolver";

export const DrawingBoardRoutes: Route[] = [
  {
    path: 'servicePlanning',
    children: [
      {
        path: 'EDIT',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          viewEditResolver: ViewEditResolver
        },
        canActivate: [DrawingBoardGuard]
      },
      {
        path: 'VIEW',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          viewEditResolver: ViewEditResolver
        }
      },
      {
        path: 'RECREATE',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          viewEditResolver: RetryResolver
        }
      },
      {
        path: 'RETRY_EDIT',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          viewEditResolver: RetryResolver
        },
        canActivate: [DrawingBoardGuard]
      },
      {
        path: 'RETRY',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve,
          viewEditResolver: RetryResolver
        }
      },
      {
        path: '',
        component: ServicePlanningComponent,
        resolve: {
          flags: FlagsResolve
        },
      }
    ]
  },
  {
    path: 'servicePlanningEmpty',
    component: ServicePlanningEmptyComponent,
    resolve: {
      flags: FlagsResolve,
    }
  },
];

