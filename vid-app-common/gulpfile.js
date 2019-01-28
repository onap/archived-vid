var gulp = require('gulp');
var runSequence = require('gulp-run-sequence');
var dependencies = require('./package.json').dependencies;
var filter = require('gulp-filter');
var flatten = require('gulp-flatten');
var jest = require('gulp-jest').default;
var jestCli = require('jest-cli');

// -------------- Find and copy external dependencies --------------

const dest_path = './src/main/webapp/resources/vendor/';
const node_modules = './node_modules/';

var depList = () => {
  var deps = [];
  for (let dependenciesKey in dependencies) {
    if (dependencies.hasOwnProperty(dependenciesKey)) {
      deps.push(node_modules + dependenciesKey + "/**");
    }
  }
  return deps;
};

gulp.task('copyJs', function () {
  const jsFilter = filter('**/*.min.js', {passthrough: false});
  return gulp.src(depList())
  .pipe(jsFilter)
  .pipe(flatten())
  .pipe(gulp.dest(dest_path + '/js/'));
});

gulp.task('copyCss', function () {
  const cssFilter = filter('**/*.min.css', {passthrough: false});
  return gulp.src(depList())
  .pipe(cssFilter)
  .pipe(flatten())
  .pipe(gulp.dest(dest_path + '/css/'));
});

gulp.task('copyFonts', function () {
  const fontFilter = filter(['**/*.eot', "**/*.svg", "**/*.ttf", "**/*.woff", "**/*.woff2"], {passthrough: false});
  return gulp.src(depList())
  .pipe(fontFilter)
  .pipe(flatten())
  .pipe(gulp.dest(dest_path + '/fonts/'));
});

// -------------- Run Jest Tests --------------

gulp.task('test', function(done) {
  jestCli.runCLI({}, ".", function() {
    done();
  });
});

// -------------- Default Task --------------

gulp.task('default', function () {
  return runSequence('copyCss', "copyFonts", "copyJs", "test");
});