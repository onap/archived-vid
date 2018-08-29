import {FormControl, ValidatorFn} from "@angular/forms";
import {CustomValidators} from "./uniqueName.validator";
import {FileUnit} from "../../components/formControls/component/file/fileUnit.enum";


describe('Custom Validator Functions', () => {
  test('isValidJson Validator should return null when value is valid and correct object when invalid', () => {

    let isValidJsonFunction: ValidatorFn = CustomValidators.isValidJson();

    expect(isValidJsonFunction(new FormControl("{ 'key': 'value', 'array': ['first',]}"))).toEqual({ isValidJson: true });

    expect(isValidJsonFunction(new FormControl('{ "key": "value", "array": ["first"]'))).toEqual({ isValidJson: true });

    expect(isValidJsonFunction(new FormControl('{ "key": "value", "array": ["first"]}'))).toEqual(null);

    expect(isValidJsonFunction(new FormControl('{ "key": "value", "array": "first"}'))).toEqual(null);
  });

  test('isStringContainTags Validator should return null when value is valid and correct object when invalid', () => {

    const isStringContainTagsFunction: ValidatorFn = CustomValidators.isStringContainTags();

    expect(isStringContainTagsFunction(new FormControl('<asdf>'))).toEqual({isStringContainTags : true});

    expect(isStringContainTagsFunction(new FormControl('1234<asfd56'))).toEqual(null);
  });

  test('isFileTooBig Validator should return null when value is valid and correct object when invalid', () => {

    let isFileTooBigFunction: ValidatorFn = CustomValidators.isFileTooBig(FileUnit.MB, 5);

    expect(isFileTooBigFunction(new FormControl({ size: 6000001 }))).toEqual({ isFileTooBig: true });

    expect(isFileTooBigFunction(new FormControl({ size: 4000000}))).toEqual(null);

    isFileTooBigFunction = CustomValidators.isFileTooBig(FileUnit.B, 5);

    expect(isFileTooBigFunction(new FormControl({ size: 4000000}))).toEqual({ isFileTooBig: true });
  })
});
