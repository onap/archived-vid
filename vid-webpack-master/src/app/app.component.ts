import {Component} from '@angular/core';
import {NgRedux} from "@angular-redux/store";
import {AppState} from "./shared/store/reducers";
import '../style/app.scss';

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
}
