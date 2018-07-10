export class DynamicInput<T>{

  id: string;
  name: string;
  type: string;
  description: string;
  value: T;
  prompt: any;
  maxLength: number;
  minLength: number;
  isVisible: boolean;
  isRequired: boolean;
  isEnabled: boolean;
  isReadOnly: boolean;


  constructor(options: {
    id?: string,
    name?: string,
    type: string,
    description?: string,
    value?: T,
    prompt?: any,
    maxLength?: number,
    minLength?: number,
    isVisible?: boolean,
    isRequired?: boolean,
    isEnabled?: boolean,
    isReadOnly?: boolean,
  }) {
    this.id = options.id;
    this.name = options.name || '';
    this.type = options.type;
    this.description = options.description || '';
    this.value = options.value;
    this.prompt = options.prompt;
    this.maxLength = options.maxLength;
    this.minLength = options.minLength;
    this.isVisible = options.isVisible == false? options.isVisible: true;
    this.isEnabled = options.isEnabled == false? options.isEnabled: true;
    this.isRequired = options.isRequired == null? false: options.isRequired;
    this.isReadOnly = options.isReadOnly == null? false: options.isReadOnly;
  }
}

export class DynamicNumber extends DynamicInput<number> {

  max: number;
  min: number;

  constructor(options: {
    id?: string,
    name?: string,
    type: string,
    description?: string,
    value?: number,
    prompt?: any,
    maxLength?: number,
    minLength?: number,
    isVisible?: boolean,
    isRequired?: boolean,
    isEnabled?: boolean,
    isReadOnly?: boolean,
    max?: number,
    min?: number
  }){
    super(options);
    this.max = options.max;
    this.min = options.min;
  }

}

export class DynamicSelect extends DynamicInput<any> {
  optionList: any[];

  constructor(options: {
    id?: string,
    name?: string,
    type: string,
    description?: string,
    value?: any,
    prompt?: any,
    maxLength?: number,
    minLength?: number,
    isVisible?: boolean,
    isRequired?: boolean,
    isEnabled?: boolean,
    isReadOnly?: boolean,
    optionList?: any[]
  }) {
    super(options);
    this.optionList = options.optionList || [];
  }
}

export class DynamicMultiSelect extends DynamicSelect {
  selectedItems: any[];
  settings: any;

  constructor(options: {
    id?: string,
    name?: string,
    type: string,
    description?: string,
    value?: any,
    prompt?: any,
    maxLength?: number,
    minLength?: number,
    isVisible?: boolean,
    isRequired?: boolean,
    isEnabled?: boolean,
    isReadOnly?: boolean,
    settings?: any,
    optionList?: any[],
    selectedItems?: any[]
  }) {
    super(options);
    this.settings = options.settings || {};
    this.selectedItems = options.selectedItems || [];
  }
}

