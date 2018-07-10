import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent, HttpErrorResponse
} from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { ErrorMessage, ErrorService } from '../../components/error/error.component.service';
import { SpinnerComponent } from '../../components/spinner/spinner.component';

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    SpinnerComponent.showSpinner.next(true);
    return next.handle(request)
      .catch((err: HttpErrorResponse) => {
        if (err.status === 500) {
          const errorMessage: ErrorMessage = new ErrorMessage('Server not available',
            'It appears that one of the backend servers is not responding.\n Please try later.',
            500);
          ErrorService.showErrorWithMessage(errorMessage);
          return Observable.of(null);
        }
        return Observable.throw(err);
      }).finally(() => {
        SpinnerComponent.showSpinner.next(false);
      });
  }
}

