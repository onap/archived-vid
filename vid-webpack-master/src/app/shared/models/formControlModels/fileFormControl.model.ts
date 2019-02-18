import {CustomValidatorOptions, FormControlModel, ValidatorModel} from "./formControl.model";
import {FormControlType} from "./formControlTypes.enum";
import {InputFormControl} from "./inputFormControl.model";

export class FileFormControl extends FormControlModel{
  acceptedExtentions : String;
  selectedFile : String;
  onDelete? :Function;
  hiddenFile: InputFormControl[];
  constructor(data) {
    super(data);
    this.type = FormControlType.FILE;
    this.selectedFile = data.selectedFile ? data.selectedFile : data.placeHolder;
    this.acceptedExtentions = data.acceptedExtentions ? data.acceptedExtentions : '';
    this.onDelete = data.onDelete ? data.onDelete : function () {};
    this.hiddenFile = data.hiddenFile;
  }
}
