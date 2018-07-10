export interface Subscription {
  'service-type': string;
  'is-permitted': boolean;
}

interface ServiceSubscriptions {
  'service-subscription': Subscription[];
}

export interface GetSubDetailsResponse {
  'service-subscriptions': ServiceSubscriptions;
}
