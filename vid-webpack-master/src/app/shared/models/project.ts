interface ProjectResponseInterface {
  id: string,
  name: string
}

export class Project {
  id: string;
  name: string;

  constructor(projectResponse: ProjectResponseInterface){
    this.id = projectResponse.id;
    this.name = projectResponse.name;
  }
}
