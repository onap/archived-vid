import {Injectable} from "@angular/core";

@Injectable()
export class IframeService {

  addClassOpenModal(elementClassName: string) {
    var parentBodyElement = parent.document.getElementsByClassName(elementClassName)[0];
    if (parentBodyElement)  {
      parentBodyElement.classList.add("modal-open");
    }
  }

  removeClassCloseModal(elementClassName: string) {
    var parentBodyElement = parent.document.getElementsByClassName(elementClassName)[0];
    if (parentBodyElement)  {
      parentBodyElement.classList.remove("modal-open");
    }
  }
}
