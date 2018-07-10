import { Injectable } from '@angular/core';

import {AAIEpics} from "../services/aaiService/aai.epics";

@Injectable()
export class RootEpics {
  constructor(private aaiEpics: AAIEpics) {}

  public createEpics() {
    return this.aaiEpics.createEpic();
    
  }
}
