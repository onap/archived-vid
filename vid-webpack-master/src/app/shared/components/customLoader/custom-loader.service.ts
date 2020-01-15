import {Injectable} from "@angular/core";
import {LoaderComponent} from "./custom-loader.component";

@Injectable()
export class LoaderService {

  private mainLoaderName = 'general';
  public registeredLoaders = {};

  register(name: string, loader: LoaderComponent) {
    if (!this.registeredLoaders[name]) {
      this.registeredLoaders[name] = loader;
    }
  }

  unregister(name: string) {
    if (this.registeredLoaders[name]) {
      delete this.registeredLoaders[name];
    }
  }

  activate(name: string = this.mainLoaderName) {
    if (this.registeredLoaders[name]) {
      this.registeredLoaders[name].activate();
    }
  }

  deactivate(name: string = this.mainLoaderName) {
    if (this.registeredLoaders[name]) {
      this.registeredLoaders[name].deactivate();
    }
  }

}
