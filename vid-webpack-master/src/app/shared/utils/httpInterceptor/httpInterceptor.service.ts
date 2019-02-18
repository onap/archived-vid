import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';

import {Observable} from 'rxjs';
import {ErrorMessage, ErrorService} from '../../components/error/error.component.service';
import {SpinnerComponent, SpinnerInfo} from '../../components/spinner/spinner.component';
import {of} from "rxjs";

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.headers.get('x-show-spinner') !== false.toString()) {
      let spinnerInfo : SpinnerInfo = new SpinnerInfo(true, request.url, request.responseType);
      SpinnerComponent.showSpinner.next(spinnerInfo);
    }

    return next.handle(request)
      .catch((err: HttpErrorResponse) => {
        if (err.status === 500) {
          const errorMessage: ErrorMessage = new ErrorMessage('Server not available',
            'It appears that one of the backend servers is not responding.\n Please try later.',
            500);
          ErrorService.showErrorWithMessage(errorMessage);
          return of(null);
        }
        return Observable.throw(err);
      }).finally(() => {
        let spinnerInfo : SpinnerInfo = new SpinnerInfo(false, request.url, request.responseType);
        SpinnerComponent.showSpinner.next(spinnerInfo);
      });
  }
}

