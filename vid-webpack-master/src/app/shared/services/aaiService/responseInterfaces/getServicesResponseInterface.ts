export interface ServiceResponseInterface {
  'service-id': string,
  'service-description': string
  'is-permitted': boolean
}

export interface GetServicesResponseInterface {
  service: ServiceResponseInterface[];
}
