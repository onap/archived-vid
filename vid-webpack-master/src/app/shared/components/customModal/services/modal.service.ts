import {ComponentRef, Injectable, Type} from "@angular/core";
import {CreateDynamicComponentService} from "./create-dynamic-component.service";
import {ModalType} from "../models/modal.type";
import {ButtonType} from "../models/button.type";
import {ModalButtonComponent} from "onap-ui-angular/dist/modals/modal-button.component";
import {ModalSize} from "../models/modal.size";
import {IModalConfig} from "../models/modal.model";
import {ModalComponent} from "../modal.component";


@Injectable()
export class ModalService {

  constructor(private createDynamicComponentService: CreateDynamicComponentService) {
  }

  private getBaseModal = (type: ModalType | ButtonType, title: string, message: string, testId: string, buttons?: ModalButtonComponent[]): ModalComponent => {
    const modalConfig = {
      size: ModalSize.small,
      title: title,
      message: message,
      testId: testId,
      buttons: buttons ? buttons : [{text: 'OK', type: type, closeModal: true}],
      type: type
    } as IModalConfig;
    const modalInstance: ComponentRef<ModalComponent> = this.openModal(modalConfig);
    return modalInstance.instance;
  };

  /* Shortcut method to open basic modals with title, message, and OK button that simply closes the modal. */
  public openInfoModal = (title: string, message: string, testId: string, buttons?: ModalButtonComponent[]): ModalComponent => {

    return this.getBaseModal(ModalType.info, title, message, testId, buttons);
  };

  public openWarningModal = (title: string, message: string, testId: string, buttons?: ModalButtonComponent[]): ModalComponent => {
    return this.getBaseModal(ModalType.warning, title, message, testId, buttons);
  };

  public openErrorModal = (title: string, message: string, testId: string, buttons?: ModalButtonComponent[]): ModalComponent => {
    return this.getBaseModal(ModalType.error, title, message, testId, buttons);
  };

  public openSuccessModal = (title: string, message: string, testId: string, buttons?: ModalButtonComponent[]): ModalComponent => {
    return this.getBaseModal(ModalType.success, title, message, testId, buttons);
  };

  public openCustomModal = (modalConfig: IModalConfig, dynamicComponentType: Type<any>, dynamicComponentInput?: any) => {
    const modalInstance: ComponentRef<ModalComponent> = this.openModal(modalConfig);
    this.createInnnerComponent(modalInstance, dynamicComponentType, dynamicComponentInput);
    return modalInstance.instance;
  };

  public createInnnerComponent = (modalInstance: ComponentRef<ModalComponent>, dynamicComponentType: Type<any>, dynamicComponentInput?: any): void => {
    modalInstance.instance.innerModalContent = this.createDynamicComponentService.insertComponentDynamically(dynamicComponentType, dynamicComponentInput, modalInstance.instance.dynamicContentContainer);
  };

  public openModal = (customModalData: IModalConfig): ComponentRef<ModalComponent> => {
    let modalInstance: ComponentRef<ModalComponent> = this.createDynamicComponentService.createComponentDynamically(ModalComponent, customModalData);
    modalInstance.instance.instanceRef = modalInstance;
    return modalInstance;
  }

}
