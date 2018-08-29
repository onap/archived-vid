import {Component} from '@angular/core';

@Component({
  selector: 'app-support',
  template: `
    <ul>
      <li><a href="../../version">Version</a></li>
      <li><a href="../../flags">Feature Flags</a></li>
      <li><a href="#/healthStatus">Probe</a></li>
      <li>
        <form action="../../rest/models/reset" method="post">
            <button name="upvote" value="Upvote"></button>
        </form>
      </li>
    </ul>
  `,
})

export class SupportComponent {
}
