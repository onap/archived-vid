export class Subscriber {
  id: string;
  name: string;
  isPermitted: boolean;

  constructor(subscriberResponse){
    this.id = subscriberResponse['global-customer-id'];
    this.name = subscriberResponse['subscriber-name'];
    this.isPermitted = subscriberResponse['is-permitted'];
  }
}
