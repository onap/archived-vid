import { Component, OnInit } from '@angular/core';
import {ExternalComponentStatus} from "../shared/models/externalComponentStatus";
import {HealthStatusService} from "../shared/server/healthStatusService/health-status.service";

@Component({
  selector: 'app-health-status',
  templateUrl: './health-status.component.html',
  styleUrls: ['./health-status.component.scss']
})
export class HealthStatusComponent implements OnInit {
  private componentStatuses: Array<ExternalComponentStatus> = [];
  private dataIsReady: boolean;
  private lastUpdatedDate: Date;

  constructor(private _healthStatusService: HealthStatusService) {
  }


  ngOnInit() {
    this.refreshData();
  }

  refreshData(): void {
    this.dataIsReady = false;
    this._healthStatusService.getProbe()
      .subscribe((res: Array<ExternalComponentStatus>) => {
        this.componentStatuses = res;
        this.dataIsReady = true;
        this.lastUpdatedDate = new Date();
      })
  }

  getMetadata(status : ExternalComponentStatus):string {
    let metadata = status.metadata;
    delete metadata.rawData;
    return metadata;
  }

  isAvailable(componentStatus: ExternalComponentStatus) {
    return componentStatus.available;
  }
}
