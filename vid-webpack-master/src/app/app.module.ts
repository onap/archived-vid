import {ApplicationRef, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AppComponent} from './app.component';
import {routing} from './app.routing';
import {VlanTaggingModule} from './vlanTagging/vlan-tagging.module'
import {BootstrapModalModule} from 'ng2-bootstrap-modal';
import {HashLocationStrategy, LocationStrategy} from "@angular/common";
import {InstantiationStatusModule} from './instantiationStatus/InstantiationStatus.module';
import {SharedModule} from './shared/shared.module';
import {AngularSvgIconModule} from 'angular-svg-icon';
import {NgReduxModule} from '@angular-redux/store';
import {StoreModule} from "./shared/store/module";
import {HttpInterceptorService} from './shared/utils/httpInterceptor/httpInterceptor.service';
import {DrawingBoardModule} from './drawingBoard/drawingBoard.module';
import {ScrollToModule} from '@nicky-lenaers/ngx-scroll-to';
import {LogService} from './shared/utils/log/log.service';
import {FeatureFlagsService} from "./shared/services/featureFlag/feature-flags.service";
import {SupportComponent} from "./support/support.component";
import {DrawingBoardGuard} from "./drawingBoard/guards/servicePlanningGuard/drawingBoardGuard";
import {MsoService} from "./shared/services/msoService/mso.service";
import {HealthStatusModule} from "./healthStatus/health-status.module";

@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    routing,
    SharedModule.forRoot(),
    ScrollToModule.forRoot(),
    DrawingBoardModule,
    HealthStatusModule,
    VlanTaggingModule,
    InstantiationStatusModule,
    BootstrapModalModule,
    AngularSvgIconModule,
    ReactiveFormsModule,
    NgReduxModule,
    StoreModule,
  ],
  declarations: [
    AppComponent,
    SupportComponent,
  ],
  providers: [
    LogService,
    FeatureFlagsService,
    DrawingBoardGuard,
    MsoService,
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    { provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})

export class AppModule {

  constructor(public appRef: ApplicationRef) {
    Object.defineProperty(appRef, '_rootComponents', { get: () => appRef['components'] });
  }
}
