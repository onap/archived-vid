const cypressTypeScriptPreprocessor = require('./cy-ts-preprocessor')
module.exports = on => {
  on('file:preprocessor', cypressTypeScriptPreprocessor);
  on('fail', (err, runnable) => {return false});
};

