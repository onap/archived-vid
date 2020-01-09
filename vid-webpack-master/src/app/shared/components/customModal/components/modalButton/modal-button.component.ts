import {Component, EventEmitter, HostListener, Input, Output} from "@angular/core";
import {CustomButtonComponent} from "../../../customButton/custom-button.component";

@Component({
  selector: "custom-modal-button",
  templateUrl: './modal-button.component.html',
  styleUrls: ['./modal-button.component.scss']
})
export class CustomModalButtonComponent extends CustomButtonComponent {

  @Input() public id?: string;
  @Input() public callback: Function;
  @Input() public closeModal: boolean;
  @Output() closeModalEvent: EventEmitter<any> = new EventEmitter<any>();
  @HostListener('click') invokeCallback = (): void => {
    if (this.callback) {
      this.callback();
    }
    if (this.closeModal) {
      this.closeModalEvent.emit();
    }
  }

  constructor() {
    super();
    this.closeModal = false;
  }
}
