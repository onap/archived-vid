import {Utils} from "./utils";
import each from "jest-each";


describe('Util', () => {

  test('hasContents should return false if object is undefined or null or empty', () => {
    expect(Utils.hasContents(undefined)).toBeFalsy();
    expect(Utils.hasContents(null)).toBeFalsy();
    expect(Utils.hasContents("")).toBeFalsy();
  });

  test('hasContents should return true if object is not undefined and not null and not empty', () => {
    expect(Utils.hasContents("someValue")).toBeTruthy();
  });

  const instantiationTypesDataProvider = [
    ['Macro', false ],
    ['ALaCarte', true ],
    ['ClientConfig', true],
    ['dont know', true]
  ];
  each(instantiationTypesDataProvider).test('instantiationType %s isALaCarte shall be %s', (instantiationType, expected ) => {
    expect(Utils.isALaCarte(instantiationType)).toEqual(expected);
  });

  each([
    ["empty properties, empty flags",{}, {}, 1],
    ["null properties, undefined flags",null, undefined, 1],
    ["max_instances 3, flag is on", {max_instances:3}, {FLAG_2002_UNLIMITED_MAX: true}, 3],
    ["max_instances 3, flag is off", {max_instances:3}, {FLAG_2002_UNLIMITED_MAX: false}, 3],
    ["null properties, flag is on", null, {FLAG_2002_UNLIMITED_MAX: true}, null],
    ["null properties, flag is off", null, {FLAG_2002_UNLIMITED_MAX: false}, 1],
    ["undefined properties, flag is off", undefined, {FLAG_2002_UNLIMITED_MAX: false}, 1],
  ]).test('getMaxFirstLevel %s', (desc, properties, flags, expected) => {
    expect(Utils.getMaxFirstLevel(properties, flags)).toEqual(expected);
  });

  each([
    ["empty properties, empty flags",{}, {}, 1],
    ["null properties, undefined flags",null, undefined, 1],
    ["wrong field, flag is on", {max_instances:3}, {FLAG_2002_UNLIMITED_MAX: true}, null],
    ["maxCountInstances 3, flag is on", {maxCountInstances:3}, {FLAG_2002_UNLIMITED_MAX: true}, 3],
    ["maxCountInstances 3, flag is off", {maxCountInstances:3}, {FLAG_2002_UNLIMITED_MAX: true}, 3],
  ]).test('getMaxFirstLevel %s', (desc, properties, flags, expected) => {
    expect(Utils.getMaxVfModule(properties, flags)).toEqual(expected);
  });



});
