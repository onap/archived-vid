import {Formasync} from './form-async/form-async.component';
import {Component, OnInit, ViewChild} from "@angular/core";
import {NetworkSelectorComponent} from "./network-selector/network-selector.component";
import {NgRedux, select} from "@angular-redux/store";
import {AppState} from "../shared/store/reducers";
import {ActivatedRoute} from "@angular/router";
import {loadServiceAccordingToUuid, loadAaiNetworkAccordingToNetworkCF, loadUserId} from "../shared/services/aaiService/aai.actions";
import {createRequest} from "../factories/mso.factory";
import {VNFModel} from "../shared/models/vnfModel";
import {VfcInstanceGroupProperties} from "../shared/models/vfcInstanceGroupProperties";
import {ModelInformationItem} from "../shared/components/model-information/model-information.component";
import {Observable} from "rxjs";
import {RootEpics} from "../shared/store/epics";
import * as _ from "lodash";

enum WizardSteps {
  one,
  two
}

const buttonTextNext = "Next";
const buttonTextBack = "Back";
const buttonTextConfirm = "Confirm";
const buttonTextCancel = "Cancel";

@Component({
  selector: "vlan-tagging",
  templateUrl: "./vlan-tagging.component.html",
  styleUrls: ["./vlan-tagging.component.scss"]
})

export class VlanTaggingComponent implements OnInit {
  constructor(private store: NgRedux<AppState>,
              private route: ActivatedRoute,
              rootEpics: RootEpics) {
    this.nextButtonText = buttonTextNext;
    this.cancelButtonText = buttonTextCancel;
    this.currentStep = WizardSteps.one;
    rootEpics.createEpics();
  }

  subscriberId: string;
  subscriberName: string;
  serviceKey: string;
  serviceType: string;
  vnfKey: string;
  serviceInstanceName: string;
  serviceModelId: string;
  modelInfoItems: Array<ModelInformationItem>;
  groups: Array<Array<ModelInformationItem>>;
  params : any;
  currentStep: WizardSteps;
  nextButtonText: string;
  cancelButtonText: string;
  wizardSteps = WizardSteps;
  cloudRegionId: string;
  serviceInstanceId : string;
  model: VNFModel;
  userId: string;
  modelCustomizationId : string;
  onServiceInstanceChange(event) {
    console.log(event);
  }

  private serviceHirarchy: any;

  @select(['service', 'serviceHierarchy'])
  readonly serviceHierarchyObserable: Observable<any>;

  @select(['service', 'userId'])
  readonly userIdObs: Observable<any>;


  @ViewChild(NetworkSelectorComponent, {static: false})
  public networkSelectorComponent: NetworkSelectorComponent;
  @ViewChild(Formasync, {static: false})
  public formAsync: Formasync;


  deploySubInterface() {

    const requestDetails = createRequest(
      this.userId,
      this.formAsync.serviceInstance,
      this.serviceHirarchy[this.serviceModelId],
      this.serviceKey,
      this.networkSelectorComponent.groupSelection,
      this.vnfKey,
      this.modelCustomizationId
    );

    // this.msoService.createVnf(requestDetails, this.serviceKey).subscribe();

    window.parent.postMessage({
      eventId: 'submitIframe',
      data: requestDetails
    }, "*");
  }

  ngOnInit() {
    this.cloudRegionId = "";
    this.store.dispatch(loadUserId());
    this.userIdObs.subscribe(res => this.userId = res);
    this.route.queryParams.subscribe(params => {
      this.params = params;
      this.serviceModelId = params["serviceModelId"];
      this.subscriberId = params["globalCustomerId"];
      this.serviceType = params["serviceType"];
      this.serviceKey = params["serviceInstanceID"];
      this.vnfKey = params["modelCustomizationName"];
      this.serviceInstanceId = params["serviceInstanceID"];
      this.serviceInstanceName = params["serviceInstanceName"];
      this.modelCustomizationId = params["modelCustomizationId"];
      this.store.dispatch(loadServiceAccordingToUuid(this.serviceModelId));
      this.serviceHierarchyObserable.subscribe(data => {
        this.serviceHirarchy = data;
        if (data && data[this.serviceModelId]) {
          this.model = new VNFModel(data[this.serviceModelId].vnfs[this.vnfKey]);
          this.updateModelInfo(this.model);
        }
      });
    });
  }

  private updateModelInfo(vnfModel: VNFModel) {
    this.modelInfoItems = [
      new ModelInformationItem("Service Instance Name", "serviceInstanceName", [this.serviceInstanceName], "", true),
      new ModelInformationItem("Model Invariant UUID", "modelInvariantUUID", [vnfModel.invariantUuid], "", true),
      new ModelInformationItem("Model Version", "modelVersion", [vnfModel.version], "", true),
      new ModelInformationItem("Model UUID", "modelUuid", [vnfModel.uuid], "", true),
      new ModelInformationItem("Model Customization UUID", "modelCustomizationUuid", [vnfModel.customizationUuid], "", true),
    ];

    this.groups = [];
    _.forOwn(vnfModel.vfcInstanceGroups, (vfcInstanceGroup, key) => {
      const properties: VfcInstanceGroupProperties = vfcInstanceGroup.vfcInstanceGroupProperties;
      this.groups.push(this.createGroupsInformation(vfcInstanceGroup.name, properties));
    });
  }

  nextStep() {
    switch (this.currentStep) {
      case WizardSteps.one:
        this.groups.map(group => {
          let networkName = _.find(group, (groupElements: ModelInformationItem) => groupElements.testsId === "networkCollectionFunction");
          this.store.dispatch(
            loadAaiNetworkAccordingToNetworkCF(networkName["values"][0], this.formAsync.serviceInstance.cloudOwner, this.formAsync.serviceInstance.lcpRegion)
          );
        });
        this.currentStep = WizardSteps.two;
        this.updateNavigationButtonText(this.currentStep);
        break;
      case WizardSteps.two:
        this.deploySubInterface();
        break;
    }
  }

  cancel() {
    switch (this.currentStep) {
      case WizardSteps.one:
        window.parent.postMessage({
          eventId: 'closeIframe'
        }, "*");
        break;
      case WizardSteps.two:
        this.currentStep = WizardSteps.one;
        this.updateNavigationButtonText(this.currentStep);
        break;
    }
  }

  updateNavigationButtonText(step: WizardSteps) {
    switch (step) {
      case WizardSteps.one:
        this.nextButtonText = buttonTextNext;
        this.cancelButtonText = buttonTextCancel;
        break;
      case WizardSteps.two:
        this.nextButtonText = buttonTextConfirm;
        this.cancelButtonText = buttonTextBack;
        break;
    }
  }

  isNextButtonDisabled() {
    switch (this.currentStep) {
      case WizardSteps.one:
        return !this.formAsync.form.valid;
      case WizardSteps.two:
        return !this.networkSelectorComponent.form.valid;
    }
  }

  createGroupsInformation(name: string, properties: VfcInstanceGroupProperties): Array<ModelInformationItem> {
    let modelInfoItems = [];
    modelInfoItems.push(new ModelInformationItem("Group Name", "groupName", [name], "", true));
    modelInfoItems.push(new ModelInformationItem("Network Collection Function", "networkCollectionFunction", [properties.networkCollectionFunction], "", true));
    modelInfoItems.push(new ModelInformationItem("VFC Instance Group Function", "instanceGroupFunction", [properties.vfcInstanceGroupFunction], "", true));
    modelInfoItems.push(new ModelInformationItem("Parent Port Role", "parentPortRole", [properties.vfcParentPortRole], "", true));
    modelInfoItems.push(new ModelInformationItem("Sub Interface Role", "subInterfaceRole", [properties.subinterfaceRole], "", true));
    return modelInfoItems;
  }
}
