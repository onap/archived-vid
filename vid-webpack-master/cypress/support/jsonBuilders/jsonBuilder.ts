export class JsonBuilder<T> implements IJsonBuilder<T>{
  currentValue: T;

  public basicJson(json: JSON, url: string, status: number, delay: number, alias: string, changeResFunc?: Function) : void {
    this.currentValue = <T>JSON.parse(JSON.stringify(json));
    this.currentValue = changeResFunc ? changeResFunc(this.currentValue) : this.currentValue;
    return this.initMockCall(url, status, delay, alias);
  }

  public initMockCall(url: string, status: number, delay: number, alias: string) {
      cy.server()
        .route({
          method: 'GET',
          status: status,
          delay : delay ? delay : 0,
          url: url,
          response: JSON.stringify(this.currentValue)
        }).as(alias);
  }
  public basicMock(jsonPath: string, url: string) {
    cy.readFile(jsonPath).then((res) => {
      this.basicJson(res, url, 200, 0, url,);
    })
  }
}

export interface IJsonBuilder<T>{
  basicJson(json: JSON, url: string, status: number, delay: number, alias: string, changeResFunc?: Function) : void;
  initMockCall(url: string, status: number, delay: number, alias: string): void;
  basicMock(jsonPath: string, url: string): void;
}
