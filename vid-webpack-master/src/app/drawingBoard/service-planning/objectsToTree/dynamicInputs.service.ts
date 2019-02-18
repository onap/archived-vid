import {Injectable} from "@angular/core";
import {Utils} from "../../../shared/utils/utils";
import {Constants} from "../../../shared/utils/constants";
import Parameter = Constants.Parameter;

@Injectable()
export class DynamicInputsService {
  public getArbitraryInputs(inputs) {
    let parameter;
    let parameterList = [];
    for (let key in inputs) {
      parameter = {
        id: key,
        type: Parameter.STRING,
        name: key,
        value: inputs[key][Parameter.DEFAULT],
        isRequired: inputs[key][Parameter.REQUIRED],
        description: inputs[key][Parameter.DESCRIPTION]
      };
      switch (inputs[key][Parameter.TYPE]) {
        case Parameter.INTEGER:
          parameter.type = Parameter.NUMBER;
          break;
        case Parameter.BOOLEAN:
          parameter.type = Parameter.BOOLEAN;
          break;
        case Parameter.RANGE:
          break;
        case Parameter.LIST:
          parameter.type = Parameter.LIST;
          break;
        case Parameter.MAP:
          parameter.type = Parameter.MAP;
          break;
      }
      if (Utils.hasContents(inputs[key][Parameter.CONSTRAINTS])
        && ( inputs[key][Parameter.CONSTRAINTS].length > 0 )) {
        let constraintsArray = inputs[key][Parameter.CONSTRAINTS];
        this.addConstraintParameters(parameterList, constraintsArray, key, inputs, parameter);
      }
      else {

        parameterList.push(parameter);
      }
    }
    return parameterList;
  }

  private addConstraintParameters(parameterList, constraintsArray, key, inputs, parameter) {
    // If there are constraints and the operator is "valid_values",
    // use a select parameter type.
    let i: number = constraintsArray.length;
    let parameterPushed: boolean = false;
    if (i > 0) {
      while ((i--) && (!parameterPushed)) {
        let keys = Object.keys(constraintsArray[i]);
        for (let operator in keys) {
          switch (keys[operator]) {
            case Parameter.VALID_VALUES:
              let j: number = constraintsArray[i][Parameter.VALID_VALUES].length;
              if (j > 0) {
                let oList = [];
                let option;
                while (j--) {
                  option = {
                    name: constraintsArray[i][Parameter.VALID_VALUES][j],
                    isDefault: false
                  };
                  if ((Utils.hasContents(inputs[key][Parameter.DEFAULT]) )
                    && (inputs[key][Parameter.DEFAULT] === constraintsArray[i][Parameter.VALID_VALUES][j] )) {
                    option = {
                      name: constraintsArray[i][Parameter.VALID_VALUES][j],
                      isDefault: true
                    }
                  }
                  oList.push(option);
                }
                parameter.type = Parameter.SELECT;
                parameter.optionList = oList;
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;

            case Parameter.EQUAL:
              if (constraintsArray[i][Parameter.EQUAL] != null) {
                parameter.type = Parameter.STRING;
                parameter.isReadOnly = true;
                parameter.value = constraintsArray[i][Parameter.EQUAL];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;

            case Parameter.LENGTH:
              if (constraintsArray[i][Parameter.LENGTH] != null) {
                parameter.minLength = constraintsArray[i][Parameter.LENGTH];
                parameter.maxLength = constraintsArray[i][Parameter.LENGTH];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
            case Parameter.MAX_LENGTH:
              if (constraintsArray[i][Parameter.MAX_LENGTH] != null) {
                parameter.maxLength = constraintsArray[i][Parameter.MAX_LENGTH];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
            case Parameter.MIN_LENGTH:
              if (constraintsArray[i][Parameter.MIN_LENGTH] != null) {
                parameter.minLength = constraintsArray[i][Parameter.MIN_LENGTH];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
            case Parameter.IN_RANGE:
              if (constraintsArray[i][Parameter.IN_RANGE] != null) {
                if (constraintsArray[i][Parameter.IN_RANGE].length > 1) {
                  parameter.min = constraintsArray[i][Parameter.IN_RANGE][0];
                  parameter.max = constraintsArray[i][Parameter.IN_RANGE][1];
                  parameter.type = Parameter.NUMBER;
                  parameter.value = inputs[key][Parameter.DEFAULT];
                  parameterList.push(parameter);
                  parameterPushed = true;
                }
              }
              break;
            case Parameter.GREATER_THAN:
              if (constraintsArray[i][Parameter.GREATER_THAN] != null) {
                parameter.type = Parameter.NUMBER;
                parameter.min = constraintsArray[i][Parameter.GREATER_THAN];
                parameter.value = inputs[key][Parameter.DEFAULT];
                parameterList.push(parameter);
                parameterPushed = true;
              }
              break;
          }
        }
      }
    }
  };
}
