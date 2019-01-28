module.exports = {
  verbose: true,
  roots: [
    "<rootDir>/src/main/webapp/app"
  ],
  modulePaths: [
    "<rootDir>/src/main/webapp/resources"
  ],
  setupTestFrameworkScriptFile: "<rootDir>/test-config.js",
  collectCoverage: true,
  collectCoverageFrom: [
    "src/**/*.js",
    "!**/node_modules/**",
    "!**/vendor/**"
  ]
};