import { Component, Input } from '@angular/core';
import { Subject } from 'rxjs/Subject';

@Component({
  selector : 'spinner-component',
  templateUrl : './spinner.component.html',
  styleUrls : ['./spinner.component.scss']
})
export class SpinnerComponent {
  show : boolean = false;
  static showSpinner: Subject<boolean> = new Subject<boolean>();

  constructor(){
    SpinnerComponent.showSpinner.subscribe((status) => {
      this.show = status;
    })
  }
}
