import {ModalType} from "./modal.type";
import {IButtonComponent} from "./modal-button.model";

export interface IModalConfig {
  size?: string; // xl|l|md|sm|xsm
  title?: string;
  titleIcon?: TitleIconDetails;
  message?: string;
  buttons?: IModalButtonComponent[];
  testId?: string;
  type?: ModalType;
}

export interface IModalButtonComponent extends IButtonComponent {
  id?: string;
  callback?: () => void;
  closeModal?: boolean;
}

export interface TitleIconDetails {
  iconName?: string;
  iconMode?: string;
  iconSize?: string;
}


