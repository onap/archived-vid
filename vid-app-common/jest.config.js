module.exports = {
  verbose: true,
  roots: [
    "<rootDir>/src/main/webapp/app"
  ],
  modulePaths: [
    "<rootDir>/src/main/webapp/app/vid/external"
  ],
  setupFilesAfterEnv: ["./test-config.js"],
  collectCoverage: false,
  collectCoverageFrom: [
    "src/**/*.js",
    "!**/node_modules/**",
    "!**/vendor/**"
  ]
};
