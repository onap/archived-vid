import {Route} from "@angular/router";
import {FlagsResolve} from "../shared/resolvers/flag/flag.resolver";
import {VlanTaggingComponent} from "./vlan-tagging.component";

export const VlanTaggingRoutes: Route[] = [
  {
    path: 'vlan',
    children: [
      {
        path: '',
        component: VlanTaggingComponent,
        resolve: {
          flags: FlagsResolve
        },
      }
    ]
  }
];

