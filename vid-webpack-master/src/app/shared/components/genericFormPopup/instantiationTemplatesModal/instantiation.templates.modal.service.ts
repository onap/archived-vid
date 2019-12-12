import {Injectable} from "@angular/core";
import {InstantiationTemplatesRowModel} from "./instantiation.templates.row.model";
import {NgRedux} from "@angular-redux/store";
import {Router, UrlTree} from "@angular/router";
import {AppState} from "../../../store/reducers";
import {updateDrawingBoardStatus} from "../../../storeUtil/utils/global/global.actions";
import {DrawingBoardModes} from "../../../../drawingBoard/service-planning/drawing-board.modes";

@Injectable()
export class InstantiationTemplatesModalService {

  constructor(private _router: Router,
              private _store: NgRedux<AppState>,) {
  }

  convertResponseToUI = (jobsResponse: any[]): InstantiationTemplatesRowModel[] => {
    let tableRows: InstantiationTemplatesRowModel[] = [];

    jobsResponse.forEach((job) => {
      tableRows.push(new InstantiationTemplatesRowModel(job));
    });

    return tableRows;
  };

  navigateToNewViewEdit(item: InstantiationTemplatesRowModel, mode: DrawingBoardModes): void {
    this._store.dispatch(updateDrawingBoardStatus(mode));
    const viewEditUrlTree: UrlTree = this.getNewViewEditUrlTree(item, mode);
    this._router.navigateByUrl(viewEditUrlTree);
    window.parent.location.assign(this.getViewEditUrl(viewEditUrlTree));
  }

  getNewViewEditUrlTree(item: InstantiationTemplatesRowModel, mode: DrawingBoardModes): UrlTree {
    return this._router.createUrlTree(
      ['/servicePlanning/' + mode],
      {
        queryParams: {
          serviceModelId: item.serviceModelId,
          jobId: item.jobId
        }
      });
  }

  getViewEditUrl(viewEditUrlTree: UrlTree): string {
    return '../../serviceModels.htm#' + viewEditUrlTree.toString();
  }


}
