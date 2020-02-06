import {Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {SideMenuModel} from "./side-menu.model";
import {Router} from "@angular/router";
import {SideMenuService} from "./side-menu.component.service";
import {VersionModel} from "./version.model";
import {Subject} from "rxjs";

@Component({
  selector: 'side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.scss']
})
export class SideMenuComponent{
  sideMenuService: SideMenuService;
  @Input() sideMenuItems: SideMenuModel[] = [];
  @Input() applicationVersion: VersionModel = null;
  @Input() appName: string = null;
  store: NgRedux<AppState>;
  isOpen = false;

  static closeSideMenu : Subject<void> = new Subject<void>();
  @ViewChild('sideMenuCheckbox', {static: false}) sideMenuCheckbox: ElementRef;

  constructor(private router: Router,
              private _sideMenuService: SideMenuService,
              private _store: NgRedux<AppState>) {
    this.sideMenuService = _sideMenuService;
    this.store = _store;

    SideMenuComponent.closeSideMenu.subscribe(()=>{
      this.isOpen = false;
    });
  }

  redirect(sideMenuItem: SideMenuModel): void {
    window.location.href = '../../' + sideMenuItem.action;
  }

  openCloseSideMenu() : void {
    this.isOpen = !this.isOpen;
    this.sideMenuCheckbox.nativeElement.click();
  }

  getIconType() : string {
    return this.isOpen ? 'icons_close' : 'browse';
  }

  getIconColor() : string {
    return this.isOpen ? 'primary' : 'white';
  }
}
