zk.afterLoad("zul.wgt", function () {
	zul.wgt.Label.prototype.getEncodedText = function() {
		return zUtl.encodeXML(this._value, {multiline:this._multiline,pre:this._pre, maxlength: this._maxlength}).replace(/_nl_/g, "<br />");
	}
});