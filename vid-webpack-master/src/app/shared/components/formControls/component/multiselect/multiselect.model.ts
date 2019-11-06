export class MultiSelectItem {
  id : number;
  itemId : number;
  itemName : string;

  constructor(genericId: number, itemId : number, itemName : string){
    this.id = genericId;
    this.itemId = itemId;
    this.itemName = itemName;
  }
}
