import {Injectable} from "@angular/core";
import {InstantiationTemplatesRowModel} from "./instantiation.templates.row.model";
import {Router} from "@angular/router";
import * as _ from 'lodash';

@Injectable()
export class InstantiationTemplatesModalService {
  constructor(private _router : Router){

  }
  convertResponseToUI = (jobsResponse: any[]): InstantiationTemplatesRowModel[] => {
    let tableRows: InstantiationTemplatesRowModel[] = [];

    jobsResponse.forEach((job) => {
      tableRows.push(new InstantiationTemplatesRowModel(job));
    });

    return tableRows;
  };


  filterByUserId = (userId: string, originalTableData: InstantiationTemplatesRowModel[]): InstantiationTemplatesRowModel[] => {
    if (!_.isNil(originalTableData)) {
      return originalTableData.filter((item: InstantiationTemplatesRowModel) => {
        return item.userId === userId;
      });
    }
    return [];
  };


  navigateToNewServiceModal(serviceModelId: string) {
     this._router.navigate(['/servicePopup'], { queryParams: { serviceModelId: serviceModelId, isCreate:true, isInstantiationTemplateExists : true}, queryParamsHandling: 'merge' });
  }

}
