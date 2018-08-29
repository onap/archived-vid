import {Component, Input} from '@angular/core';

@Component({
  selector: 'tree-node-header-properties',
  templateUrl: './tree-node-header-properties.component.html',
  styleUrls: ['./tree-node-header-properties.component.scss']
})
export class TreeNodeHeaderPropertiesComponent {
  @Input() properties : object[] = [];
}
