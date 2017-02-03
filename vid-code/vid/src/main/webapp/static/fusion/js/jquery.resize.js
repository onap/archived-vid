/*!
 * jquery.resize.js 0.0.1 - https://github.com/yckart/jquery.resize.js
 * Resize-event for DOM-Nodes
 *
 * @see http://workingdraft.de/113/
 * @see http://www.backalleycoder.com/2013/03/18/cross-browser-event-based-element-resize-detection/
 *
 * Copyright (c) 2013 Yannick Albert (http://yckart.com)
 * Licensed under the MIT license (http://www.opensource.org/licenses/mit-license.php).
 * 2013/04/01
 */

(function(factory) {
    if(typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['jquery'], factory);
    } else if(typeof exports === 'object') {
        // Node/CommonJS style for Browserify
        module.exports = factory;
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function($) {

    function addFlowListener(element, type, fn) {
        var flow = type == 'over';
        element.addEventListener('OverflowEvent' in window ? 'overflowchanged' : type + 'flow', function(e) {
            if(e.type == (type + 'flow') || ((e.orient == 0 && e.horizontalOverflow == flow) || (e.orient == 1 && e.verticalOverflow == flow) || (e.orient == 2 && e.horizontalOverflow == flow && e.verticalOverflow == flow))) {
                e.flow = type;
                return fn.call(this, e);
            }
        }, false);
    };

    function fireEvent(element, type, data, options) {
        var options = options || {},
            event = document.createEvent('Event');
        event.initEvent(type, 'bubbles' in options ? options.bubbles : true, 'cancelable' in options ? options.cancelable : true);
        for(var z in data) event[z] = data[z];
        element.dispatchEvent(event);
    };

    $.event.special.resize = {
        setup: function() {
            var element = this;
            var resize = 'onresize' in element;
            if(!resize && !element._resizeSensor) {
                var sensor = element._resizeSensor = document.createElement('div');
                sensor.className = 'resize-sensor';
                sensor.innerHTML = '<div class="resize-overflow"><div></div></div><div class="resize-underflow"><div></div></div>';

                var x = 0,
                    y = 0,
                    first = sensor.firstElementChild.firstChild,
                    last = sensor.lastElementChild.firstChild,
                    matchFlow = function(event) {
                        var change = false,
                            width = element.offsetWidth;
                        if(x != width) {
                            first.style.width = width - 1 + 'px';
                            last.style.width = width + 1 + 'px';
                            change = true;
                            x = width;
                        }
                        var height = element.offsetHeight;
                        if(y != height) {
                            first.style.height = height - 1 + 'px';
                            last.style.height = height + 1 + 'px';
                            change = true;
                            y = height;
                        }
                        if(change && event.currentTarget != element) fireEvent(element, 'resize');
                    };

                if(getComputedStyle(element).position == 'static') {
                    element.style.position = 'relative';
                    element._resizeSensor._resetPosition = true;
                }
                addFlowListener(sensor, 'over', matchFlow);
                addFlowListener(sensor, 'under', matchFlow);
                addFlowListener(sensor.firstElementChild, 'over', matchFlow);
                addFlowListener(sensor.lastElementChild, 'under', matchFlow);
                element.appendChild(sensor);
                matchFlow({});
            }
            var events = element._flowEvents || (element._flowEvents = []);
            if(events.indexOf(handler) == -1) events.push(handler);
            if(!resize) element.addEventListener('resize', handler, false);
            element.onresize = function(e) {
                events.forEach(function(fn) {
                    fn.call(element, e);
                });
            };
        },

        teardown: function() {
            var element = this;
            var index = element._flowEvents.indexOf(handler);
            if(index > -1) element._flowEvents.splice(index, 1);
            if(!element._flowEvents.length) {
                var sensor = element._resizeSensor;
                if(sensor) {
                    element.removeChild(sensor);
                    if(sensor._resetPosition) element.style.position = 'static';
                    delete element._resizeSensor;
                }
                if('onresize' in element) element.onresize = null;
                delete element._flowEvents;
            }
            element.removeEventListener('resize', handler);
        }
    };

    $.fn.extend({
        resize: function(fn) {
            return fn ? this.bind("resize", fn) : this.trigger("resize");
        },

        unresize: function(fn) {
            return this.unbind("resize", fn);
        }
    });


    function handler(event) {
        var orgEvent = event || window.event,
            args = [].slice.call(arguments, 1);

        event = $.event.fix(orgEvent);
        event.type = "resize";

        // Add event to the front of the arguments
        args.unshift(event);

        return($.event.dispatch || $.event.handle).apply(this, args);
    }

}));