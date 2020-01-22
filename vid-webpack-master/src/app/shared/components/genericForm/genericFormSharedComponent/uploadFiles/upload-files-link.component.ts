import {Component, ElementRef, Input, OnInit, ViewChild} from "@angular/core";
import {FileItem, FileUploader} from "ng2-file-upload";
import {UploadFilesLinkModel} from "./upload-files-link.model";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'upload-files',
  templateUrl: './upload-files-link.component.html',
  styleUrls: ['./upload-files-link.component.scss']
})
export class UploadFilesLinkComponent implements OnInit {
  uploader: FileUploader;
  @Input() uploadFilesModel: UploadFilesLinkModel;
  @Input() form: FormGroup;
  @ViewChild('fileInput', {static: false}) fileInput: ElementRef;

  ngOnInit(): void {
    this.uploader = new FileUploader({});

    this.uploader.onAfterAddingAll = async (files: FileItem[]) => {
      const result = await this.uploadFilesModel.uploadMethod.call(files, this.form);
      if (result && this.uploadFilesModel.onSuccess) {
        this.uploadFilesModel.onSuccess.call(this.form);
      } else if (!result && this.uploadFilesModel.onFailed) {
        this.uploadFilesModel.onFailed.call(this.form);
      }
      this.uploadFilesModel.uploadText = result ? 'Upload another' : 'Upload'
    };
    this.resetUpload();
  }

  resetUpload(): void {
    this.fileInput.nativeElement.value = '';
  }

  uploadFilesTrigger() {
    if (this.uploadFilesModel.isDisabled && !this.uploadFilesModel.isDisabled(this.form)) {
      this.fileInput.nativeElement.click();
    }
  }
}
