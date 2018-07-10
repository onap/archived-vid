import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DynamicInputsComponent } from '../components/dynamic-inputs/dynamic-inputs.component';
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { DynamicInputLabelPipe } from '../shared/pipes/dynamicInputLabel/dynamic-input-label.pipe';


@NgModule({
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule, AngularMultiSelectModule
  ],
  providers: [],
  declarations: [ DynamicInputLabelPipe, DynamicInputsComponent ],
  entryComponents: [],
  exports: [ DynamicInputLabelPipe, DynamicInputsComponent ]

})

export class InputsModule { }
