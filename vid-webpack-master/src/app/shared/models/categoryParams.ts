import {SelectOptionInterface} from "./selectOption";

export class CategoryParams {
  owningEntityList: SelectOptionInterface[];
  projectList: SelectOptionInterface[];
  lineOfBusinessList: SelectOptionInterface[];
  platformList: SelectOptionInterface[];

  constructor(owningEntityList=[], projectList=[], lob=[], platform=[]){
    this.owningEntityList = owningEntityList;
    this.projectList = projectList;
    this.lineOfBusinessList = lob;
    this.platformList = platform;
  }
}
