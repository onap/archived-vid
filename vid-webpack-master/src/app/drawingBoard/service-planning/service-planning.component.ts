import {Component, OnInit, ViewChild} from '@angular/core';
import {DrawingBoardTreeComponent} from "./drawing-board-tree/drawing-board-tree.component";
import {AvailableModelsTreeComponent} from "./available-models-tree/available-models-tree.component";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {TreeComponent} from 'angular-tree-component';
import {ActivatedRoute} from "@angular/router";
import * as _ from 'lodash';
import {DrawingBoardModes} from "./drawing-board.modes";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../shared/store/reducers";
import {updateDrawingBoardStatus} from "../../shared/storeUtil/utils/global/global.actions";
import {FeatureFlagsService, Features} from "../../shared/services/featureFlag/feature-flags.service";
import {ComponentInfoService} from "./component-info/component-info.service";
import {ComponentInfoModel, ComponentInfoType} from "./component-info/component-info-model";

@Component({
  selector: 'service-planning',
  templateUrl: './service-planning.component.html',
  styleUrls: ['./service-planning.component.scss']
})

export class ServicePlanningComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private  store: NgRedux<AppState>) {
  }

  pageMode: DrawingBoardModes = DrawingBoardModes.CREATE;
  @ViewChild(DrawingBoardTreeComponent, {static: false}) drawingModelTree;
  @ViewChild(AvailableModelsTreeComponent, {static: false}) availableModelTree;

  isShowTree(): boolean {
    return true;
  }

  public highlightNodeBySelectingInstance(modelId: number): void {
    // modelId might be undefined, e.g., if selected instance has no source in model
    let matchInstance = modelId ? this.availableModelTree.tree.treeModel.getNodeBy((node: ITreeNode) => (node.data.modelUniqueId) === modelId) : undefined;
    if (matchInstance) {
      matchInstance.setActiveAndVisible().expand();
    } else {
      this.clearSelectionInTree(this.availableModelTree.tree);
    }
  }

  public highlightInstancesBySelectingNode(uniqueId: string): void {
    if (this.isShowTree()) {
      let _this = this;
      let matchInstances = _this.searchTree(uniqueId);
      if (!matchInstances.length)
        _this.clearSelectionInTree(_this.drawingModelTree.tree);
      matchInstances.forEach(function (instance, index) {
        let multi: boolean = !!index;
        _this.drawingModelTree.tree.treeModel.getNodeById(instance.id)
          .setActiveAndVisible(multi).expand();
      });

    }
  }

  clearSelectionInTree(tree: TreeComponent): void {
    let activateNode = tree.treeModel.getActiveNode();
    activateNode ? activateNode.toggleActivated().blur() : null;
  }

  searchTree(uniqueId: string) {
    let _this = this;
    let results = [];
    let nodes = _this.drawingModelTree.nodes;
    nodes.forEach(function (node) {
      _this.searchTreeNode(node, uniqueId, results);
    });
    return results;
  }

  searchTreeNode(node, uniqueId: string, results): void {
    if ((node.modelUniqueId) === uniqueId) {
      results.push(node);
    }

    if (node.children != null) {
      for (let i = 0; i < node.children.length; i++) {
        this.searchTreeNode(node.children[i], uniqueId, results);
      }
    }
  }

  ngOnInit(): void {
    this.pageMode = (!_.isNil(this.route.routeConfig.path) && this.route.routeConfig.path !== "") ? this.route.routeConfig.path as DrawingBoardModes : DrawingBoardModes.CREATE;
    this.store.dispatch(updateDrawingBoardStatus(this.pageMode));
  }

  isShowComponentInfo():boolean {
    return FeatureFlagsService.getFlagState(Features.FLAG_1906_COMPONENT_INFO, this.store)
  }

  clickOutside(): void{
    this.clearSelectionInTree(this.drawingModelTree.tree);
    this.clearSelectionInTree(this.availableModelTree.tree);
    ComponentInfoService.triggerComponentInfoChange.next(new ComponentInfoModel(ComponentInfoType.SERVICE, [], []))
  }
}

export class ServicePlanningEmptyComponent extends ServicePlanningComponent {
  isShowTree(): boolean {
    return false;
  }
}

