import {Component, EventEmitter, Output, ViewChild} from '@angular/core';
import {ITreeOptions, TreeComponent} from 'angular-tree-component';
import {IDType, ITreeNode} from 'angular-tree-component/dist/defs/api';
import {DialogService} from 'ng2-bootstrap-modal';
import {AvailableModelsTreeService} from './available-models-tree.service';
import {NgRedux} from "@angular-redux/store";
import {ActivatedRoute} from '@angular/router';
import {AppState} from '../../../shared/store/reducers';
import {AaiService} from '../../../shared/services/aaiService/aai.service';
import {ServiceNodeTypes} from '../../../shared/models/ServiceNodeTypes';
import {IframeService} from "../../../shared/utils/iframe.service";
import {DefaultDataGeneratorService} from "../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {VfModulePopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {NetworkPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/network/network.popup.service";
import {createVFModuleInstance} from "../../../shared/storeUtil/utils/vfModule/vfModule.actions";
import {VnfPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vnf/vnf.popup.service";
import {DrawingBoardModes} from "../drawing-board.modes";
import {DrawingBoardTreeService} from "../drawing-board-tree/drawing-board-tree.service";
import {ObjectToModelTreeService} from "../objectsToTree/objectToModelTree/objectToModelTree.service";
import {VnfGroupPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vnfGroup/vnfGroup.popup.service";
import {SharedTreeService} from "../objectsToTree/shared.tree.service";
import {changeInstanceCounter} from "../../../shared/storeUtil/utils/general/general.actions";
import {createVnfGroupInstance} from "../../../shared/storeUtil/utils/vnfGroup/vnfGroup.actions";
import {VnfGroupControlGenerator} from "../../../shared/components/genericForm/formControlsServices/vnfGroupGenerator/vnfGroup.control.generator";
import {HighlightPipe} from "../../../shared/pipes/highlight/highlight-filter.pipe";
import * as _ from 'lodash';
import {ComponentInfoModel} from "../component-info/component-info-model";
import {ComponentInfoService} from "../component-info/component-info.service";
import {FeatureFlagsService, Features} from "../../../shared/services/featureFlag/feature-flags.service";
import {Utils} from "../../../shared/utils/utils";


@Component({
  selector: 'available-models-tree',
  templateUrl: './available-models-tree.component.html',
  styleUrls: ['./available-models-tree.component.scss'],
  providers : [HighlightPipe]
})

export class AvailableModelsTreeComponent {
  filterValue : string = '';
  serviceModelId: string;
  serviceHierarchy;
  parentElementClassName = 'content';
  _store: NgRedux<AppState>;
  isNewObject: boolean;
  availableModelsTreeService: AvailableModelsTreeService;
  drawingBoardTreeService: DrawingBoardTreeService;

  constructor(private _iframeService: IframeService,
              private _aaiService: AaiService,
              private route: ActivatedRoute,
              private dialogService: DialogService,
              private _availableModelsTreeService: AvailableModelsTreeService,
              private _drawingBoardTreeService: DrawingBoardTreeService,
              private _defaultDataGeneratorService: DefaultDataGeneratorService,
              private _vnfGroupControlGenerator: VnfGroupControlGenerator,
              private _vfModulePopuopService: VfModulePopupService,
              private _vnfGroupPopupService: VnfGroupPopupService,
              private _vnfPopupService: VnfPopupService,
              private _networkPopupService: NetworkPopupService,
              private  store: NgRedux<AppState>,
              private _objectToModelTreeService : ObjectToModelTreeService,
              private  _sharedTreeService : SharedTreeService,
              private _highlightPipe : HighlightPipe) {
    this.availableModelsTreeService = _availableModelsTreeService;
    this.drawingBoardTreeService = _drawingBoardTreeService;

    this._store = store;
    this.route
      .queryParams
      .subscribe(params => {
        this.serviceModelId = params['serviceModelId'];
        this._aaiService.getServiceModelById(this.serviceModelId).subscribe(
          value => {
            this.serviceHierarchy = value;
            this.nodes = this._objectToModelTreeService.convertServiceHierarchyModelToTreeNodes(this.serviceHierarchy);
            this.shouldOpenVRFModal(this.nodes);
          },
          error => {
            console.log('error is ', error)
          }
        );
      });

  }

  @Output()
  highlightInstances: EventEmitter<number> = new EventEmitter<number>();
  @ViewChild('tree', {static: false}) tree: TreeComponent;

  nodes = [];
  service = {name: ''};

  options: ITreeOptions = {
    allowDrop:false,
    nodeHeight: 36,
    dropSlotHeight: 0,
    nodeClass: (node: ITreeNode) => {
      if (node.data.type === ServiceNodeTypes.VFmodule && ! node.parent.data['getNodeCount'](node.parent, this.serviceModelId) && this.store.getState().global.drawingBoardStatus !== DrawingBoardModes.VIEW) {
        node.data.disabled = true;
        return 'tree-node tree-node-disabled';
      }
      node.data.disabled = false;
      return 'tree-node';
    }
  };


  shouldOpenVRFModal(nodes) :void {
    const node = this._availableModelsTreeService.shouldOpenVRFModal(nodes, this.serviceModelId, this.store.getState().service);
    if(!_.isNil(node)){
      this.onClickAdd(node,  this.serviceModelId);
    }
  }

  getNodeName(node : ITreeNode, filter : string) {
    return this._highlightPipe.transform(node.data.name ,filter ? filter : '');
  }

  expandParentByNodeId(id: IDType): void {
    this.tree.treeModel.getNodeById(id).parent.expand();
  }

  updateNodes(updateData : {nodes : any, filterValue : string}) : void {
    this.nodes = updateData.nodes;
    this.filterValue = updateData.filterValue;
  }

  selectNode(node: ITreeNode): void {
    node.expand();
    this._sharedTreeService.setSelectedNF(null);
    this.highlightInstances.emit(node.data.modelUniqueId);
    if (FeatureFlagsService.getFlagState(Features.FLAG_1906_COMPONENT_INFO, this.store)) {
      const serviceHierarchy = this._store.getState().service.serviceHierarchy[this.serviceModelId];

      const instanceModel = this._sharedTreeService.modelByIdentifiers(
        serviceHierarchy, node.data.modelTypeName,
        this._sharedTreeService.modelUniqueNameOrId(node.data), node.data.name
      );

      const model = node.data.getModel(instanceModel);
      const modelInfoItems  = node.data.getInfo(model, null);
      const componentInfoModel :ComponentInfoModel = this._sharedTreeService.addGeneralInfoItems(modelInfoItems, node.data.componentInfoType, model, null);
      ComponentInfoService.triggerComponentInfoChange.next(componentInfoModel);
    }
  }



  onClickAdd(node: ITreeNode, serviceId: string ,  isNewObject: boolean = false): void {
    this.isNewObject = isNewObject;
    let data = node.data;
    let dynamicInputs = data.dynamicInputs;
    let isAlaCarte: boolean = Utils.isALaCarte(this.serviceHierarchy.service.vidNotions.instantiationType);
    let isEcompGeneratedNaming: boolean = data.isEcompGeneratedNaming;
    let type: string = data.type;
    if (node.data.type === ServiceNodeTypes.VF ||
      this._availableModelsTreeService.shouldOpenDialog(type, dynamicInputs, isEcompGeneratedNaming)) {
      this._iframeService.addClassOpenModal(this.parentElementClassName);
      node.data.onAddClick(node, serviceId);
    } else {
      if (node.data.type === ServiceNodeTypes.VnfGroup)  {
        let instanceName = this._vnfGroupControlGenerator.getDefaultInstanceName(null, serviceId, node.data.name);
        let vnfGroup = this._defaultDataGeneratorService.generateVnfGroupInstance(this.serviceHierarchy.vnfGroups[node.data.name], isEcompGeneratedNaming, isAlaCarte, instanceName);
        this._store.dispatch(changeInstanceCounter(node.data.modelUniqueId, serviceId, 1 , <any> {data: {type: 'VnfGroup'}}));
        this._store.dispatch(createVnfGroupInstance(vnfGroup, node.data.name, serviceId, node.data.name));
        DrawingBoardTreeService.triggerCheckIsDirty.next(this.serviceModelId);
      } else {
        let vfModule = this._defaultDataGeneratorService.generateVFModule(this.serviceHierarchy.vnfs[node.parent.data.name].vfModules[node.data.name], dynamicInputs, isEcompGeneratedNaming, isAlaCarte);
        let positionOfNextInstance = null;
        if(this._sharedTreeService.isAddPositionFlagTrue()) {
          let baseModule = this.serviceHierarchy.vnfs[node.parent.data.name].vfModules[node.data.name].properties.baseModule;
          if(baseModule) {
            //Assign Position of 1 for the instance being created & update the positions(existing position +1) for remaining instances
            positionOfNextInstance = 1;
            this._defaultDataGeneratorService.updatePositionForRemainingVfModules(this.serviceModelId);
          } else {
            positionOfNextInstance = this._defaultDataGeneratorService.calculatePositionOfVfmodule(this.serviceModelId);
          }
        }
        if (this._sharedTreeService.selectedNF) {
          this.store.dispatch(createVFModuleInstance(vfModule, node.data.name, this.serviceModelId, positionOfNextInstance, this._sharedTreeService.selectedNF));
          DrawingBoardTreeService.triggerCheckIsDirty.next(this.serviceModelId);
        } else if (this._availableModelsTreeService.getOptionalVNFs(this.serviceModelId, node.parent.data.modelUniqueId).length === 1) {
          let existVnf = this._store.getState().service.serviceInstance[this.serviceModelId].vnfs;
          if(!_.isNil(existVnf)){
            for(let vnfKey in existVnf){
              const modelUniqueId = this._sharedTreeService.modelUniqueId(existVnf[vnfKey]);
              if(modelUniqueId === node.parent.data.id){
                this.store.dispatch(createVFModuleInstance(vfModule, node.data.name, this.serviceModelId, positionOfNextInstance, vnfKey));
                DrawingBoardTreeService.triggerCheckIsDirty.next(this.serviceModelId);
              }
            }
          }


        } else {
          this._availableModelsTreeService.addingAlertAddingNewVfModuleModal();
        }
      }
    }
  }
}
