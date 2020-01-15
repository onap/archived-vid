import {Component, ComponentRef, Input} from "@angular/core";
import {ModalComponent} from "../../modal.component";
import {CustomButtonComponent} from "../../../customButton/custom-button.component";
import {RippleAnimationAction} from "../../directives/ripple-click.animation.directive";


@Component({
  selector: "sdc-modal-close-button",
  template: `
    <div class="sdc-modal__close-button"
         customRippleClickAnimation
         [ngClass]="disabled ? 'disabled' : ''"
         [rippleOnAction]="!disabled && rippleAnimationAction"
         [attr.data-tests-id]="testId"
         (click)="!disabled && closeModal('close')"
    >
      <custom-icon name="close" [mode]="disabled? 'secondary' : 'info'" size="small"></custom-icon>
    </div>
  `
})
export class ModalCloseButtonComponent extends CustomButtonComponent {

  @Input() testId: string;
  @Input() disabled: boolean;
  @Input() modalInstanceRef: ComponentRef<ModalComponent>;

  public rippleAnimationAction: RippleAnimationAction = RippleAnimationAction.MOUSE_ENTER;

  constructor() {
    super();
  }

  public closeModal = (btnName: string): void => {
    this.modalInstanceRef.instance.closeModal(btnName);
  }
}
