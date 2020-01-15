import {Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {Mode} from "../customButton/models/mode.model";
import {Size} from "./models/icon-size.model";
import {BackgroundShape} from "./models/background-shape.model";
import {BackgroundColor} from "./models/background-color.model";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";
import {iconsMap} from 'onap-ui-common';

@Component({
  selector: 'custom-icon',
  templateUrl: './custom-icon.component.html',
  styleUrls: ['./custom-icon.component.scss'],
})
export class SvgIconComponent implements OnChanges {

  @Input() public name: string;
  @Input() public type: string;
  @Input() public mode: Mode;
  @Input() public size: Size;
  @Input() public backgroundShape: BackgroundShape;
  @Input() public backgroundColor: BackgroundColor;
  @Input() public disabled: boolean;
  @Input() public clickable: boolean;
  @Input() public className: any;
  @Input() public testId: string;

  public svgIconContent: string;
  public svgIconContentSafeHtml: SafeHtml;
  public svgIconCustomClassName: string;
  public classes: string;

  constructor(protected domSanitizer: DomSanitizer) {
    this.size = Size.medium;
    this.disabled = false;
    this.type = this.type || "common";
  }

  static Icons(): { [key: string]: string } {
    return iconsMap;
  }

  public ngOnChanges(changes: SimpleChanges) {
    this.updateSvgIconByName();
    this.buildClasses();
  }

  protected updateSvgIconByName() {
    this.svgIconContent = iconsMap[this.type][this.name] || null;
    if (this.svgIconContent) {
      this.svgIconContentSafeHtml = this.domSanitizer.bypassSecurityTrustHtml(this.svgIconContent);
      this.svgIconCustomClassName = '__' + this.name.replace(/\s+/g, '_');
    } else {
      this.svgIconContentSafeHtml = null;
      this.svgIconCustomClassName = 'missing';
    }
  }

  private buildClasses = (): void => {
    const _classes = ['svg-icon'];
    if (this.mode) {
      _classes.push('mode-' + this.mode);
    }
    if (this.size) {
      _classes.push('size-' + this.size);
    }
    if (this.clickable) {
      !this.disabled && _classes.push('clickable');
    }
    if (this.svgIconCustomClassName) {
      _classes.push(this.svgIconCustomClassName);
    }
    if (this.className) {
      _classes.push(this.className);
    }
    if (this.backgroundShape) {
      _classes.push('bg-type-' + this.backgroundShape);
    }
    if (this.backgroundShape && this.backgroundColor) {
      _classes.push('bg-color-' + this.backgroundColor);
    } else if (this.backgroundShape && !this.backgroundColor) {
      _classes.push('bg-color-primary');
    }
    this.classes = _classes.join(" ");
  }
}
