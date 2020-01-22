import {Component, ElementRef, Input, OnInit, ViewChild} from "@angular/core";
import {FileItem, FileUploader} from "ng2-file-upload";
import {UploadFilesModel} from "./upload-files.model";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'upload-files',
  templateUrl: './upload-files.component.html',
  styleUrls: ['./upload-files.component.scss']
})
export class UploadFilesComponent implements OnInit {
  target: any;
  uploader: FileUploader;
  @Input() uploadFilesModel: UploadFilesModel;
  @Input() form: FormGroup;
  @ViewChild('fileInput', {static: false}) fileInput: ElementRef;

  ngOnInit(): void {
    this.uploader = new FileUploader({});

    this.uploader.onAfterAddingAll = async (files: FileItem[]) => {
      const result = await this.uploadFilesModel.uploadMethod.call(files, this.form);
      if (result && this.uploadFilesModel.onSuccess) {
        this.uploadFilesModel.onSuccess.call(this.form);
        this.uploadFilesModel.uploadText = 'Upload another';
      } else if (!result && this.uploadFilesModel.onFailed) {
        this.uploadFilesModel.onFailed.call(this.form);
        this.uploadFilesModel.uploadText = 'Upload';
      }
      this.fileInput.nativeElement.value = '';
    }
  }

  uploadFilesTrigger(){
    if(!this.uploadFilesModel.isDisabled(this.form)){
      this.fileInput.nativeElement.click();
    }
  }
}
