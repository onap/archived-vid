import {AfterViewInit, Component, ViewChild, ViewContainerRef} from "@angular/core";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'tooltip-template',
  template: `
    <div class="custom-tooltip-template-container">
        <ng-container #templateContainer></ng-container>
    </div>`
})

export class TooltipTemplateComponent implements AfterViewInit {
  @ViewChild('templateContainer', {read: ViewContainerRef, static: true}) public container: ViewContainerRef;

  public viewReady: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  ngAfterViewInit() : void {
    this.viewReady.next(true);
  }
}
