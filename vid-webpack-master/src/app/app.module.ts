import { ApplicationRef, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { ApiService } from './shared';
import { routing } from './app.routing';
import { createNewHosts, removeNgStyles } from '@angularclass/hmr';
import { BrowseSdcModule } from './browseSdc/browseSdc.module';
import { VlanTaggingModule } from './vlanTagging/vlan-tagging.module'
import { BootstrapModalModule } from 'ng2-bootstrap-modal';
import { HashLocationStrategy, LocationStrategy } from "@angular/common";
import { InstantiationStatusModule } from './instantiationStatus/InstantiationStatus.module';
import { SharedModule } from './shared/shared.module';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { NgReduxModule } from '@angular-redux/store';
import { StoreModule } from "./store/module";
import { HttpInterceptorService } from './shared/utils/httpInterceptor/httpInterceptor.service';
import { DrawingBoardModule } from './drawingBoard/drawingBoard.module';
import { HealthStatusComponent } from './healthStatus/health-status.component';
import { ScrollToModule } from '@nicky-lenaers/ngx-scroll-to';
import { LogService } from './shared/utils/log/log.service';

@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    routing,
    SharedModule.forRoot(),
    ScrollToModule.forRoot(),
    DrawingBoardModule,
    VlanTaggingModule,
    InstantiationStatusModule,
    BrowseSdcModule,
    BootstrapModalModule,
    BrowseSdcModule,
    AngularSvgIconModule,
    ReactiveFormsModule,
    NgReduxModule,
    StoreModule,
  ],
  declarations: [
    AppComponent,
    HomeComponent,
    HealthStatusComponent
  ],
  providers: [
    ApiService,
    LogService,
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    { provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  private static CustomLoader: any;

  constructor(public appRef: ApplicationRef) {
    //for ng2-bootstrap-modal in angualar 5
    Object.defineProperty(appRef, '_rootComponents', { get: () => appRef['components'] });
  }
  hmrOnInit(store) {
    console.log('HMR store', store);
  }
  hmrOnDestroy(store) {
    let cmpLocation = this.appRef.components.map(cmp => cmp.location.nativeElement);
    // recreate elements
    store.disposeOldHosts = createNewHosts(cmpLocation);
    // remove styles
    removeNgStyles();
  }
  hmrAfterDestroy(store) {
    // display new elements
    store.disposeOldHosts();
    delete store.disposeOldHosts;
  }
}
