import {Route} from "@angular/router";
import {FlagsResolve} from "../shared/resolvers/flag/flag.resolver";
import {InstantiationStatusComponent} from "./instantiationStatus.component";
import {SideMenuResolver} from "../shared/resolvers/sideMenu/sideMenu.resolver";

export const InstantiationStatusRoutes: Route[] = [
  {
    path: 'instantiationStatus',
    children: [
      {
        path: '',
        component: InstantiationStatusComponent,
        resolve: {
          flags: FlagsResolve,
          sideMenu : SideMenuResolver
        },
      }
    ]
  }
];

