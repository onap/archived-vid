import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MockNgRedux, NgReduxTestingModule} from "@angular-redux/store/testing";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {ConfigurationModelInfo} from "./configuration.model.info";
import {SharedTreeService} from "../../shared.tree.service";

describe('Vnf Model Info', () => {
  let injector;
  let  _dynamicInputsService : DynamicInputsService;
  let  _sharedTreeService : SharedTreeService;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        MockNgRedux,
        SharedTreeService,
        DynamicInputsService]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    _sharedTreeService = injector.get(SharedTreeService);
  })().then(done).catch(done.fail));

  test('ConfigurationModelInfo should be defined', () => {
    expect(ConfigurationModelInfo).toBeDefined();
  });

  test('ConfigurationModelInfo should defined extra details', () => {
    let configurationModelInfo: ConfigurationModelInfo = new ConfigurationModelInfo(_dynamicInputsService, _sharedTreeService);
    expect(configurationModelInfo.name).toEqual('configurations');
    expect(configurationModelInfo.type).toEqual('Configuration');
  });

  test('isEcompGeneratedNaming should return false', () => {
    let configurationModelInfo: ConfigurationModelInfo = new ConfigurationModelInfo(_dynamicInputsService, _sharedTreeService);
    let isEcompGeneratedNaming: boolean = configurationModelInfo.isEcompGeneratedNaming();
    expect(isEcompGeneratedNaming).toBeTruthy();
  });

  test('getTooltip should return "Configuration"', () => {
    let configurationModelInfo: ConfigurationModelInfo = new ConfigurationModelInfo(_dynamicInputsService, _sharedTreeService);
    let tooltip: string = configurationModelInfo.getTooltip();
    expect(tooltip).toEqual('Configuration');
  });

  test('getType should return "Configuration"', () => {
    let configurationModelInfo: ConfigurationModelInfo = new ConfigurationModelInfo(_dynamicInputsService, _sharedTreeService);
    let tooltip: string = configurationModelInfo.getType();
    expect(tooltip).toEqual('Configuration');
  });

  test('getModel should return Configuration model', () => {
    let configurationModelInfo: ConfigurationModelInfo = new ConfigurationModelInfo(_dynamicInputsService, _sharedTreeService);
    let model = configurationModelInfo.getModel({ uuid: 'foo' });
    expect(model.uuid).toEqual('foo');
  });

});
