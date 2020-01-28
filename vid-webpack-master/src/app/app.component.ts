import {Component} from '@angular/core';
import {NgRedux} from "@angular-redux/store";
import {AppState} from "./shared/store/reducers";
import '../style/app.scss';
import {IframeService} from "./shared/utils/iframe.service";

@Component({
  selector: 'vid-app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  constructor(private store: NgRedux<AppState>) {
    store.subscribe(()=>{
      sessionStorage.setItem('reduxState', JSON.stringify(store.getState()));
    });
  }

  /***********************************************************************************
    should return true if feature flag is on and the current app is not inside iframe
   **********************************************************************************/
  shouldShowSideMenu() :boolean {
    const showSideMenuFlag = !!this.store.getState().global.flags["FLAG_2006_SHOW_NEW_SIDE_MENU"];
    if(!showSideMenuFlag){
      return false;
    }else{
      return !IframeService.isIframe();
    }
  }


  isIframe() : boolean {
    return IframeService.isIframe() && !!this.store.getState().global.flags["FLAG_2006_SHOW_NEW_SIDE_MENU"];
  }
}
