import {Component, Input} from '@angular/core';
import {AuditInformationItem} from "../../../models/auditInfoControlModels/auditInformationItems.model";

@Component({
      selector: 'audit-info-modal-table',
    templateUrl: './auditInfoModalTable.component.html',
    styleUrls: ['./auditInfoModalTable.component.scss']
})
export class AuditInfoTableComponent{

  @Input() auditInfoItems: AuditInformationItem = null;

  constructor() { }

  onNavigate(){
        window.open("http://ecompguide.web.att.com:8000/#ecomp_ug/c_ecomp_ops_vid.htmll#r_ecomp_ops_vid_bbglossary", "_blank");
      }

  readOnlyRetryUrl = (): string =>
        `../../serviceModels.htm?more#/servicePlanning/RETRY?serviceModelId=${this.auditInfoItems.serviceModelId}&jobId=${this.auditInfoItems.jobId}`

    }
