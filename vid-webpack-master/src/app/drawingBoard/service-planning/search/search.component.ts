import {Component, EventEmitter, Input, Output} from "@angular/core";
import {IDType, ITreeNode} from "angular-tree-component/dist/defs/api";
import * as _ from 'lodash';

@Component({
  selector: 'search-component',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent {
  @Input() tree;
  @Input() nodes;
  @Input() inputTestId: string;

  @Output() updateNodes: EventEmitter<any> = new EventEmitter();

  searchTree(searchText: string): void {
      if(_.isNil(searchText)){
        return;
      }
      let __this = this;
      let results: ITreeNode[] = [];
      this.nodes.forEach( (node) => {
        __this.searchTreeNode(node, searchText, results);
      });
      results.forEach(function (result) {
        __this.expandParentByNodeId(result.id)
      });
      this.updateNodes.emit({
        nodes: this.nodes,
        filterValue: searchText
      });
      return;
  }

  expandParentByNodeId(id: IDType): void {
    this.tree.treeModel.getNodeById(id).parent.expand();
  }

  searchTreeNode(node, searchText: string, results): void {
    if (node.name.toLowerCase().indexOf(searchText.toLowerCase()) != -1) {
      results.push(node);
    }
    if (node.children != null) {
      for (let i = 0; i < node.children.length; i++) {
        this.searchTreeNode(node.children[i], searchText, results);
      }
    }
  }
}
