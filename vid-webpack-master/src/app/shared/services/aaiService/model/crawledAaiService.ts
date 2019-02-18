
  export interface Child {
    type: string;
    orchestrationStatus: string;
    provStatus: string;
    inMaint: boolean;
    modelVersionId: string;
    modelCustomizationId: string;
    modelInvariantId: string;
    id: string;
    name: string;
    children: any[];
  }

  export interface Root {
    type: string;
    orchestrationStatus: string;
    modelVersionId: string;
    modelCustomizationId: string;
    modelInvariantId: string;
    id: string;
    name: string;
    children: Child[];
  }

  export interface RootObject {
    root: Root;
  }


