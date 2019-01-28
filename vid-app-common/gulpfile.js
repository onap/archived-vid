var gulp = require("gulp");
var jest = require("jest");

// -------------- Run Jest Tests --------------

gulp.task("test", function() {
  return jest.runCLI({}, ".")
  .then((result) => {
    if (!result.results.success){
      console.error("Execution of js tests failed with status 1");
      process.exit(1);
    }
  });
});

// -------------- Default Task --------------

gulp.task("default", gulp.parallel(["test"]));