import { Subject } from 'rxjs/Subject';

export class  MessageBoxData {
  title?: string;
  message?: string;
  size : ModalSize;
  type: ModalType;
  buttons: Array<IModalButtonComponent>;

  constructor(title: string, message: string, type: ModalType, size : ModalSize, buttons: Array<IModalButtonComponent>) {
    this.title = title;
    this.message = message;
    this.size = size;
    this.type = type;
    this.buttons = buttons;
  }
}

export interface IModalConfig {
  size?: string;
  title?: string;
  message?: string;
  buttons?: Array<IModalButtonComponent>;
  type?: string;
}
export interface IButtonComponent {
  text: string;
  disabled?: boolean;
  type?: string;
  size?: string;
}
export interface IModalButtonComponent extends IButtonComponent {
  callback?: Function;
  closeModal?: boolean;
}
export  enum ModalType {
  alert = "alert",
  error = "error",
  standard = "info",
  custom = "custom",
}
export enum ModalSize {
  xlarge = "xl",
  large = "l",
  medium = "md",
  small = "sm",
  xsmall = "xsm",
}



