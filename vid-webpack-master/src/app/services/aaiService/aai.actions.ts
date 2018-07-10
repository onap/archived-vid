import {Action, ActionCreator} from "redux";

export const LOAD_PRODUCT_FAMILIES = '[PRODUCT_FAMILIES] Load';

export const LOAD_LCP_TENANT = '[LCP_TENANT] Load';

export const LOAD_AIC_ZONES = '[AIC_ZONES] Load';

export const LOAD_CATEGORY_PARAMETERS = '[LOAD_CATEGORY_PARAMETERS] Load';

export const LOAD_SERVICE_MDOEL_BY_UUID = '[LOAD_SERVICE_MDOEL_BY_UUID] Load';

export const LOAD_NETWORK_ACCORDING_TO_NF = '[LOAD_NETWORK_ACCORDING_TO_NF] Load'

export const LOAD_USER_ID = '[LOAD_USER_ID] Load'


export interface LoadProductFamiliesAction extends Action {}

interface LoadLcpTenant extends Action {}

interface LoadAicZones extends Action {}

interface LoadCategoryParameters extends Action {}

interface LoadServiceModelByUuid extends Action {}

interface LoadNetworkAccordingToNetworkCF extends Action{}

interface LoadUserId extends Action{}


export const loadServiceAccordingToUuid : ActionCreator<LoadServiceModelByUuid> =
 (uuid : string) =>({
   type : LOAD_SERVICE_MDOEL_BY_UUID,
   modelId : uuid
 })


export const loadProductFamiliesAction: ActionCreator<LoadProductFamiliesAction> =
  () => ({
    type: LOAD_PRODUCT_FAMILIES,
  });


export const loadUserId: ActionCreator<LoadUserId> =
() => ({
  type: LOAD_USER_ID,
});


  export const loadLcpTenant: ActionCreator<LoadLcpTenant> =
  () => ({
    type: LOAD_LCP_TENANT,
  });


export const loadAicZones: ActionCreator<LoadAicZones> =
  () => ({
    type: LOAD_AIC_ZONES,
  });

export const loadCategoryParameters: ActionCreator<LoadCategoryParameters> =
  () => ({
    type: LOAD_CATEGORY_PARAMETERS,
  });


export const loadAaiNetworkAccordingToNetworkCF: ActionCreator<LoadNetworkAccordingToNetworkCF> =
  (networkFunction,cloudOwner,cloudRegionId) => ({
    type: LOAD_NETWORK_ACCORDING_TO_NF,
    networkFunctions: networkFunction,
    cloudOwner: cloudOwner,
    cloudRegionId: cloudRegionId
  });
