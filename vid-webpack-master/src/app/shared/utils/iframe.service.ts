import {Injectable} from "@angular/core";
import {DialogService} from "ng2-bootstrap-modal";

@Injectable()
export class IframeService {

  addClassOpenModal(elementClassName: string) {
    const parentBodyElement = parent.document.getElementsByClassName(elementClassName)[0];
    if (parentBodyElement) {
      parentBodyElement.classList.add("modal-open");
    }
  }

  removeClassCloseModal(elementClassName: string) {
    const parentBodyElement = parent.document.getElementsByClassName(elementClassName)[0];
    if (parentBodyElement) {
      parentBodyElement.classList.remove("modal-open");
    }
  }

  closeIframe(dialogService: DialogService, that, event?) {
    const messageEvent = event ? event : "closeIframe";
    this.removeClassCloseModal('content');
    dialogService.removeDialog(that);
    setTimeout(() => {
      window.parent.postMessage(messageEvent, "*");
    }, 15);
  }



  addFullScreen() {
    let parentBodyElement = parent.document.getElementsByClassName('service-model-content')[0];
    if (parentBodyElement) {
      parentBodyElement.classList.add("full-screen");
    }
  }

  removeFullScreen() {
    let parentBodyElement = parent.document.getElementsByClassName('service-model-content')[0];
    if (parentBodyElement) {
      parentBodyElement.classList.remove("full-screen");
    }
  }
}
