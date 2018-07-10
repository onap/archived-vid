export interface SubscriptionResponseInterface {
  'service-type': string
  'is-permitted': boolean
}

export class ServiceType {
  id: string;
  name: string;
  isPermitted: boolean;


  constructor(id: string, subscription: SubscriptionResponseInterface){
    this.id = id;
    this.name = subscription['service-type'];
    this.isPermitted = subscription['is-permitted'];
  }
}
