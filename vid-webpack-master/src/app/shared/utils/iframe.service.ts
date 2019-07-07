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


  addFullScreen(){
    let parentBodyElement =  parent.document.getElementsByClassName('service-model-content')[0];
    if (parentBodyElement)  {
      parentBodyElement.classList.add("full-screen");
    }
  }

  removeFullScreen(){
    let parentBodyElement =  parent.document.getElementsByClassName('service-model-content')[0];
    if (parentBodyElement)  {
      parentBodyElement.classList.remove("full-screen");
    }
  }
}
