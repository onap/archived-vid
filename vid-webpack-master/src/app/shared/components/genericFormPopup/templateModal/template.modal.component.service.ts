import {Injectable} from "@angular/core";
import {TemplateTableRowModel} from "./template-table-row.model";

@Injectable()
export class TemplateModalComponentService {
  getTableHeaders = (): string[] => {
    return ['User ID', 'Date', 'Instance Name', 'Instantiation Status', 'Summary', 'Region', 'Tenant', 'AIC Zone'];
  };

  convertResponseToUI = (jobsResponse: any[]): TemplateTableRowModel[] => {
    let tableRows: TemplateTableRowModel[] = [];

    jobsResponse.forEach((job) => {
      tableRows.push(new TemplateTableRowModel(job));
    });

    return tableRows;
  };
}
