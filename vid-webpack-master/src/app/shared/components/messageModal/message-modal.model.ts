import {MessageModalTypeEnum} from "./message-modal-type.enum";
import {ButtonType} from "../customModal/models/button.type";

export class MessageModalModel {
  title : string;
  text : string;
  type : MessageModalTypeEnum;
  buttons :  {text: string, size: string, type : ButtonType, closeModal: boolean}[];

  constructor( title : string, text : string, type : MessageModalTypeEnum, buttons : {text: string, size: string, type : ButtonType, closeModal: boolean}[]){
    this.title = title;
    this.text = text;
    this.type = type;
    this.buttons = buttons;
  }
}
