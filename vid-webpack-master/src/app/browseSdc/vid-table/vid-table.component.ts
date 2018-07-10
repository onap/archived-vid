/**
 * Created by cp2122 on 1/2/2018.
 */
import { Component, Input, EventEmitter, Output } from '@angular/core';
export class CustomTableColumnDefinition {
  public displayName = '';
  public key = '';
  public type? = 'text';
  public text? = ''; // for button
  public action? = ''; // for button - callback
  public showCondition? = ''; // for button
  // public binding = '';
  public filter? = '';
  // public computedClass: any;
  // public isComputed = false;
  // public isAnchor = false;
  // public srefBinding = '';
}

export class CustomTableConfig {
  public sortBy = '';
  public sortOrder = 'desc';
  public pageSize? = 10;
  public pageNumber? = 1;
  public totalCount? = 0;
  public totalPages? = 0;
  public maxSize? = 10;
  public showSelectCheckbox? = false;
  public showSelectAll? = true;
  public showSort? = true;
  public clientSort? = true;
  public clientPaging? = true;
  public stickyHeader? = true;
  public stickyHeaderOffset? = 0;
  public stickyContainer? = '';
}

export class CustomTableOptions {
  public data: any[];
  public columns: Array<CustomTableColumnDefinition>;
  public config: CustomTableConfig;
  // public callbacks: any;
}
@Component({
  selector: 'vid-table',
  styleUrls: ['./vid-table.component.scss'],
  templateUrl: './vid-table.component.html'
})

export class VidTableComponent {
  @Input() options: CustomTableOptions;
  @Input() filterQuery = '';

  @Output() clickEvent = new EventEmitter<any>();
  clickBtn(row, actionName) {
    row.actionName = actionName;
    this.clickEvent.emit(row);
  }
}
