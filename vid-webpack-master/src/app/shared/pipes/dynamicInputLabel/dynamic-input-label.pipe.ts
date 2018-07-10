import {PipeTransform, Pipe} from '@angular/core';

@Pipe({ name: 'dynamicInputLabel' })
export class DynamicInputLabelPipe implements PipeTransform {
  transform(text: string): string {
    let split_label = text.toLowerCase().replace(/_/g,' ');
    let uppercase_vnf = split_label.replace(/\bvnf\b/ig, 'VNF');
    let uppercase_nf = uppercase_vnf.replace(/\bnf\b/ig, 'NF');
    let capitalize_sentence = uppercase_nf.charAt(0).toUpperCase() + uppercase_nf.slice(1);
    return capitalize_sentence + ':*';
  }
}
