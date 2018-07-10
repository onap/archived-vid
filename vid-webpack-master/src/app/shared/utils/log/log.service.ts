import {isDevMode} from "@angular/core";
declare var console: any;

export interface ILogger {
  assert(...args: any[]): void;
  error(...args: any[]): void;
  group(...args: any[]): void;
  groupEnd(...args: any[]): void;
  info(...args: any[]): void;
  log(...args: any[]): void;
  warn(...args: any[]): void;
}

export class LogService implements ILogger {

  isProduction : boolean = !isDevMode();
  public assert(...args: any[]): void {
    console.assert(LogService.getPrefixLog(...args));
  }

  public error(...args: any[]): void {
    console.error(LogService.getPrefixLog(...args));
  }

  public group(...args: any[]): void {
    console.group(LogService.getPrefixLog(...args));
  }

  public groupEnd(...args: any[]): void {
    console.groupEnd(LogService.getPrefixLog(...args));
  }

  public info(...args: any[]): void {
    console.info(LogService.getPrefixLog(...args));
  }

  public log(...args: any[]): void {
    if(!this.isProduction){
      console.log(LogService.getPrefixLog(...args));
    }
  }

  public warn(...args: any[]): void {
    console.warn(LogService.getPrefixLog(...args));
  }

  static getPrefixLog(...args :any[]){
    return {
      time : new Date(),
      message : args[0],
      data : args.length > 0 ? args[1] : ''
    };
  }
}




