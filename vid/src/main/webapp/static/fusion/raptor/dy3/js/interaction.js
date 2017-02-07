	function moveV3(event, g, context) {
		if (context.isPanning) {
			Dygraph.movePan(event, g, context);
		} else if (context.isZooming) {
			Dygraph.moveZoom(event, g, context);
		}
	}
	
	function upV3(event, g, context) {
		if (context.isPanning) {
			Dygraph.endPan(event, g, context);
		} else if (context.isZooming) {
			Dygraph.endZoom(event, g, context);
		}
	}	

// Take the offset of a mouse event on the dygraph canvas and
// convert it to a pair of percentages from the bottom left. 
// (Not top left, bottom is where the lower value is.)
function offsetToPercentage(g, offsetX, offsetY) {
  // This is calculating the pixel offset of the leftmost date.
  var xOffset = g.toDomCoords(g.xAxisRange()[0], null)[0];
  var yar0 = g.yAxisRange(0);

  // This is calculating the pixel of the higest value. (Top pixel)
  var yOffset = g.toDomCoords(null, yar0[1])[1];

  // x y w and h are relative to the corner of the drawing area,
  // so that the upper corner of the drawing area is (0, 0).
  var x = offsetX - xOffset;
  var y = offsetY - yOffset;

  // This is computing the rightmost pixel, effectively defining the
  // width.
  var w = g.toDomCoords(g.xAxisRange()[1], null)[0] - xOffset;

  // This is computing the lowest pixel, effectively defining the height.
  var h = g.toDomCoords(null, yar0[0])[1] - yOffset;

  // Percentage from the left.
  var xPct = w == 0 ? 0 : (x / w);
  // Percentage from the top.
  var yPct = h == 0 ? 0 : (y / h);

  // The (1-) part below changes it from "% distance down from the top"
  // to "% distance up from the bottom".
  return [xPct, (1-yPct)];
}

function dblClickV3(event, g, context) {
  // Reducing by 20% makes it 80% the original size, which means
  // to restore to original size it must grow by 25%
  /*
  if (!(event.offsetX && event.offsetY)){
    event.offsetX = event.layerX - event.target.offsetLeft;
    event.offsetY = event.layerY - event.target.offsetTop;
  }

  var percentages = offsetToPercentage(g, event.offsetX, event.offsetY);
  var xPct = percentages[0];
  var yPct = percentages[1];

  if (event.ctrlKey) {
    zoom(g, -.25, xPct, yPct);
  } else {
    zoom(g, +.2, xPct, yPct);
  }
  */
  restorePositioning(g);
}

var lastClickedGraph = null;

function clickV3(event, g, context) {
  lastClickedGraph = g;
  Dygraph.cancelEvent(event);
}

function scrollV3(event, g, context) {
  if (lastClickedGraph != g) {
    return;
  }
  var normal = event.detail ? event.detail * -1 : event.wheelDelta / 40;
  // For me the normalized value shows 0.075 for one click. If I took
  // that verbatim, it would be a 7.5%.
  var percentage = normal / 50;

  if (!(event.offsetX && event.offsetY)){
    event.offsetX = event.layerX - event.target.offsetLeft;
    event.offsetY = event.layerY - event.target.offsetTop;
  }

  var percentages = offsetToPercentage(g, event.offsetX, event.offsetY);
  var xPct = percentages[0];
  var yPct = percentages[1];

  zoom(g, percentage, xPct, yPct);
  Dygraph.cancelEvent(event);
}

// Adjusts [x, y] toward each other by zoomInPercentage%
// Split it so the left/bottom axis gets xBias/yBias of that change and
// tight/top gets (1-xBias)/(1-yBias) of that change.
//
// If a bias is missing it splits it down the middle.
function zoom(g, zoomInPercentage, xBias, yBias) {
  xBias = xBias || 0.5;
  yBias = yBias || 0.5;
  function adjustAxis(axis, zoomInPercentage, bias) {
    var delta = axis[1] - axis[0];
    var increment = delta * zoomInPercentage;
    var foo = [increment * bias, increment * (1-bias)];
    return [ axis[0] + foo[0], axis[1] - foo[1] ];
  }
  var yAxes = g.yAxisRanges();
  var newYAxes = [];
  for (var i = 0; i < yAxes.length; i++) {
    newYAxes[i] = adjustAxis(yAxes[i], zoomInPercentage, yBias);
  }

  g.updateOptions({
    dateWindow: adjustAxis(g.xAxisRange(), zoomInPercentage, xBias),
    valueRange: newYAxes[0]
    });
}

function restorePositioning(g) {
  g.updateOptions({
    dateWindow: null,
    valueRange: null
  });
}	

function zoom_custom(res) {
  var w = g.xAxisRange();
  desired_range = [w[0] - res * 1000, w[0] ];
  animate();
}

function reset() {
 desired_range = orig_range;
 animate();
}

var click = 0;
var desired_range = null;
function approach_range() {
	if (!desired_range) return;
	// go halfway there
	var range = g.xAxisRange();
	if (Math.abs(desired_range[0] - range[0]) < 60 &&
		Math.abs(desired_range[1] - range[1]) < 60) {
		if(desired_range[0]>=orig_range[0])
			g.updateOptions({dateWindow: desired_range});
		else {
			g.updateOptions({dateWindow: orig_range});
			click = 8;
		}
		// (do not set another timeout.)
	} else {
		var new_range;
        new_range = [0.5 * (desired_range[0] + range[0]),
                       0.5 * (desired_range[1] + range[1])];
        g.updateOptions({dateWindow: new_range});
        animate();
    }
}	  

function animate() {
  setTimeout(approach_range, 50);
}


var v4Active = false;
var v4Canvas = null;

function downV4(event, g, context) {
  context.initializeMouseDown(event, g, context);
    
    Dygraph.Interaction.startTouch(event, g, context);
  
    Dygraph.Interaction.moveTouch(event, g, context);
  
    Dygraph.Interaction.endTouch(event, g, context);
  
  v4Active = true;
  moveV4(event, g, context); // in case the mouse went down on a data point.
}

var processed = [];

function moveV4(event, g, context) {
  var RANGE = 7;

  if (v4Active) {
    var graphPos = Dygraph.findPos(g.graphDiv);
    var canvasx = Dygraph.pageX(event) - graphPos.x;
    var canvasy = Dygraph.pageY(event) - graphPos.y;

    var rows = g.numRows();
    // Row layout:
    // [date, [val1, stdev1], [val2, stdev2]]
    for (var row = 0; row < rows; row++) {
      var date = g.getValue(row, 0);
      var x = g.toDomCoords(date, null)[0];
      var diff = Math.abs(canvasx - x);
      if (diff < RANGE) {
        for (var col = 1; col < 3; col++) {
          // TODO(konigsberg): these will throw exceptions as data is removed.
          var vals =  g.getValue(row, col);
          if (vals == null) { continue; }
          var val = vals[0];
          var y = g.toDomCoords(null, val)[1];
          var diff2 = Math.abs(canvasy - y);
          if (diff2 < RANGE) {
            var found = false;
            for (var i in processed) {
              var stored = processed[i];
              if(stored[0] == row && stored[1] == col) {
                found = true;
                break;
              }
            }
            if (!found) {
              //processed.push([row, col]);
              //drawV4(x, y);
            }
            return;
          }
        }
      }
    }
  }
}

function upV4(event, g, context) {
  if (v4Active) {
    v4Active = false;
  }
}

function dblClickV4(event, g, context) {
  restorePositioning(g);
}

function drawV4(x, y) {
  var ctx = v4Canvas;

  ctx.strokeStyle = "#000000";
  //ctx.fillStyle = "#FFFF00";
  ctx.fillStyle = "#FF0000";
  ctx.beginPath();
  ctx.arc(x,y,5,0,Math.PI*2,true);
  ctx.closePath();
  ctx.stroke();
  ctx.fill();
}

function captureCanvas(canvas, area, g) {
  v4Canvas = canvas;
}

function newDygraphTouchstart(event, g, context) { 
// This right here is what prevents IOS from doing its own zoom/touch behavior 
// It stops the node from being selected too 
event.preventDefault(); // touch browsers are all nice. 

if (event.touches.length > 1) { 
// If the user ever puts two fingers down, it's not a double tap. 
context.startTimeForDoubleTapMs = null; 
} 

var touches = []; 
for (var i = 0; i < event.touches.length; i++) { 
var t = event.touches[i]; 
// we dispense with 'dragGetX_' because all touchBrowsers support pageX 
touches.push({ 
pageX: t.pageX, 
pageY: t.pageY, 
dataX: g.toDataXCoord(t.pageX), 
dataY: g.toDataYCoord(t.pageY) 
// identifier: t.identifier 
}); 
} 
context.initialTouches = touches; 

if (touches.length == 1) { 
// This is just a swipe. 
context.initialPinchCenter = touches[0]; 
context.touchDirections = { x: true, y: true }; 

// ADDITION - this needs to select the points 
//var closestTouchP = g.findClosestPoint(touches[0].pageX,touches[0].pageY); 
//if(closestTouchP) { 
//var selectionChanged = g.setSelection(closestTouchP.row, closestTouchP.seriesName); 
//} 
g.mouseMove_(event); 

} else if (touches.length >= 2) { 
// It's become a pinch! 
// In case there are 3+ touches, we ignore all but the "first" two. 

// only screen coordinates can be averaged (data coords could be log scale). 
context.initialPinchCenter = { 
pageX: 0.5 * (touches[0].pageX + touches[1].pageX), 
pageY: 0.5 * (touches[0].pageY + touches[1].pageY), 

// TODO(danvk): remove 
dataX: 0.5 * (touches[0].dataX + touches[1].dataX), 
dataY: 0.5 * (touches[0].dataY + touches[1].dataY) 
}; 

// Make pinches in a 45-degree swath around either axis 1-dimensional zooms. 
var initialAngle = 180 / Math.PI * Math.atan2( 
context.initialPinchCenter.pageY - touches[0].pageY, 
touches[0].pageX - context.initialPinchCenter.pageX); 

// use symmetry to get it into the first quadrant. 
initialAngle = Math.abs(initialAngle); 
if (initialAngle > 90) initialAngle = 90 - initialAngle; 

context.touchDirections = { 
x: (initialAngle < (90 - 45/2)), 
y: (initialAngle > 45/2) 
}; 
} 

// save the full x & y ranges. 
context.initialRange = { 
x: g.xAxisRange(), 
y: g.yAxisRange() 
}; 
};
