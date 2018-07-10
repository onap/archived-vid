import {AfterViewInit, Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import { ContextMenuService } from 'ngx-contextmenu';
import { Constants } from '../../shared/utils/constants';
import {ServicePlanningService} from "../../services/service-planning.service";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {ITreeOptions, TreeComponent} from "angular-tree-component";
import {VnfPopupComponent} from "../../components/vnf-popup/vnf-popup.components";
import {DialogService} from "ng2-bootstrap-modal";
import {ActivatedRoute} from "@angular/router";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import { MessageBoxData, ModalSize, ModalType } from '../../shared/components/messageBox/messageBox.data';
import { MessageBoxService } from '../../shared/components/messageBox/messageBox.service';
import { deleteVnfInstance, deleteVfModuleInstance } from '../../service.actions';
import { isNullOrUndefined } from 'util';
import {IframeService} from "../../shared/utils/iframe.service";


@Component({
  selector: 'drawing-board-tree',
  templateUrl: './drawing-board-tree.html',
  styleUrls : ['./drawing-board-tree.scss']
})


export class DrawingBoardTreeComponent implements OnInit, AfterViewInit {
  constructor(private _contextMenuService: ContextMenuService,
              private _servicePlanningService: ServicePlanningService,
              private _iframeService : IframeService,
              private dialogService: DialogService,
              private store: NgRedux<AppState>,
              private route: ActivatedRoute) {
    this.route
      .queryParams
      .subscribe(params => {
        this.serviceModelId = params['serviceModelId'];
      });
  }

  @Output()
  highlightNode : EventEmitter<number> = new EventEmitter<number>();

  @ViewChild('tree') tree: TreeComponent;
  missingDataTooltip: string = Constants.Error.MISSING_VNF_DETAILS;
  currentNode: ITreeNode = null; //
  nodes = [];
  serviceModelId: string;
  options: ITreeOptions = {
    nodeHeight: 45,
    dropSlotHeight: 1
  };
  parentElementClassName = 'content';

  ngOnInit(): void {
    this.store.subscribe(() => {this.updateTree()});
    this.updateTree()
  }

  updateTree() {
    const serviceInstance = this.store.getState().service.serviceInstance[this.serviceModelId];
    this.nodes = this._servicePlanningService.convertServiceInstanceToTreeData(serviceInstance, this.serviceModelId);
  }

  ngAfterViewInit():void {
    // Expand drawing tree on init.
    this.tree.treeModel.expandAll();
  }

  public onContextMenu($event: MouseEvent, node: ITreeNode): void {
    this.currentNode = node;
    node.focus();
    node.setActiveAndVisible(false);
    this.selectNode(node);
    this._contextMenuService.show.next({
      event: <any>$event,
      item: node,
    });
    $event.preventDefault();
    $event.stopPropagation();
  }

  public editItem(node: ITreeNode): void {
    node = this.currentNode;
    this._iframeService.addClassOpenModal(this.parentElementClassName);
    this.dialogService.addDialog(VnfPopupComponent, {
      serviceModelId: this.serviceModelId,
      modelName: node.data.modelName,
      modelType: node.data.type,
      parentModelName: node.parent.data.modelName,
      isNewVfModule : false
    })
  }

  public deleteItem(node: ITreeNode): void {
    if(this.currentNode.data.type === 'VF'){
      if(!isNullOrUndefined(this.currentNode.data.children) && this.currentNode.data.children.length === 0){
        this.store.dispatch(deleteVnfInstance(this.currentNode.data.modelName, this.serviceModelId));
      }else {
        let messageBoxData : MessageBoxData = new MessageBoxData(
          "Remove VNF",  // modal title
          "You are about to remove this VNF and all its children from this service. Are you sure you want to remove it?",

          ModalType.alert,
          ModalSize.medium,
          [
            {text:"Remove VNF", size:"large",  callback: this.removeVnf.bind(this), closeModal:true},
            {text:"Don’t Remove", size:"medium", closeModal:true}
          ]);

        MessageBoxService.openModal.next(messageBoxData);
      }
    }else {
        this.store.dispatch(deleteVfModuleInstance(this.currentNode.data.modelName, this.serviceModelId, node.parent.data.modelName));
    }
  }

  removeVnf() {
    this.store.dispatch(deleteVnfInstance(this.currentNode.data.modelName,  this.serviceModelId));
  }

  public selectNode(node: ITreeNode): void {
    node.expand();
    this.highlightNode.emit(node.data.modelId);
  }

  isDataMissing(node: ITreeNode) {
    //todo: currently not showing the alert icon. will be implemented in upcoming story.
    return false;
  }

}


