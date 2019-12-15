import {Injectable} from "@angular/core";
import {InstantiationTemplatesRowModel} from "./instantiation.templates.row.model";

@Injectable()
export class InstantiationTemplatesModalService {

  convertResponseToUI = (jobsResponse: any[]): InstantiationTemplatesRowModel[] => {
    let tableRows: InstantiationTemplatesRowModel[] = [];

    jobsResponse.forEach((job) => {
      tableRows.push(new InstantiationTemplatesRowModel(job));
    });

    return tableRows;
  };

}
