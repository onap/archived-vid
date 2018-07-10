
import { TestBed, getTestBed} from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { ServicePopupService } from './service-popup.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NumbersLettersUnderscoreValidator } from '../../shared/components/validators/numbersLettersUnderscore/numbersLettersUnderscore.validator';

describe('Service Popup Service', () => {
  let injector;
  let service: ServicePopupService;
  let httpMock: HttpTestingController;
  let form : FormGroup;
  let servicePopupDataModel;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ServicePopupService]
    });

    injector = getTestBed();
    service = injector.get(ServicePopupService);
    httpMock = injector.get(HttpTestingController);

    form  = generateFormGroup();
    servicePopupDataModel =  generateServicePopupDataModel();
  });

  describe('#resetDynamicInputs', () => {
    it('resetDynamicInputs should reset dymanic fields',(done: DoneFn) => {
      const dynamicInputs = generateDynamicInputs();
      let serviceForm = generateFormGroup();
      serviceForm.addControl(dynamicInputs[0].name, new FormControl({value: dynamicInputs[0].value, disabled: false}));

      serviceForm.controls[dynamicInputs[0].name].setValue("diffValue");
      service.resetDynamicInputs({
        serviceInstanceDetailsFormGroup : serviceForm,
        dynamicInputs : dynamicInputs
      }, dynamicInputs);

      expect(serviceForm.controls[dynamicInputs[0].name].value).toEqual(dynamicInputs[0].value);
      done();
    })
  });

  describe('#onControlError', () => {

    it('should return true if instanceName is illegal', (done: DoneFn) => {
      form.controls['instanceName'].setValue("illegal - illegal");

      let result : boolean = service.onControlError(<any>servicePopupDataModel, form);
      expect(result).toBeTruthy();
      done();
    });

    it('should return false if instanceName is legal', (done: DoneFn) => {

      form.controls['instanceName'].setValue("legal");
      let result  = service.onControlError(<any>servicePopupDataModel, form);
      expect(result).toBeFalsy();
      done();
    });

    it('should return false if lcpRegions is empty and is disabled', (done: DoneFn) => {
      servicePopupDataModel.servicePopupDataModel['lcpRegions'] = [];
      let result  = service.onControlError(<any>servicePopupDataModel, form);
      expect(result).toBeFalsy();
      done();
    });

    it('should return true if lcpRegions is empty', (done: DoneFn) => {
      servicePopupDataModel.servicePopupDataModel['lcpRegions'] = [];
      form.controls['lcpCloudRegionId'].enable();
      let result  = service.onControlError(<any>servicePopupDataModel, form);
      expect(result).toBeTruthy();
      done()
    });
  });


  function generateDynamicInputs(){
    return JSON.parse('[{"id":"2017488_adiodvpe0_ASN","type":"string","name":"2017488_adiodvpe0_ASN","value":"AV_vPE","isRequired":true,"description":"AV/PE"}]');
  }


  function generateServicePopupDataModel() {
    return {
      "servicePopupDataModel" : JSON.parse('{"subscribers":[{"id":"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb","name":"Mobility","isPermitted":false},{"id":"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fc","name":"PACKET CORE","isPermitted":false},{"id":"e433710f-9217-458d-a79d-1c7aff376d89","name":"USP VOICE","isPermitted":true}],"serviceTypes":[{"id":"0","name":"vFlowLogic","isPermitted":false},{"id":"1","name":"VIRTUAL USP","isPermitted":true},{"id":"2","name":"Mobility","isPermitted":false},{"id":"3","name":"vBNG","isPermitted":false},{"id":"4","name":"vVoiceMail","isPermitted":false},{"id":"5","name":"Nimbus","isPermitted":false},{"id":"6","name":"vSEGW","isPermitted":false},{"id":"7","name":"vVM","isPermitted":false},{"id":"8","name":"vOTA","isPermitted":false},{"id":"9","name":"vMME","isPermitted":false},{"id":"10","name":"vMNS","isPermitted":false},{"id":"11","name":"vSCP","isPermitted":false},{"id":"12","name":"VPMS","isPermitted":false},{"id":"13","name":"vMMSC","isPermitted":false},{"id":"14","name":"SSD","isPermitted":false},{"id":"15","name":"vMOG","isPermitted":false},{"id":"16","name":"FIRSTNET","isPermitted":false},{"id":"17","name":"ACTIVE_CHARGE","isPermitted":false},{"id":"18","name":"vHSS","isPermitted":false}],"aicZones":[{"id":"NFT1","name":"NFTJSSSS-NFT1"},{"id":"JAG1","name":"YUDFJULP-JAG1"},{"id":"YYY1","name":"UUUAIAAI-YYY1"},{"id":"BAN1","name":"VSDKYUTP-BAN1"},{"id":"DKJ1","name":"DKJSJDKA-DKJ1"},{"id":"MCS1","name":"ASACMAMS-MCS1"},{"id":"UIO1","name":"uioclli1-UIO1"},{"id":"RAJ1","name":"YGBIJNLQ-RAJ1"},{"id":"OPA1","name":"opaclli1-OPA1"},{"id":"SDE1","name":"ZXCVBNMA-SDE1"},{"id":"VEN2","name":"FGHJUHIL-VEN2"},{"id":"ORL1","name":"ORLDFLMA-ORL1"},{"id":"JAD1","name":"JADECLLI-JAD1"},{"id":"ZXL1","name":"LWLWCANN-ZXL1"},{"id":"CKL1","name":"CLKSKCKK-CKL1"},{"id":"SDF1","name":"sdfclli1-SDF1"},{"id":"RAD1","name":"RADICAL1-RAD1"},{"id":"KIT1","name":"BHYJFGLN-KIT1"},{"id":"REL1","name":"INGERFGT-REL1"},{"id":"JNL1","name":"CJALSDAC-JNL1"},{"id":"OLK1","name":"OLKOLKLS-OLK1"},{"id":"CHI1","name":"CHILLIWE-CHI1"},{"id":"UUU4","name":"UUUAAAUU-UUU4"},{"id":"TUF1","name":"TUFCLLI1-TUF1"},{"id":"KJN1","name":"CKALDKSA-KJN1"},{"id":"SAM1","name":"SNDGCA64-SAN1"},{"id":"SCK1","name":"SCKSCKSK-SCK1"},{"id":"HJH1","name":"AOEEQQQD-HJH1"},{"id":"HGD1","name":"SDFQWHGD-HGD1"},{"id":"KOR1","name":"HYFLNBVT-KOR1"},{"id":"ATL43","name":"AICLOCID-ATL43"},{"id":"ATL54","name":"AICFTAAI-ATL54"},{"id":"ATL66","name":"CLLIAAII-ATL66"},{"id":"VEL1","name":"BNMLKUIK-VEL1"},{"id":"ICC1","name":"SANJITAT-ICC1"},{"id":"MNT11","name":"WSXEFBTH-MNT11"},{"id":"DEF2","name":"WSBHGTYL-DEF2"},{"id":"MAD11","name":"SDFQWGKL-MAD11"},{"id":"OLG1","name":"OLHOLHOL-OLG1"},{"id":"GAR1","name":"NGFVSJKO-GAR1"},{"id":"SAN22","name":"GNVLSCTL-SAN22"},{"id":"HRG1","name":"HRGHRGGS-HRG1"},{"id":"JCS1","name":"JCSJSCJS-JCS1"},{"id":"DHA12","name":"WSXEDECF-DHA12"},{"id":"HJE1","name":"AOEEWWWD-HJE1"},{"id":"NCA1","name":"NCANCANN-NCA1"},{"id":"IOP1","name":"iopclli1-IOP1"},{"id":"RTY1","name":"rtyclli1-RTY1"},{"id":"KAP1","name":"HIOUYTRQ-KAP1"},{"id":"ZEN1","name":"ZENCLLI1-ZEN1"},{"id":"HKA1","name":"JAKHLASS-HKA1"},{"id":"CQK1","name":"CQKSCAKK-CQK1"},{"id":"SAI1","name":"UBEKQLPD-SAI1"},{"id":"ERT1","name":"ertclli1-ERT1"},{"id":"IBB1","name":"PLMKOIJU-IBB1"},{"id":"TIR2","name":"PLKINHYI-TIR2"},{"id":"HSD1","name":"CHASKCDS-HSD1"},{"id":"SLF78","name":"SDCTLFN1-SLF78"},{"id":"SEE78","name":"SDCTEEE4-SEE78"},{"id":"SAN13","name":"TOKYJPFA-SAN13"},{"id":"SAA78","name":"SDCTAAA1-SAA78"},{"id":"LUC1","name":"ATLDFGYC-LUC1"},{"id":"AMD13","name":"MEMATLAN-AMD13"},{"id":"TOR1","name":"TOROONXN-TOR1"},{"id":"QWE1","name":"QWECLLI1-QWE1"},{"id":"ZOG1","name":"ZOGASTRO-ZOG1"},{"id":"CAL33","name":"CALIFORN-CAL33"},{"id":"SHH78","name":"SDIT1HHH-SHH78"},{"id":"DSA1","name":"LKJHGFDS-DSA1"},{"id":"CLG1","name":"CLGRABAD-CLG1"},{"id":"BNA1","name":"BNARAGBK-BNA1"},{"id":"ATL84","name":"CANTTCOC-ATL84"},{"id":"APP1","name":"WBHGTYUI-APP1"},{"id":"RJN1","name":"RJNRBZAW-RJN1"},{"id":"EHH78","name":"SDCSHHH5-EHH78"},{"id":"mac10","name":"PKGTESTF-mac10"},{"id":"SXB78","name":"SDCTGXB1-SXB78"},{"id":"SAX78","name":"SDCTAXG1-SAX78"},{"id":"SYD1","name":"SYDNAUBV-SYD1"},{"id":"TOK1","name":"TOKYJPFA-TOK1"},{"id":"KGM2","name":"KGMTNC20-KGM2"},{"id":"DCC1b","name":"POIUYTGH-DCC1b"},{"id":"SKK78","name":"SDCTKKK1-SKK78"},{"id":"SGG78","name":"SDCTGGG1-SGG78"},{"id":"SJJ78","name":"SDCTJJJ1-SJJ78"},{"id":"SBX78","name":"SDCTBXG1-SBX78"},{"id":"LAG1","name":"LARGIZON-LAG1"},{"id":"IAA1","name":"QAZXSWED-IAA1"},{"id":"POI1","name":"PLMNJKIU-POI1"},{"id":"LAG1a","name":"LARGIZON-LAG1a"},{"id":"PBL1","name":"PBLAPBAI-PBL1"},{"id":"LAG45","name":"LARGIZON-LAG1a"},{"id":"MAR1","name":"MNBVCXZM-MAR1"},{"id":"HST70","name":"HSTNTX70-HST70"},{"id":"DCC1a","name":"POIUYTGH-DCC1a"},{"id":"TOL1","name":"TOLDOH21-TOL1"},{"id":"LON1","name":"LONEENCO-LON1"},{"id":"SJU78","name":"SDIT1JUB-SJU78"},{"id":"STN27","name":"HSTNTX01-STN27"},{"id":"SSW56","name":"ss8126GT-SSW56"},{"id":"SBB78","name":"SDIT1BBB-SBB78"},{"id":"DCC3","name":"POIUYTGH-DCC3"},{"id":"GNV1","name":"GNVLSCTL-GNV1"},{"id":"WAS1","name":"WASHDCSW-WAS1"},{"id":"TOY1","name":"TORYONNZ-TOY1"},{"id":"STT1","name":"STTLWA02-STT1"},{"id":"STG1","name":"STTGGE62-STG1"},{"id":"SLL78","name":"SDCTLLL1-SLL78"},{"id":"SBU78","name":"SDIT1BUB-SBU78"},{"id":"ATL2","name":"ATLNGANW-ATL2"},{"id":"BOT1","name":"BOTHWAKY-BOT1"},{"id":"SNG1","name":"SNGPSIAU-SNG1"},{"id":"NYC1","name":"NYCMNY54-NYC1"},{"id":"LAG1b","name":"LARGIZON-LAG1b"},{"id":"AMD15","name":"AMDFAA01-AMD15"},{"id":"SNA1","name":"SNANTXCA-SNA1"},{"id":"PLT1","name":"PLTNCA60-PLT1"},{"id":"TLP1","name":"TLPNXM18-TLP1"},{"id":"SDD81","name":"SAIT1DD6-SDD81"},{"id":"DCC1","name":"POIUYTGH-DCC1"},{"id":"DCC2","name":"POIUYTGH-DCC2"},{"id":"OKC1","name":"OKCBOK55-OKC1"},{"id":"PAR1","name":"PARSFRCG-PAR1"},{"id":"TES36","name":"ABCEETES-TES36"},{"id":"COM1","name":"PLMKOPIU-COM1"},{"id":"ANI1","name":"ATLNGTRE-ANI1"},{"id":"SDG78","name":"SDIT1BDG-SDG78"},{"id":"mac20","name":"PKGTESTF-mac20"},{"id":"DSF45","name":"DSFBG123-DSF45"},{"id":"HST25","name":"HSTNTX01-HST25"},{"id":"AMD18","name":"AUDIMA01-AMD18"},{"id":"SAA80","name":"SAIT9AA3-SAA80"},{"id":"SSA56","name":"SSIT2AA7-SSA56"},{"id":"SDD82","name":"SAIT1DD9-SDD82"},{"id":"JCV1","name":"JCVLFLBW-JCV1"},{"id":"SUL2","name":"WERTYUJK-SUL2"},{"id":"PUR1","name":"purelyde-PUR1"},{"id":"FDE55","name":"FDERT555-FDE55"},{"id":"SITE","name":"LONEENCO-SITE"},{"id":"ATL1","name":"ATLNGAMA-ATL1"},{"id":"JUL1","name":"ZXCVBNMM-JUL1"},{"id":"TAT34","name":"TESAAISB-TAT34"},{"id":"XCP12","name":"CHKGH123-XCP12"},{"id":"RAI1","name":"poiuytre-RAI1"},{"id":"HPO1","name":"ATLNGAUP-HPO1"},{"id":"KJF12","name":"KJFDH123-KJF12"},{"id":"SCC80","name":"SAIT9CC3-SCC80"},{"id":"SAA12","name":"SAIT9AF8-SAA12"},{"id":"SAA14","name":"SAIT1AA9-SAA14"},{"id":"ATL35","name":"TTESSAAI-ATL35"},{"id":"CWY1","name":"CWYMOWBS-CWY1"},{"id":"ATL76","name":"TELEPAAI-ATL76"},{"id":"DSL12","name":"DSLFK242-DSL12"},{"id":"ATL53","name":"AAIATLTE-ATL53"},{"id":"SAA11","name":"SAIT9AA2-SAA11"},{"id":"ATL62","name":"TESSASCH-ATL62"},{"id":"AUG1","name":"ASDFGHJK-AUG1"},{"id":"POI22","name":"POIUY123-POI22"},{"id":"SAA13","name":"SAIT1AA9-SAA13"},{"id":"BHY17","name":"BHYTFRF3-BHY17"},{"id":"LIS1","name":"HOSTPROF-LIS1"},{"id":"SIP1","name":"ZXCVBNMK-SIP1"},{"id":"ATL99","name":"TEESTAAI-ATL43"},{"id":"ATL64","name":"FORLOAAJ-ATL64"},{"id":"TAT33","name":"TESAAISA-TAT33"},{"id":"RAD10","name":"INDIPUNE-RAD10"},{"id":"RTW5","name":"BHYTFRY4-RTW5"},{"id":"JGS1","name":"KSJKKKKK-JGS1"},{"id":"ATL98","name":"TEESTAAI-ATL43"},{"id":"WAN1","name":"LEIWANGW-WAN1"},{"id":"ATL44","name":"ATLSANAB-ATL44"},{"id":"RTD2","name":"BHYTFRk4-RTD2"},{"id":"NIR1","name":"ORFLMANA-NIR1"},{"id":"ATL75","name":"SANAAIRE-ATL75"},{"id":"NUM1","name":"QWERTYUI-NUM1"},{"id":"MTN32","name":"MDTWNJ21-MTN32"},{"id":"RTZ4","name":"BHYTFRZ6-RTZ4"},{"id":"ATL56","name":"ATLSANAC-ATL56"},{"id":"AMS1","name":"AMSTNLBW-AMS1"},{"id":"RCT1","name":"AMSTERNL-RCT1"},{"id":"JAN1","name":"ORFLMATT-JAN1"},{"id":"ABC14","name":"TESAAISA-ABC14"},{"id":"TAT37","name":"TESAAISD-TAT37"},{"id":"MIC54","name":"MICHIGAN-MIC54"},{"id":"ABC11","name":"ATLSANAI-ABC11"},{"id":"AMF11","name":"AMDOCS01-AMF11"},{"id":"ATL63","name":"ATLSANEW-ATL63"},{"id":"ABC12","name":"ATLSECIA-ABC12"},{"id":"MTN20","name":"MDTWNJ21-MTN20"},{"id":"ABC15","name":"AAITESAN-ABC15"},{"id":"AVT1","name":"AVTRFLHD-AVT1"},{"id":"ATL34","name":"ATLSANAI-ATL34"}],"lcpRegions":[{"id":"AAIAIC25","name":"AAIAIC25","isPermitted":true},{"id":"mtn6","name":"mtn6","isPermitted":true}],"lcpRegionsTenantsMap":{},"tenants":[{"id":"bae71557c5bb4d5aac6743a4e5f1d054","name":"AIN Web Tool-15-D-testgamma","isPermitted":true},{"id":"229bcdc6eaeb4ca59d55221141d01f8e","name":"AIN Web Tool-15-D-STTest2","isPermitted":true},{"id":"1178612d2b394be4834ad77f567c0af2","name":"AIN Web Tool-15-D-SSPtestcustome","isPermitted":true},{"id":"19c5ade915eb461e8af52fb2fd8cd1f2","name":"AIN Web Tool-15-D-UncheckedEcopm","isPermitted":true},{"id":"de007636e25249238447264a988a927b","name":"AIN Web Tool-15-D-dfsdf","isPermitted":true},{"id":"62f29b3613634ca6a3065cbe0e020c44","name":"AIN/SMS-16-D-Multiservices1","isPermitted":true},{"id":"649289e30d3244e0b48098114d63c2aa","name":"AIN Web Tool-15-D-SSPST66","isPermitted":true},{"id":"3f21eeea6c2c486bba31dab816c05a32","name":"AIN Web Tool-15-D-ASSPST47","isPermitted":true},{"id":"f60ce21d3ee6427586cff0d22b03b773","name":"CESAR-100-D-sspjg67246","isPermitted":true},{"id":"8774659e425f479895ae091bb5d46560","name":"CESAR-100-D-sspjg68359","isPermitted":true},{"id":"624eb554b0d147c19ff8885341760481","name":"AINWebTool-15-D-iftach","isPermitted":true},{"id":"214f55f5fc414c678059c383b03e4962","name":"CESAR-100-D-sspjg612401","isPermitted":true},{"id":"c90666c291664841bb98e4d981ff1db5","name":"CESAR-100-D-sspjg621340","isPermitted":true},{"id":"ce5b6bc5c7b348e1bf4b91ac9a174278","name":"sspjg621351cloned","isPermitted":true},{"id":"b386b768a3f24c8e953abbe0b3488c02","name":"AINWebTool-15-D-eteancomp","isPermitted":true},{"id":"dc6c4dbfd225474e9deaadd34968646c","name":"AINWebTool-15-T-SPFET","isPermitted":true},{"id":"02cb5030e9914aa4be120bd9ed1e19eb","name":"AINWebTool-15-X-eeweww","isPermitted":true},{"id":"f2f3830e4c984d45bcd00e1a04158a79","name":"CESAR-100-D-spjg61909","isPermitted":true},{"id":"05b91bd5137f4929878edd965755c06d","name":"CESAR-100-D-sspjg621512cloned","isPermitted":true},{"id":"7002fbe8482d4a989ddf445b1ce336e0","name":"AINWebTool-15-X-vdr","isPermitted":true},{"id":"4008522be43741dcb1f5422022a2aa0b","name":"AINWebTool-15-D-ssasa","isPermitted":true},{"id":"f44e2e96a1b6476abfda2fa407b00169","name":"AINWebTool-15-D-PFNPT","isPermitted":true},{"id":"b69a52bec8a84669a37a1e8b72708be7","name":"AINWebTool-15-X-vdre","isPermitted":true},{"id":"fac7d9fd56154caeb9332202dcf2969f","name":"AINWebTool-15-X-NONPODECOMP","isPermitted":true},{"id":"2d34d8396e194eb49969fd61ffbff961","name":"DN5242-Nov16-T5","isPermitted":true},{"id":"cb42a77ff45b48a8b8deb83bb64acc74","name":"ro-T11","isPermitted":true},{"id":"fa45ca53c80b492fa8be5477cd84fc2b","name":"ro-T112","isPermitted":true},{"id":"4914ab0ab3a743e58f0eefdacc1dde77","name":"DN5242-Nov21-T1","isPermitted":true},{"id":"d0a3e3f2964542259d155a81c41aadc3","name":"test-mtn6-09","isPermitted":true},{"id":"cbb99fe4ada84631b7baf046b6fd2044","name":"DN5242-Nov16-T3","isPermitted":true}],"productFamilies":null,"projects":[{"id":"DFW","name":"DFW"},{"id":"x1","name":"x1"},{"id":"yyy1","name":"yyy1"}],"owningEntities":[{"id":"aaa1","name":"aaa1"},{"id":"d61e6f2d-12fa-4cc2-91df-7c244011d6fc","name":"MetroPacketCore"},{"id":"Wireline","name":"Wireline"}],"globalCustomerId":"e433710f-9217-458d-a79d-1c7aff376d89"}')}
  }

  function generateFormGroup(){
    return new FormGroup({
      globalSubscriberId: new FormControl(
        Validators.compose([Validators.required])
      ),
      productFamilyId: new FormControl(),
      subscriptionServiceType: new FormControl({value: null,  disabled: true}, Validators.compose([Validators.required])),
      lcpCloudRegionId: new FormControl({value: null,  disabled: true}, Validators.compose([Validators.required])),
      tenantId: new FormControl({value: null,  disabled: true}, Validators.compose([Validators.required])),
      aicZoneId: new FormControl(),
      projectName: new FormControl(),
      owningEntityId: new FormControl(Validators.compose([Validators.required])),
      instanceName : new FormControl({value: null}, Validators.compose([Validators.required, NumbersLettersUnderscoreValidator.valid]))
    });
  }


  function generateServiceInstanceDetails(){
    return {
      servicePopupDataModel : {
        "productFamilies" : []
      },
      serviceInstanceDetailsFormGroup : {
        controls : {
          productFamilyId : {
            disabled : false
          }
        }
      }
    }
  }

  function generateLegalServiceInstance(){
    return {
      instanceName : "legalInstanceName"
    }
  }

  function generateIllegalServiceInstance(){
    return {
      instanceName : "illegalInstanceName"
    }
  }

});
