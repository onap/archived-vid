interface OwningEntityResponse {
  id: string,
  name: string
}

export class OwningEntity {
  id: string;
  name: string;

  constructor(serviceJson: OwningEntityResponse){
    this.id = serviceJson.id;
    this.name = serviceJson.name;
  }
}
