import {ElementRef, Injectable, Renderer2, RendererFactory2} from "@angular/core";
import {ConfigurationService} from "../../shared/services/configuration.service";

@Injectable()
export class FeatureFlagService{
  private features :  { [key: string]: boolean } = {};
  private renderer: Renderer2;
  constructor(private _configurationService: ConfigurationService,
              rendererFactory: RendererFactory2){
    this.renderer = rendererFactory.createRenderer(null, null);
    this._configurationService.getFlags().subscribe((res: { [key: string]: boolean }) =>{
      this.features = res;
    })
  }


  isFeatureOn(feature : string) : boolean {
    return this.features && this.getFeatureFlag()[feature] === true;
  }

  getFeatureFlag() : { [key: string]: boolean } {
    return this.features;
  }

  hideElement(element: ElementRef) {
    this.renderer.setStyle(element.nativeElement, 'display', 'none');
  }
}
