import {Injectable} from "@angular/core";
import {TemplateTableRowModel} from "./template-table-row.model";

@Injectable()
export class TemplateModalComponentService {
  convertResponseToUI = (jobsResponse: any[]): TemplateTableRowModel[] => {
    let tableRows: TemplateTableRowModel[] = [];

    jobsResponse.forEach((job) => {
      tableRows.push(new TemplateTableRowModel(job));
    });

    return tableRows;
  };
}
