import { Component } from '@angular/core';
import { DialogComponent, DialogService } from 'ng2-bootstrap-modal';
import { CustomTableOptions } from '../vid-table/vid-table.component';

export interface PreviousVersionsModel {

  title: string;
  tableOptions: CustomTableOptions;
}

@Component({
  selector: 'previous-versions',
  templateUrl: 'previous-versions.html',
  styleUrls: ['previous-versions.css']
})
export class PreviousVersionsComponent extends DialogComponent<PreviousVersionsModel, boolean> implements PreviousVersionsModel {

  title: string;
  tableOptions: CustomTableOptions;

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  public deploy(row) {
    this.result = row;
    this.close();
  }
}
