import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {TooltipModule} from "ngx-tooltip";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../shared/shared.module";
import {FeatureFlagModule} from "../featureFlag/featureFlag.module";
import {HealthStatusService} from "../shared/server/healthStatusService/health-status.service";
import {HealthStatusComponent} from "./health-status.component";

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    TooltipModule,
    CommonModule,
    SharedModule.forRoot(),
    FeatureFlagModule.forRoot()],
  providers: [HealthStatusService],
  declarations: [HealthStatusComponent],
  entryComponents: [HealthStatusComponent],
  exports: [HealthStatusComponent]
})

export class HealthStatusModule {}
