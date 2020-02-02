import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild,} from '@angular/core';
import {ContextMenuComponent, ContextMenuService} from 'ngx-contextmenu';
import {Constants} from '../../../shared/utils/constants';
import {IDType, ITreeNode} from "angular-tree-component/dist/defs/api";
import {TreeComponent, TreeModel, TreeNode} from "angular-tree-component";
import {DialogService} from "ng2-bootstrap-modal";
import {ActivatedRoute} from "@angular/router";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../shared/store/reducers";
import {IframeService} from "../../../shared/utils/iframe.service";
import {DuplicateService} from '../duplicate/duplicate.service';
import {DrawingBoardTreeService, TreeNodeContextMenuModel} from "./drawing-board-tree.service";
import {NetworkPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/network/network.popup.service";
import {VfModulePopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {VnfPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vnf/vnf.popup.service";
import {HighlightPipe} from "../../../shared/pipes/highlight/highlight-filter.pipe";
import {VnfGroupPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vnfGroup/vnfGroup.popup.service";
import {ObjectToInstanceTreeService} from "../objectsToTree/objectToInstanceTree/objectToInstanceTree.service";
import {SharedTreeService} from "../objectsToTree/shared.tree.service";
import {Subject} from "rxjs/Subject";
import {changeServiceIsDirty} from "../../../shared/storeUtil/utils/service/service.actions";
import * as _ from 'lodash';
import {ErrorMsgService} from "../../../shared/components/error-msg/error-msg.service";
import {DragAndDropService} from "./dragAndDrop/dragAndDrop.service";
import {FeatureFlagsService, Features} from "../../../shared/services/featureFlag/feature-flags.service";
import {ComponentInfoService} from "../component-info/component-info.service";
import {ComponentInfoModel} from "../component-info/component-info-model";
import {ObjectToModelTreeService} from "../objectsToTree/objectToModelTree/objectToModelTree.service";
import {DrawingBoardModes} from "../drawing-board.modes";
import {ServiceInstanceActions} from "../../../shared/models/serviceInstanceActions";
import {ModalService} from "../../../shared/components/customModal/services/modal.service";

@Component({
  selector: 'drawing-board-tree',
  templateUrl: './drawing-board-tree.html',
  styleUrls: ['./drawing-board-tree.scss'],
  providers: [HighlightPipe]
})

export class DrawingBoardTreeComponent implements OnInit, AfterViewInit {
  _store: NgRedux<AppState>;
  duplicateService: DuplicateService;
  drawingBoardTreeService: DrawingBoardTreeService;
  objectToModelTreeService : ObjectToModelTreeService;
  objectToInstanceTreeService : ObjectToInstanceTreeService;
  errorMsgService: ErrorMsgService;
  isFilterEnabled: boolean = false;
  filterValue: string = '';
  contextMenuOptions: TreeNodeContextMenuModel[];

  @Input() pageMode : DrawingBoardModes;
  static triggerDeleteActionService: Subject<string> = new Subject<string>();
  static triggerUndoDeleteActionService: Subject<string> = new Subject<string>();
  static triggerreCalculateIsDirty: Subject<string> = new Subject<string>();
  @ViewChild(ContextMenuComponent, {static: false}) public contextMenu: ContextMenuComponent;

  constructor(private _contextMenuService: ContextMenuService,
              private _iframeService: IframeService,
              private dialogService: DialogService,
              private store: NgRedux<AppState>,
              private route: ActivatedRoute,
              private _duplicateService: DuplicateService,
              private modalService: ModalService,
              private _drawingBoardTreeService: DrawingBoardTreeService,
              private _networkPopupService: NetworkPopupService,
              private _vfModulePopuopService: VfModulePopupService,
              private _vnfPopupService: VnfPopupService,
              private _vnfGroupPopupService: VnfGroupPopupService,
              private _errorMsgService: ErrorMsgService,
              private _highlightPipe: HighlightPipe,
              private _objectToInstanceTreeService: ObjectToInstanceTreeService,
              private _sharedTreeService: SharedTreeService,
              private _dragAndDropService : DragAndDropService,
              private _objectToModelTreeService : ObjectToModelTreeService,
              private _componentInfoService: ComponentInfoService) {

    this.errorMsgService = _errorMsgService;
    this.duplicateService = _duplicateService;
    this.drawingBoardTreeService = _drawingBoardTreeService;
    this.contextMenuOptions = _drawingBoardTreeService.generateContextMenuOptions();
    this.objectToModelTreeService = _objectToModelTreeService;
    this.objectToInstanceTreeService = _objectToInstanceTreeService;
    DrawingBoardTreeComponent.triggerDeleteActionService.subscribe((serviceModelId) => {
      this._sharedTreeService.shouldShowDeleteInstanceWithChildrenModal(this.nodes, serviceModelId, (node, serviceModelId)=>{
        this.drawingBoardTreeService.deleteActionService(this.nodes, serviceModelId);
        this.store.dispatch(changeServiceIsDirty(this.nodes, serviceModelId));
      });
    });

    DrawingBoardTreeComponent.triggerUndoDeleteActionService.subscribe((serviceModelId) => {
      this.drawingBoardTreeService.undoDeleteActionService(this.nodes, serviceModelId);
      this.store.dispatch(changeServiceIsDirty(this.nodes, serviceModelId));
    });

    DrawingBoardTreeComponent.triggerreCalculateIsDirty.subscribe((serviceModelId) => {
      this.store.dispatch(changeServiceIsDirty(this.nodes, serviceModelId));
    });

    this._store = store;
    this.route
      .queryParams
      .subscribe(params => {
        this.serviceModelId = params['serviceModelId'];
      });
  }

  getNodeId(node: ITreeNode): string {
    return (node.data.parentType !== "" ? (node.data.parentType + "_") : "") + node.data.typeName;
  }

  updateNodes(updateData: { nodes: any, filterValue: string }): void {
    this.nodes = updateData.nodes;
    this.filterValue = updateData.filterValue;
  }

  isLinkedInstance = (node) : boolean => {
    return !_.isNil(node) && node.parentType === "VRF" || node.parentType === "VnfGroup";
  };

  @Output()
  highlightNode: EventEmitter<number> = new EventEmitter<number>();

  @ViewChild('tree', {static: false}) tree: TreeComponent;
  missingDataTooltip: string = Constants.Error.MISSING_VNF_DETAILS;
  currentNode: ITreeNode = null;
  flags: any;
  nodes = [];
  serviceModelId: string;
  options = {
    allowDrag: this._dragAndDropService.isFlagOn(),
    actionMapping: {
      mouse: {
        drop: (tree:TreeModel, node:TreeNode, $event:any, {from, to}) => {
          this._dragAndDropService.drop(this.store, this.serviceModelId, this.nodes, {from, to});
        }
      }
    },
    nodeHeight: 45,
    dropSlotHeight: 1
  };
  parentElementClassName = 'content';

  ngOnInit(): void {
    this.store.subscribe(() => {
      this.updateTree();
    });
    this.updateTree()
  }

  getNodeName(node: ITreeNode, filter: string) {
      return this._highlightPipe.transform(node.data.name, filter ? filter : '');
  }

  updateTree() {
    const serviceInstance = this.store.getState().service.serviceInstance[this.serviceModelId];
    this.nodes = this._objectToInstanceTreeService.convertServiceInstanceToTreeData(serviceInstance, this.store.getState().service.serviceHierarchy[this.serviceModelId]).filter((item) => item !== null);
    console.log('right nodes', this.nodes);

  }


  ngAfterViewInit(): void {
    this.tree.treeModel.expandAll();
  }

  public onContextMenu($event: MouseEvent, node: ITreeNode): void {
    this.flags = this.store.getState().global.flags;

    this.currentNode = node;
    node.focus();
    node.setActiveAndVisible(false);
    this.selectNode(node);
    setTimeout(() => {
      this._contextMenuService.show.next({
        contextMenu: this.contextMenu,
        event: <any>$event,
        item: node,
      });
      $event.preventDefault();
      $event.stopPropagation();
    }, 250);

  }


  executeMenuAction(methodName: string): void {
    if (!_.isNil(this.currentNode.data.menuActions) && !_.isNil(this.currentNode.data.menuActions[methodName])) {
      this.currentNode.data.menuActions[methodName]['method'](this.currentNode, this.serviceModelId);
      this.store.dispatch(changeServiceIsDirty(this.nodes, this.serviceModelId));
    }
  }

  isEnabled(node: ITreeNode, serviceModelId: string, methodName: string): boolean {
    if (!_.isNil(this.currentNode) && !_.isNil(this.currentNode.data.menuActions) && !_.isNil(this.currentNode.data.menuActions[methodName])) {
      return this.currentNode.data.menuActions[methodName]['enable'](this.currentNode, this.serviceModelId);
    }
    return false;
  }

  isVisible(node: ITreeNode, methodName: string): boolean {
    if (!_.isNil(this.currentNode) && !_.isNil(this.currentNode.data.menuActions) && !_.isNil(this.currentNode.data.menuActions[methodName])) {
      return this.currentNode.data.menuActions[methodName]['visible'](this.currentNode, this.serviceModelId);
    }
    return false;
  }



  isUpgraded(node: ITreeNode): boolean {
    return this.isLabeledAsAction(node, "Upgrade");
  }

  isDeleted(node: ITreeNode): boolean {
    return this.isLabeledAsAction(node, "Delete");
  }

  private isLabeledAsAction(node: ITreeNode, action) {
    let nodeAction = node.data.action.split('_').pop();
    if (!_.isNil(nodeAction)) {
      return nodeAction === action;
    }
    return false;
  }

  public selectNode(node: ITreeNode): void {
    node.expand();
    this._sharedTreeService.setSelectedVNF(node);
    this.highlightNode.emit(node.data.modelUniqueId);
    if (FeatureFlagsService.getFlagState(Features.FLAG_1906_COMPONENT_INFO, this.store)) {
      const serviceHierarchy = this._store.getState().service.serviceHierarchy[this.serviceModelId];

      const instanceModel = this._sharedTreeService.modelByIdentifiers(
        serviceHierarchy, node.data.modelTypeName,
        this._sharedTreeService.modelUniqueNameOrId(node.data), node.data.modelName
      );

      const model = node.data.getModel(instanceModel);
      const modelInfoItems = node.data.getInfo(model, node.data);
      const componentInfoModel: ComponentInfoModel = this._sharedTreeService.addGeneralInfoItems(modelInfoItems, node.data.componentInfoType, model, node.data);
      ComponentInfoService.triggerComponentInfoChange.next(componentInfoModel);
    }
  }

  expandParentByNodeId(id: IDType): void {
    this.tree.treeModel.getNodeById(id).parent.expand();
  }

  getcontextMenuOptionLabel(contextMenuOption: TreeNodeContextMenuModel): string{
    let optionLabel = contextMenuOption.label;
    if(contextMenuOption.label === ServiceInstanceActions.Upgrade) {
      return optionLabel.concat(" to V" + this._store.getState().service.serviceInstance[this.serviceModelId].latestAvailableVersion);
    }
    return optionLabel;
  }
}


