export class InstantiationBase {
  readonly jobId: string;
  readonly serviceModelId: string;

  constructor(data) {
    this.jobId = data.jobId;
    this.serviceModelId = data.serviceModelId;
  }
}
