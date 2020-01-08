import {Component, ElementRef, Input, ViewChild} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {FileFormControl} from "../../../../models/formControlModels/fileFormControl.model";

@Component({
  selector : 'file-form-control',
  templateUrl : './file.formControl.component.html',
  styleUrls : ['./file.formControl.component.scss']
})

export class FileFormControlComponent {
  @Input() data: FileFormControl = null;
  @Input() form: FormGroup;
  @ViewChild('fileUploader', {static: false})
  fileUploader:ElementRef ;

  onDelete(event, data, form) {
    event.stopPropagation();
    event.preventDefault();
    form.value[data.controlName] = this.fileUploader.nativeElement.value = "";
    data.selectedFile = data.placeHolder;
    if (data.onDelete){
      data.onDelete(form);
    }

  }
}
