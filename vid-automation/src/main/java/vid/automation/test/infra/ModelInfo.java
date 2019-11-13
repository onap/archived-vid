package vid.automation.test.infra;

import com.google.common.collect.ImmutableList;

public class ModelInfo {
    public final String modelVersionId; //aka model uuid
    public final String modelInvariantId;
    public final String zipFileName;

    public static class ModelInfoWithMultipleVersions {
        public final String modelInvariantId;
        public final String modelVersionId1;
        public final String modelVersionId2;
        public final String modelVersionId3;
        public final String modelName;


        ModelInfoWithMultipleVersions(String modelInvariantId, String modelVersionId1, String modelVersionId2, String modelVersionId3, String modelName) {
            this.modelInvariantId = modelInvariantId;
            this.modelVersionId1 = modelVersionId1;
            this.modelVersionId2 = modelVersionId2;
            this.modelVersionId3 = modelVersionId3;
            this.modelName = modelName;
        }

        public static final ModelInfoWithMultipleVersions modelInfoWithMultipleVersions = new ModelInfoWithMultipleVersions(
                "5d353b28-e5b7-419b-98e8-cad5d258be13",
                "5e7970bc-a292-4955-8a1b-8a44f586f527",
                "04743c62-ab58-41a0-bc53-1052ef1c094a",
                "d2415de6-dde3-4737-b9b0-4f9fa02fab68",
                "serviceWithGreatNameForMultiple");
    }

    public ModelInfo(String modelVersionId, String modelInvariantId, String zipFileName) {
        this.modelVersionId = modelVersionId;
        this.modelInvariantId = modelInvariantId;
        this.zipFileName = zipFileName;
    }

    public ModelInfo(String modelVersionId, String modelInvariantId) {
        this(modelVersionId, modelInvariantId, "fakeCsarName.zip");
    }

    public static final ModelInfo macroForBrowseSdc = new ModelInfo("4d71990b-d8ad-4510-ac61-496288d9078e","d27e42cf-087e-4d31-88ac-6c4b7585f800");
    public static final ModelInfo aLaCarteForBrowseSdc = new ModelInfo("4d71990b-d8ad-4510-ac61-496288d9078e","a8dcd72d-d44d-44f2-aa85-53aa9ca99cba");
    public static final ModelInfo serviceWithOneVersion =   new ModelInfo("16a3133f-cd29-44df-aa3c-79a75e40802a", "a313c1fb-b8ce-4e5f-abfa-ad6611203350");

    public static final ModelInfo instantiationTypeAlacarte_vidNotionsInstantiationUIByUUID = new ModelInfo     ("95eb2c44-bff2-4e8b-ad5d-8266870b7717", "31a229a2-71d0-48e1-9003-850c2696d6d4", "csar15782222_instantiationTypeAlacarte_vidNotionsInstantiationUIByUUID.zip");
    public static final ModelInfo macroSriovWithDynamicFieldsEcompNamingTruePartialModelDetails = new ModelInfo ("2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd", "45aee87d-883b-4644-9006-e0ca48d33cbf", "csar-withDynamicFields-ecompNamingTrue-partialModelDetails.zip");
    public static final ModelInfo macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails = new ModelInfo     ("1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd", "cdb90b57-ed78-4d44-a5b4-7f43a02ec632", "csar-noDynamicFields-ecompNamingFalse-fullModelDetails-1a80c596.zip");
    public static final ModelInfo macroSriovWithDynamicFieldsEcompNamingFalsePartialModelDetailsVnfEcompNamingFalse = new ModelInfo("6b528779-44a3-4472-bdff-9cd15ec93450", "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0" , "csar-withDynamicFields-ecompNamingFalse-partialModelDetails-vnfEcompNamingFalse.zip");
    public static final ModelInfo macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetailsVnfEcompNamingFalse = new ModelInfo("4a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd", "4140a873-00bb-4f57-ac46-0494cc9e674a", "csar-noDynamicFields-ecompNamingFalse-fullModelDetails-1a80c596-vnfEcompNamingFalse.zip");
    public static final ModelInfo aLaCarteNetworkProvider5G = new ModelInfo("4659e8bd-0920-4eed-8ec5-550b4c8dceeb", "16e56d12-40b3-4db1-a40e-d48c36679e2e", "service-SrIovProvider1-csar.zip");
    public static final ModelInfo pasqualeVmxVpeBvService488Annotations = new ModelInfo("f4d84bb4-a416-4b4e-997e-0059973630b9", "598e3f9e-3244-4d8f-a8e0-0e5d7a29eda9", "service-PasqualeVmxVpeBvService488-csar-annotations.zip");
    public static final ModelInfo macroDrawingBoardComplexService = new ModelInfo("6e59c5de-f052-46fa-aa7e-2fca9d674c44","cfef8302-d90f-475f-87cc-3f49a62ef14c", "service-Complexservice-csar.zip" );
    public static final ModelInfo aLaCarteServiceCreationTest = new ModelInfo("f913c5d0-206e-45c2-9284-1c68f4e67dc7", "45e61192-876c-4e28-9139-5a0c47410379", "serviceCreationTest.zip");
    public static final ModelInfo aLaCarteServiceCreationNewUI = new ModelInfo("f3862254-8df2-4a0a-8137-0a9fe985860c", "d1068db8-b933-4919-8972-8bc1aed366c8", "service-Vocg1804Svc.zip");
    public static final ModelInfo aLaCarteVnfGroupingService = new ModelInfo("4117a0b6-e234-467d-b5b9-fe2f68c8b0fc", "7ee41ce4-4827-44b0-a48e-2707a59905d2", "csar15782222_instantiationTypeAlacarte_VnfGrouping.zip");
    public static final ModelInfo serviceFabricSriovService = new ModelInfo("253f1467-fe68-4e80-ba71-308000caec31", "c15fe228-7d40-4f99-afa7-10abeedf9aac", "service-fabric-SriovService-csar.zip");
    public static final ModelInfo infrastructureVpnService = new ModelInfo("f028b2e2-7080-4b13-91b2-94944d4c42d8", "dfc2c44c-2429-44ca-ae26-1e6dc1f207fb", "service-Infravpn-csar.zip");
    public static final ModelInfo collectionResourceService = new ModelInfo("abd0cb02-5f97-42cd-be93-7dd3e31a6a64", "04bdd793-32ed-4045-adea-4e096304a067", "csar_collection_resource.zip");
    public static final ModelInfo collectionResourceForResume = new ModelInfo("6e0bec91-09f3-43aa-9cf3-e617cd0146be", "f6342be5-d66b-4d03-a1aa-c82c3094c4ea", "csar_collection_resource_for_resume.zip");
    public static final ModelInfo transportWithPnfsService = new ModelInfo("12550cd7-7708-4f53-a09e-41d3d6327ebc", "561faa57-7bbb-40ec-a81c-c0d4133e98d4", "csarTransportWithPnfs.zip");
    public static final ImmutableList<ModelInfo> superSetOfModelInfos = buildModelInfos();

    public static ImmutableList<ModelInfo> buildModelInfos()  {
        return new ImmutableList.Builder<ModelInfo>()
                .add(macroForBrowseSdc)
                .add(aLaCarteForBrowseSdc)
                .add(serviceWithOneVersion)
                .add(instantiationTypeAlacarte_vidNotionsInstantiationUIByUUID)
                .add(macroSriovWithDynamicFieldsEcompNamingTruePartialModelDetails)
                .add(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails)
                .add(macroSriovWithDynamicFieldsEcompNamingFalsePartialModelDetailsVnfEcompNamingFalse)
                .add(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetailsVnfEcompNamingFalse)
                .add(aLaCarteNetworkProvider5G)
                .add(pasqualeVmxVpeBvService488Annotations)
                .add(macroDrawingBoardComplexService)
                .add(aLaCarteServiceCreationTest)
                .add(aLaCarteVnfGroupingService)
                .add(infrastructureVpnService)
                .add(transportWithPnfsService)
                .add(collectionResourceService)
                .add(collectionResourceForResume)
                .add(aLaCarteServiceCreationNewUI)
                .build();
    }
}


