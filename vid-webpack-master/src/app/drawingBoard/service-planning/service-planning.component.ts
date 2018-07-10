import {Component, ViewChild} from '@angular/core';
import {DrawingBoardTreeComponent} from "../drawing-board-tree/drawing-board-tree.component";
import {AvailableModelsTreeComponent} from "../available-models-tree/available-models-tree.component";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {TreeComponent} from 'angular-tree-component';

@Component({
  selector: 'service-planning',
  templateUrl: './service-planning.component.html',
  styleUrls: ['./service-planning.component.scss']
})

export class ServicePlanningComponent {

  @ViewChild(DrawingBoardTreeComponent) drawingModelTree;
  @ViewChild(AvailableModelsTreeComponent) availableModelTree;

  isShowTree(): boolean {
    return true;
  }

  public highlightNodeBySelectingInstance(modelId: number): void {
    this.availableModelTree.tree.treeModel.getNodeBy((node: ITreeNode) => node.data.id === modelId)
    .setActiveAndVisible().expand();
  }

  public highlightInstancesBySelectingNode(id: number): void {
    if(this.isShowTree()) {
      let _this = this;
      let matchInstances = _this.searchTree(id);
      if (!matchInstances.length)
        _this.clearSelectionInTree(_this.drawingModelTree.tree);
      matchInstances.forEach(function (instance, index) {
        let multi : boolean = !!index;
        _this.drawingModelTree.tree.treeModel.getNodeById(instance.id)
          .setActiveAndVisible(multi).expand();
      });

    }
  }

  clearSelectionInTree(tree: TreeComponent): void {
    let activateNode = tree.treeModel.getActiveNode();
    activateNode ? activateNode.toggleActivated().blur() : null;
  }

  searchTree(modelId: number) {
    let _this = this;
    let results = [];
    let nodes = _this.drawingModelTree.nodes;
    nodes.forEach(function (node) {
      _this.searchTreeNode(node, modelId, results);
    });
    return results;
  }

  searchTreeNode(node, modelId: number, results): void {
    if(node.modelId === modelId){
      results.push(node);
    }
    if (node.children != null){
      for(let i = 0; i < node.children.length; i++){
        this.searchTreeNode(node.children[i], modelId, results);
      }
    }
  }


}

export class ServicePlanningEmptyComponent extends ServicePlanningComponent {
  isShowTree() : boolean {
    return false;
  }
}


