const next = require('postcss-cssnext');
const modules = require('postcss-modules');
const postcss = require('postcss');

const processCss = function(file, done) {
  postcss([
    next,
    modules({
      getJSON: function(filename, json) {
        file.rename(file.path + '.json')
        done(JSON.stringify(json))
      }
    })
  ]).process(file.content, {
    from: file.path,
    to: file.path
  }).catch(function(err) {
    throw err
  })
}
