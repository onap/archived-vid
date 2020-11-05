import {getTestBed, TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {NgRedux} from "@angular-redux/store";
import {DrawingBoardTreeService, TreeNodeContextMenuModel} from "./drawing-board-tree.service";
import {ITreeNode} from "angular-tree-component/dist/defs/api";

class MockAppStore<T> {
  getState() {
    return {
      service: {
        serviceInstance: {
          "serviceInstanceId": {
            vnfs: {
              "vnfStoreKey": {
                isMissingData: true,
                vfModules: {
                  "vfModulesName": {
                    "vfModulesName": {
                      isMissingData: true
                    }
                  }
                }
              },

              "vnfStoreKey1": {
                isMissingData: false,
                vfModules: {
                  "vfModulesName": {
                    "vfModulesName": {
                      isMissingData: false
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

describe('Drawing board tree Service', () => {
  let injector;
  let service: DrawingBoardTreeService;
  let httpMock: HttpTestingController;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        DrawingBoardTreeService,
        {provide: NgRedux, useClass: MockAppStore}]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(DrawingBoardTreeService);
    httpMock = injector.get(HttpTestingController);

  })().then(done).catch(done.fail));


  test('generateContextMenuOptions should return list of optional context menu', () => {
    const options: TreeNodeContextMenuModel[] = service.generateContextMenuOptions();
    const expected: TreeNodeContextMenuModel[] = [
      new TreeNodeContextMenuModel('edit', 'context-menu-edit', 'Edit', 'edit-file-o'),
      new TreeNodeContextMenuModel('duplicate', 'context-menu-duplicate', 'Duplicate', 'copy-o'),
      new TreeNodeContextMenuModel('addGroupMember', 'context-menu-addGroupMember', 'Add group members', 'plus'),
      new TreeNodeContextMenuModel('delete', 'context-menu-delete', 'Delete', 'trash-o'),
      new TreeNodeContextMenuModel('remove', 'context-menu-remove', 'Remove', 'trash-o'),
      new TreeNodeContextMenuModel('upgrade', 'context-menu-upgrade', 'Upgrade', 'upgrade'),
      new TreeNodeContextMenuModel('undoDelete', 'context-menu-undoDelete', 'Undo Delete', 'undo-delete'),
      new TreeNodeContextMenuModel('undoUpgrade', 'context-menu-undoUpgrade', 'Undo Upgrade', 'undo-delete'),
      new TreeNodeContextMenuModel('changeAssociations', 'context-menu-changeAssociations', 'Change Associations', 'edit-file-o'),
      new TreeNodeContextMenuModel('pauseInstantiation', 'context-menu-pause', 'Add pause upon completion', 'pause-upon-completion'),
      new TreeNodeContextMenuModel('removePause', 'context-menu-removePause', 'Remove Pause', 'pause-upon-completion')
    ];
    expect(options.length).toEqual(11);
    expect(options).toEqual(expected);
  });

  test('isVNFMissingData should return true if vnf isMissingData = true', () => {
    let node: ITreeNode = <any>{
      data: {
        type: 'VF',
        vnfStoreKey: "vnfStoreKey"
      }
    };
    let result: boolean = service.isVNFMissingData(node, "serviceInstanceId");
    expect(result).toBeTruthy();
  });


  test('isVNFMissingData should return false if vnf has isMissingData = false', () => {
    let node: ITreeNode = <any>{
      data: {
        type: 'VFModule',
        modelName: "vfModulesName",
        dynamicModelName: "vfModulesName",
        parent: {
          vnfStoreKey: "vnfStoreKey1",
          type: 'VF'
        }
      }
    };
    let result: boolean = service.isVNFMissingData(node, "serviceInstanceId");
    expect(result).toBeFalsy();
  });


  test('isVFModuleMissingData should return true if vnfModule has isMissingData = true', () => {
    let node: ITreeNode = <any>{
      data: {
        type: 'VFModule',
        modelName: "vfModulesName",
        dynamicModelName: "vfModulesName",
        parent: {
          vnfStoreKey: "vnfStoreKey",
          type: 'VF'
        }
      }
    };
    let result: boolean = service.isVFModuleMissingData(node, "serviceInstanceId");
    expect(result).toBeFalsy();
  });


  test('isVFModuleMissingData should return false if vnfModule has isMissingData = false', () => {
    let node: ITreeNode = <any>{
      data: {
        type: 'VFModule',
        modelName: "vfModulesName",
        dynamicModelName: "vfModulesName",
        parent: {
          vnfStoreKey: "vnfStoreKey1",
          type: 'VF'
        }
      }
    };
    let result: boolean = service.isVFModuleMissingData(node, "serviceInstanceId");
    expect(result).toBeFalsy();
  });

});
