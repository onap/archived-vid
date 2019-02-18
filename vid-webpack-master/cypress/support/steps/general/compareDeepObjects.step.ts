const { _ } = Cypress;

declare namespace Cypress {
  interface Chainable {
    deepCompare: typeof deepCompare,
  }
}

function deepCompare(actual : any, expected : any) {
  if(actual !== null && expected !== null){
    let diff : any[] = [];
    Cypress._.mergeWith(actual, expected, function (objectValue, sourceValue, key, object, source) {
      if ( !(_.isEqual(objectValue, sourceValue)) && (Object(objectValue) !== objectValue)) {
        diff.push("key: " +key + ", expected: " + sourceValue + ", actual: " + objectValue);
      }
    });

    Cypress._.mergeWith(expected, actual, function (objectValue, sourceValue, key, object, source) {
      if ( !(_.isEqual(objectValue, sourceValue)) && (Object(objectValue) !== objectValue)) {
        diff.push("key: " +key + ", expected: " + sourceValue + ", actual: " + objectValue);
      }
    });

    if(diff.length > 0){
      console.error("diff", diff);
      cy.log("The object are not equals", diff);
      expect(actual).equals(expected);
    }
  }
}

Cypress.Commands.add('deepCompare', deepCompare);
