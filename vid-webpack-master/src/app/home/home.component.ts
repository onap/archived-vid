import { Component, OnInit } from '@angular/core';
import { SdcService } from '../services/sdc.service';
import { DataService } from '../services/data.service';

@Component({
  selector: 'my-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  providers: [SdcService, DataService]
})
export class HomeComponent implements OnInit {

  constructor(private _sdcService: SdcService) {
    // Do stuff
  }

  ngOnInit() {
    console.log('Hello Home');
    console.log('getServicesModels: ');
    this._sdcService.getServicesModels().subscribe(
      // onNext() function
      value => console.log('value is ', value),
      // onError() function
      error => console.log('error is ', error),
      // onComplete() function
      () => console.log('completed')
    );
  }

}
