import {Injectable} from "@angular/core";
import {InstantiationTemplatesRowModel} from "./instantiation.templates.row.model";
import * as _ from 'lodash';

@Injectable()
export class InstantiationTemplatesModalService {

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

}
