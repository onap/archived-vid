export interface SelectOptionInterface {
  id: string,
  name: string
  isPermitted?: boolean
}

export class SelectOption {
  id: string;
  name: string;
  isPermitted?: boolean;

  constructor(option: SelectOptionInterface){
    this.id = option.id;
    this.name = option.name;
    this.isPermitted = option.isPermitted;
  }
}
