var gulp = require('gulp');
var runSequence = require('gulp-run-sequence');
var jest = require('jest');

// -------------- Run Jest Tests --------------

gulp.task('test', function() {
  jest.runCLI({}, ".")
  .then((result) => {
    if (!result.results.success){
      console.error("Execution of js tests failed with status 1");
      process.exit(1);
    }
  });
});

// -------------- Default Task --------------

gulp.task('default', function () {
  return runSequence("test");
});