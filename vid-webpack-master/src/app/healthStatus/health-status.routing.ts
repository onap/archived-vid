import {Route} from '@angular/router';
import {FlagsResolve} from "../shared/resolvers/flag/flag.resolver";
import {HealthStatusComponent} from "./health-status.component";

export const HealthStatusRoutes: Route[] = [
  {
    path: 'healthStatus',
    children: [
      {
        path: '',
        component: HealthStatusComponent,
        resolve: {
          flags: FlagsResolve
        },
      }
    ]
  }
];

