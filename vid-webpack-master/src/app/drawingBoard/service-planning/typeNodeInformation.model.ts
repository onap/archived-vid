import {ITreeNode} from "angular-tree-component/dist/defs/api";
import * as _ from 'lodash';

export class TypeNodeInformation {
  hierarchyName: string;
  existingMappingCounterName: string;

  constructor(node: ITreeNode) {
    if (!_.isNil(node)) {
      switch (node.data.type) {
        case 'VF' : {
          this.hierarchyName = 'vnfs';
          this.existingMappingCounterName = 'existingVNFCounterMap';
          break;
        }
        case 'PNF' : {
          this.hierarchyName = 'pnfs';
          this.existingMappingCounterName = 'existingPNFCounterMap';
          break;
        }
        case 'VnfGroup' : {
          this.hierarchyName = 'vnfGroups';
          this.existingMappingCounterName = 'existingVnfGroupCounterMap';
          break;
        }
        case 'VL' : {
          this.hierarchyName = 'networks';
          this.existingMappingCounterName = 'existingNetworksCounterMap';
          break;
        }
        case 'Network': {
          this.hierarchyName = 'networks';
          this.existingMappingCounterName = 'existingNetworksCounterMap';
          break;
        }
        default : {
          console.error("Node type is not recognize");
        }
      }
    }
  }
}


