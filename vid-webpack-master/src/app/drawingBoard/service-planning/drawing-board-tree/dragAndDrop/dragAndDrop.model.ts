/******************************************
 type - node type
 isFirstLevel : node is first level
 ******************************************/

export class DragAndDropModel {
  type : string;
  isFirstLevel : boolean;

  constructor(type : string, isFirstLevel : boolean){
    this.type = type;
    this.isFirstLevel = isFirstLevel;
  }

}
