import {
  GeneralActions, UpdateAicZonesAction,
  UpdateLcpRegionsAndTenantsAction,
  UpdateProductFamiliesAction,
  UpdateSubscribersAction, UpdateUserIdAction
} from "./general.actions";
import {SelectOption} from "../../../models/selectOption";


describe('general actions', () => {
  test('#UPDATE_LCP_REGIONS_AND_TENANTS : action', () => {
    const action: UpdateLcpRegionsAndTenantsAction = <UpdateLcpRegionsAndTenantsAction>{
      type: GeneralActions.UPDATE_LCP_REGIONS_AND_TENANTS,
      lcpRegionsAndTenants: {
        "lcpRegionList": [],
        "lcpRegionsTenantsMap": {}
      }
    };

    expect(action.type).toEqual(GeneralActions.UPDATE_LCP_REGIONS_AND_TENANTS);
    expect(action.lcpRegionsAndTenants.lcpRegionList).toEqual([]);
    expect(action.lcpRegionsAndTenants.lcpRegionsTenantsMap).toEqual({});
  });

  test('#UPDATE_SUBSCRIBERS : action', () => {
    const action: UpdateSubscribersAction = <UpdateSubscribersAction>{
      type: GeneralActions.UPDATE_SUBSCRIBERS,
      subscribers: [
        {
          "id": "ERICA5779-Subscriber-2",
          "name": "ERICA5779-Subscriber-2",
          "isPermitted": false
        },
        {
          "id": "ERICA5779-Subscriber-3",
          "name": "ERICA5779-Subscriber-3",
          "isPermitted": false
        },
        {
          "id": "ERICA5779-Subscriber-4",
          "name": "ERICA5779-Subscriber-5",
          "isPermitted": false
        },
        {
          "id": "ERICA5779-TestSub-PWT-101",
          "name": "ERICA5779-TestSub-PWT-101",
          "isPermitted": false
        },
        {
          "id": "ERICA5779-TestSub-PWT-102",
          "name": "ERICA5779-TestSub-PWT-102",
          "isPermitted": false
        },
        {
          "id": "ERICA5779-TestSub-PWT-103",
          "name": "ERICA5779-TestSub-PWT-103",
          "isPermitted": false
        },
        {
          "id": "31739f3e-526b-11e6-beb8-9e71128cae77",
          "name": "CRAIG/ROBERTS",
          "isPermitted": false
        },
        {
          "id": "DHV1707-TestSubscriber-2",
          "name": "DALE BRIDGES",
          "isPermitted": false
        },
        {
          "id": "jimmy-example",
          "name": "JimmyExampleCust-20161102",
          "isPermitted": false
        },
        {
          "id": "jimmy-example2",
          "name": "JimmyExampleCust-20161103",
          "isPermitted": false
        },
        {
          "id": "CAR_2020_ER",
          "name": "CAR_2020_ER",
          "isPermitted": true
        },
        {
          "id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
          "name": "Emanuel",
          "isPermitted": false
        },
        {
          "id": "21014aa2-526b-11e6-beb8-9e71128cae77",
          "name": "JULIO ERICKSON",
          "isPermitted": false
        },
        {
          "id": "DHV1707-TestSubscriber-1",
          "name": "LLOYD BRIDGES",
          "isPermitted": false
        },
        {
          "id": "e433710f-9217-458d-a79d-1c7aff376d89",
          "name": "SILVIA ROBBINS",
          "isPermitted": true
        }
      ]
    };

    expect(action.type).toEqual(GeneralActions.UPDATE_SUBSCRIBERS);
    expect(action.subscribers).toHaveLength(15);
  });

  test('#UPDATE_PRODUCT_FAMILIES : action', () => {
    const action: UpdateProductFamiliesAction = <UpdateProductFamiliesAction>{
      type: GeneralActions.UPDATE_PRODUCT_FAMILIES,
      productFamilies: [
        new SelectOption({
          id : 'id',
          name : 'name',
          isPermitted : false
        })
      ]
    };

    expect(action.type).toEqual(GeneralActions.UPDATE_PRODUCT_FAMILIES);
    expect(action.productFamilies).toHaveLength(1);
  });


  test('#UpdateAicZonesAction : action', () => {
    const action: UpdateAicZonesAction = <UpdateAicZonesAction>{
      type: GeneralActions.UPDATE_AIC_ZONES,
      aicZones: [
        new SelectOption({
          id : 'id',
          name : 'name',
          isPermitted : false
        })
      ]
    };

    expect(action.type).toEqual(GeneralActions.UPDATE_AIC_ZONES);
    expect(action.aicZones).toHaveLength(1);
  });

  test('#UpdateUserIdAction : action', () => {
    const action: UpdateUserIdAction = <UpdateUserIdAction>{
      type: GeneralActions.UPDATE_USER_ID,
      userId: "userId"
    };

    expect(action.type).toEqual(GeneralActions.UPDATE_USER_ID);
    expect(action.userId).toBe("userId");
  });

});



