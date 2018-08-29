import {AbstractControl, ValidatorFn} from "@angular/forms";
import {Injectable} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {FileUnit} from "../../components/formControls/component/file/fileUnit.enum";
import * as _ from 'lodash';

@Injectable()
export class CustomValidators {
  static uniqueInstanceNameValidator(...args): ValidatorFn {
    const store : NgRedux<AppState> = args[0];
    const serviceId : string = args[1];
    const originalName : string = args[2];
    return (control: AbstractControl): {[key: string]: any} | null => {
      const name = control.value;
      if(name && name !== originalName) {
        const result: boolean = CustomValidators.isUnique(store.getState().service.serviceInstance, serviceId, name, name === originalName);
        if(!result){
          return {
            uniqueInstanceNameValidator : true
          };
        }else {
          return null;
        }
      }
      return null;
    };
  }

  static isUnique(serviceInstance : any, serviceId : string, name: string, isEqualToOriginalInstanceName : boolean) : boolean {
    const service = serviceInstance[serviceId];
    if(service){
      const existingNames = service.existingNames;
      if(_.includes(existingNames, name)) return false;
    }
    return true;
  }

  static isStringContainHtmlTag(str: string): boolean{
    var regex = RegExp("<[^>]*>");
    return regex.test(str);
  }

  static isValidJson() : ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} | null => {
      const content = control.value;
      let invalid: boolean = false;
      if (content)  {
        try {
          JSON.parse(content);
        } catch (e) {
          invalid = true;
        }
      }
      return (invalid) ? {isValidJson : true} : null;
    }
  }

  static isStringContainTags() : ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} | null => {
      const content = control.value;
      let invalid: boolean = false;
      if (content)  {
          invalid = CustomValidators.isStringContainHtmlTag(content);
      }
      return (invalid) ? {isStringContainTags : true} : null;
    }
  }

  static isFileTooBig(...args) : ValidatorFn {
    const unit : FileUnit = args[0];
    const size : number = args[1];
    return (control: AbstractControl): {[key: string]: any} | null => {
      const file = control.value;
      let sizeToByte: number;
      if (file) {
        switch (unit)  {
          case FileUnit.b:
            sizeToByte = 1/8;
            break;
          case FileUnit.B:
            sizeToByte = 1;
            break;
          case FileUnit.KB:
            sizeToByte = 1000;
            break;
          case FileUnit.MB:
            sizeToByte = 1000000;
            break;
          default:
            sizeToByte = 0;
            break;

        }
        return (file.size > (sizeToByte * size)) ? {isFileTooBig: true} : null;
      }
      return null;
    }
  }
}
