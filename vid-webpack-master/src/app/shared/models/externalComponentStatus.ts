export class ExternalComponentStatus {
  component: string;
  available: boolean;
  metadata: any;

  constructor(component: string, available: boolean, metadata: any) {
    this.component = component;
    this.available = available;
    this.metadata = metadata;
  }
}
