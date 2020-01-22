import {ButtonType} from "../customModal/models/button.type";

export class MessageModalModel {
  title : string;
  text : string;
  type : 'error' | 'info' | 'success';
  buttons :  {text: string, size: string, type : ButtonType, closeModal: boolean}[];

  constructor( title : string, text : string, type , buttons : {text: string, size: string, type : ButtonType, closeModal: boolean}[]){
    this.title = title;
    this.text = text;
    this.type = type;
    this.buttons = buttons;
  }
}
