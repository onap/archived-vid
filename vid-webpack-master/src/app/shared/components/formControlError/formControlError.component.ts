import { Component, Input } from '@angular/core';

@Component({
  selector : 'form-control-error',
  templateUrl : 'formControlError.component.html',
  styleUrls : ['formControlError.component.scss']
})
export class FormControlErrorComponent {
  @Input() message = null;
}
