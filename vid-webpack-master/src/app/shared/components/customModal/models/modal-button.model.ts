import {ButtonType} from "onap-ui-angular/dist/dist/common";
import {Placement} from "./modal.placement";

export interface IButtonComponent {
  text: string;
  disabled?: boolean;
  type?: ButtonType;
  testId?: string;
  preventDoubleClick?: boolean;
  icon_name?: string;
  icon_position?: string;
  show_spinner?: boolean;
  spinner_position?: Placement;
  size?: string;
}
