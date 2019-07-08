export enum ServiceInstanceActions{
  Delete = "Delete",
  Update="Update",
  Create="Create",
  None="None",
  Resume = "Resume",
  Update_Delete = 'Update_Delete',
  None_Delete = 'None_Delete'
}
export enum ServiceAction {
  INSTANTIATE = 'INSTANTIATE',
  DELETE = 'DELETE',
  UPDATE = 'UPDATE'
}
export enum JobStatus {
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  IN_PROGRESS = 'IN_PROGRESS',
  RESOURCE_IN_PROGRESS = 'RESOURCE_IN_PROGRESS',
  PAUSE = 'PAUSE',
  PENDING = 'PENDING',
  STOPPED = 'STOPPED',
  COMPLETED_WITH_ERRORS = 'COMPLETED_WITH_ERRORS',
  CREATING = 'CREATING'
}
