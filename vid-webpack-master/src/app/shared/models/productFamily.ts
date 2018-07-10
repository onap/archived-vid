import {ServiceResponseInterface} from "../../services/aaiService/responseInterfaces/getServicesResponseInterface";

export class ProductFamily {
  id: string;
  name: string;
  isPermitted: boolean;

  constructor(serviceResponse: ServiceResponseInterface){
    this.id = serviceResponse['service-id'];
    this.name = serviceResponse["service-description"];
    this.isPermitted = serviceResponse["is-permitted"];
  }
}
