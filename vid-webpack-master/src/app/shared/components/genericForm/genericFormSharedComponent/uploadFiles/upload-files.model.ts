import {FileItem} from "ng2-file-upload";

export class UploadFilesModel {
  uploadText?: string;

  /*********************************************************************
   Implement success method - run after uploadMethod return true result
   **********************************************************************/
  onSuccess?: (...args) => void;

  /*********************************************************************
   Implement failed method - run after uploadMethod return false result
   **********************************************************************/
  onFailed?: (...args) => void;

  /*********************************************************************************
   Implement upload method and return the upload result status (false/true)
   *********************************************************************************/
  uploadMethod: (file: FileItem, ...args) => Promise<boolean>;

  /********************************
   Should upload file be disabled
   ********************************/
  isDisabled?: (...args) => boolean;

  /********************************
   a tag data test id
   ********************************/
  dataTestId : string;
}
