import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {ComponentInfoService} from "./component-info.service";
import {ComponentInfoModel, ComponentInfoType} from "./component-info-model";
import {ActivatedRoute} from "@angular/router";


@Component({
  selector: 'component-info',
  templateUrl: './component-info.component.html',
  styleUrls: ['./component-info.component.scss']//,
  // changeDetection: ChangeDetectionStrategy.OnPush
})
export class ComponentInfoComponent implements OnInit {
  componentInfoModel: ComponentInfoModel = null;

  constructor(private _componentInfoService:ComponentInfoService, private route: ActivatedRoute) {}

  ngOnInit() {
    ComponentInfoService.triggerComponentInfoChange.subscribe((input : ComponentInfoModel) => {
        if(input.type === ComponentInfoType.SERVICE){
          this.getServiceInformation();
        }else {
          this.componentInfoModel = input;
        }
      });

    this.getServiceInformation();
  }



  getServiceInformation() : void {
    this.route
      .queryParams
      .subscribe(params => {
        let serviceModelId = params['serviceModelId'];
        this.componentInfoModel = this._componentInfoService.getInfoForService(serviceModelId);
      });
  }
}
