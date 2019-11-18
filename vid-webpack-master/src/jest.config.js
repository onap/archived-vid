module.exports = {
  collectCoverageFrom: ['src/**/*.{js,jsx}'],
  coveragePathIgnorePatterns: [
    '<rootDir>/node_modules/',
    '<rootDir>/src/core/server/webpack-isomorphic-tools-config.js',
    '<rootDir>/src/locale/',
  ],
  "reporters": [ "default", "jest-junit" ],
  moduleDirectories: [
    'src',
    'node_modules',
  ],
  moduleFileExtensions: [
    'js',
    'json',
    'jsx',
  ],
  moduleNameMapper: {
    // Prevent un-transpiled react-photoswipe code being required.
    '^photoswipe$': '<rootDir>/node_modules/photoswipe',
    // Use the client-side logger by default for tests.
    '^core/logger$': '<rootDir>/src/core/client/logger',
    // Alias tests for tests to be able to import helpers.
    '^tests/(.*)$': '<rootDir>/tests/$1',
    // Replaces the following formats with an empty module.
    '^.+\\.(scss|css|svg|woff|woff2|mp4|webm)$': '<rootDir>/tests/emptyModule',
  },
  setupFilesAfterEnv: ["./setupJest.ts"],
  testPathIgnorePatterns: [
    '<rootDir>/node_modules/',
    '<rootDir>/(assets|bin|config|coverage|dist|docs|flow|locale|src)/',
  ],
  testMatch: [
    '**/[Tt]est(*).js?(x)',
    '**/__tests__/**/*.js?(x)',
  ],
  transform: {
    "^.+\\.(ts|html)$": "<rootDir>/node_modules/jest-preset-angular/preprocessor.js",
    "^.+\\.js$": "babel-jest"
  },
  transformIgnorePatterns: [
    '<rootDir>/node_modules/',
  ],
  verbose: false,
};
