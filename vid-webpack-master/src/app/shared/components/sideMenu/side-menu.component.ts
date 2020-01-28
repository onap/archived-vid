import {Component, OnInit} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {SideMenuModel} from "./side-menu.model";
import {Router} from "@angular/router";
import {SideMenuService} from "./side-menu.component.service";

@Component({
  selector: 'side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.scss']
})
export class SideMenuComponent implements OnInit {
  sideMenuService: SideMenuService;
  sideMenuItems: SideMenuModel[] = [];

  constructor(private router: Router,
              private _sideMenuService: SideMenuService,
              private store: NgRedux<AppState>) {
    this.sideMenuService = _sideMenuService;
  }

  ngOnInit(): void {
    this.sideMenuItems = this.store.getState().global.sideMenuItems;
  }

  redirect(sideMenuItem: SideMenuModel) {
    window.location.href = '../../' + sideMenuItem.action;
  }
}
