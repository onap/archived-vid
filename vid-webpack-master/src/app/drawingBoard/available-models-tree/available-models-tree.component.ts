import {Component, EventEmitter, Output, ViewChild} from '@angular/core';
import {ITreeOptions, TreeComponent} from 'angular-tree-component';
import '../../../../node_modules/angular-tree-component/dist/angular-tree-component.css';
import {IDType, ITreeNode} from 'angular-tree-component/dist/defs/api';
import {DialogService} from 'ng2-bootstrap-modal';
import {AvailableModelsTreeService} from './available-models-tree.service';
import {NgRedux} from "@angular-redux/store";
import {ActivatedRoute} from '@angular/router';
import {AppState} from '../../store/reducers';
import {AaiService} from '../../services/aaiService/aai.service';
import {ServicePlanningService} from '../../services/service-planning.service';
import {VnfPopupComponent} from '../../components/vnf-popup/vnf-popup.components';
import {ServiceNodeTypes} from '../../shared/models/ServiceNodeTypes';
import {VfModuleMap} from '../../shared/models/vfModulesMap';
import {IframeService} from "../../shared/utils/iframe.service";
import {createVFModuleInstance} from "../../service.actions";
import {DefaultDataGeneratorService} from "../../shared/services/defaultDataServiceGenerator/default.data.generator.service";


@Component({
  selector: 'available-models-tree',
  templateUrl: './available-models-tree.component.html',
  styleUrls: ['./available-models-tree.component.scss']
})


export class AvailableModelsTreeComponent{

  serviceModelId: string;
  serviceHierarchy;
  parentElementClassName = 'content';
  _store : NgRedux<AppState>;
  constructor(private _servicePlanningService: ServicePlanningService,
              private _iframeService: IframeService,
              private _aaiService: AaiService,
              private route: ActivatedRoute,
              private dialogService: DialogService,
              private _availableModelsTreeService: AvailableModelsTreeService,
              private _defaultDataGeneratorService: DefaultDataGeneratorService,
              private  store: NgRedux<AppState>) {
    this._store = store;
    this.route
      .queryParams
      .subscribe(params => {
        this.serviceModelId = params['serviceModelId'];
        this._aaiService.getServiceModelById(this.serviceModelId).subscribe(
          value => {
            this.serviceHierarchy = value;
            this.nodes = this._servicePlanningService.convertServiceModelToTreeNodes(this.serviceHierarchy);
          },
          error => {
            console.log('error is ', error)
          }
        );
      });

  }

  @Output()
  highlightInstances: EventEmitter<number> = new EventEmitter<number>();
  @ViewChild('tree') tree: TreeComponent;

  nodes = [];
  service = {name: ''};
  isFilterEnabled: boolean = false;

  options: ITreeOptions = {
    nodeHeight: 36,
    dropSlotHeight: 0,
    nodeClass: (node: ITreeNode) => {
      if(node.data.type === ServiceNodeTypes.VFmodule && !this.getNodeCount(node.parent))
      {
        node.data.disabled = true;
        return 'tree-node tree-node-disabled';
      }
      node.data.disabled = false;
      return 'tree-node';
    }
  };

  expandParentByNodeId(id: IDType): void {
    this.tree.treeModel.getNodeById(id).parent.expand();
  }

  searchTree(searchText: string, event: KeyboardEvent): void {
    if (searchText === '') {
      return;
    }
    this.isFilterEnabled = event.key === 'Delete' || event.key === 'Backspace' || searchText.length > 1;
    if (this.isFilterEnabled) {
      let __this = this;
      let results: ITreeNode[] = [];
      this.nodes.forEach(function (node) {
        __this.searchTreeNode(node, searchText, results);
      });
      results.forEach(function (result) {
        __this.expandParentByNodeId(result.id)
      });
    }
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

  selectNode(node: ITreeNode): void {
    node.expand();
    this.highlightInstances.emit(node.data.id);
  }

  onClickAdd(e: MouseEvent, node: ITreeNode): void {
    let data = node.data;
    let dynamicInputs = data.dynamicInputs;
    let userProvidedNaming:boolean = data.userProvidedNaming;
    let type:string = data.type;
    if(!this.store.getState().global.flags['FLAG_SETTING_DEFAULTS_IN_DRAWING_BOARD']|| node.data.type === ServiceNodeTypes.VF || this._availableModelsTreeService.shouldOpenDialog(type, dynamicInputs, userProvidedNaming)) {
      this._iframeService.addClassOpenModal(this.parentElementClassName);
      this.dialogService.addDialog(VnfPopupComponent, {
        serviceModelId: this.serviceModelId,
        parentModelName: node.parent && node.parent.data.name,
        modelName: data.name,
        modelType: type,
        dynamicInputs: dynamicInputs,
        userProvidedNaming: userProvidedNaming,
        isNewVfModule : true
      });
     }
    else {
      let vfModule = this._defaultDataGeneratorService.generateVFModule(this.serviceHierarchy, node.parent.data.name, node.data.name);
      this.store.dispatch(createVFModuleInstance(vfModule, node.data.name, this.serviceModelId));
    }
    e.preventDefault();
    e.stopPropagation();
  }

  getNodeCount(node: ITreeNode): number {
    let modelName: string = node.data.name;
    if (ServicePlanningService.isVfModule(node)) {
      let parentVnfModelName = node.parent.data.name;
      let vfModuleMap: VfModuleMap = this._servicePlanningService.getVfModuleMap(this.serviceModelId, parentVnfModelName, modelName);
      return vfModuleMap ? Object.keys(vfModuleMap).length : 0;
    } else if (ServicePlanningService.isVnf(node)) {
      let vnfInstance = this._servicePlanningService.getVnfInstance(this.serviceModelId, modelName);
      return vnfInstance ? 1 : 0;
    }
  }

  isShowIconV(node: ITreeNode): boolean {
    return this.getNodeCount(node) > 0;
  }

  isShowNodeCount(node: ITreeNode): boolean {
    return this.getNodeCount(node) > 0;
  }

  isShowIconAdd(node: ITreeNode): boolean {
    return this._availableModelsTreeService.shouldShowAddIcon(node, this.store.getState().service.serviceHierarchy, this.serviceModelId, this.getNodeCount(node));
  }
}
