import { Component, Input } from '@angular/core';

@Component({
  selector : 'form-general-error',
  templateUrl : 'formGeneralErrors.component.html',
  styleUrls : ['formGeneralErrors.component.scss']
})

export class FormGeneralErrorsComponent {
  @Input() message : string = null;
}
