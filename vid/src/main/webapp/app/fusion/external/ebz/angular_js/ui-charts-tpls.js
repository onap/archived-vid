/*Sandbox version 0.0.41a*/
angular.module("att.charts", ["att.charts.tpls", "att.charts.utilities","att.charts.areachartD3","att.charts.barchartD3","att.charts.coschartD3","att.charts.coschartwithbarD3","att.charts.cosdeletionD3","att.charts.cosmultichartD3","att.charts.donutD3","att.charts.donutFusion","att.charts.horseshoeD3","att.charts.radialguageD3","att.charts.stackedBarchart","att.charts.stackedareachart"]);
angular.module("att.charts.tpls", ["template/areachartD3/attAreaChartD3.html","template/barchartD3/attBarChartD3.html","template/coschartD3/attCosd3Chart.html","template/coschartwithbarD3/attCosBarD3Chart.html","template/cosmultichartD3/attCosmultid3Chart.html","template/donutD3/attDonutd3Chart.html","template/donutFusion/attDonutfusionChart.html","template/horseshoeD3/attHorseshoeD3Chart.html","template/stackedBarchart/stackedBarchart.html","template/stackedareachart/stackedAreaChart.html"]);
angular.module('att.charts.utilities', [])
        .factory('$extendObj', [function () {
                var _extendDeep = function (dst) {
                    angular.forEach(arguments, function (obj) {
                        if (obj !== dst) {
                            angular.forEach(obj, function (value, key) {
                                if (dst[key] && dst[key].constructor && dst[key].constructor === Object) {
                                    _extendDeep(dst[key], value);
                                } else {
                                    dst[key] = value;
                                }
                            });
                        }
                    });
                    return dst;
                };
                return {
                    extendDeep: _extendDeep
                    
                };
            }]);

angular.module('att.charts.areachartD3', ['att.charts.utilities'])
        .constant("areaChartConfig",
                {
                    "chartcolor": {
                        "areaChartColor": ["#bff1ec", "#bbf0eb", "#00c7b2"],
                        "lineChartColor": ["#444444", "#444444"],
                        "overageColor": ["#b8509e"]
                    },
                    "gridLineColor": "#808080",
                    "lineCurveType": 'cardinal',
                    "yAxisMaxTicks": 4,
                    "chartHeight": "200",
                    "dataType": "GB",
                    "lineStroke": 3,
                    "opacityOnHover": "0.5",
                    "circleRadius": 3.5,
                    "yearLabelPos": [5, -5],
                    "tooltipTopMargin": 105,
                    "tooltipLeftMargin": 28,
                    "amountKDivision": 1000,
                    "amountKText": "K",
                    "amountMDivision": 1000000,
                    "amountMText": "M"
                })
        .directive('attAreaChart', ['areaChartConfig', '$extendObj', '$timeout', function (areaChartConfig, $extendObj, $timeout) {
                return {
                    restrict: 'A',
                    scope: {
                        chartData: '=',
                        chartColor: "=",
                        legendLabel: "=",
                        refreshChart: "=",
                        chartConfig: "="
                    },
                    templateUrl: "template/areachartD3/attAreaChartD3.html",
                    replace: true,
                    controller: ['$scope', '$attrs', function ($scope, $attrs) {
                            if (angular.isDefined($scope.chartConfig)) {
                                areaChartConfig = $extendObj.extendDeep(areaChartConfig, $scope.chartConfig);
                            }
                            $scope.dataType = areaChartConfig.dataType;
                            $scope.chartID = $attrs.id;
                        }],
                    link: function (scope, element, attrs, ctrl) {
                        scope.tooltipFlag = false;
                        scope.overageFlag = false;
                        scope.underageFlag = false;
                        var startColor, stopColor, lineColor, xAxisticks, dataObj, margin = {top: 30, right: 15, bottom: 30, left: 40},
                        width = parseInt(attrs.chartWidth, 10),
                                height = areaChartConfig.chartHeight,
                                parseDate = d3.time.format("%d-%b-%Y").parse,
                                tooltipFormat = d3.time.format("%B-%Y"),
                                labelFormat = d3.time.format("%Y");
                        var legendColors = [];
                        legendColors = (scope.chartColor) ? scope.chartColor : areaChartConfig.chartcolor;
                        startColor = legendColors.areaChartColor[0];
                        stopColor = legendColors.areaChartColor[1];
                        lineColor = legendColors.lineChartColor[0];
                        scope.areaLegendColor = legendColors.areaChartColor[2];
                        scope.lineLegendColor = legendColors.lineChartColor[1];
                        scope.overageLegend = legendColors.overageColor[0];
                        var oldIE = navigator.userAgent.toLowerCase().indexOf('msie 8.0') !== -1;
                        scope.addLegendColor = function (id) {
                            var bgColor = null;
                            switch (id) {
                                case 0:
                                    bgColor = {"background-color": scope.areaLegendColor, "border-radius": "100%"};
                                    break;
                                case 1:
                                    bgColor = {"background-color": scope.lineLegendColor};
                                    break;
                                case 2:
                                    bgColor = {"background-color": scope.overageLegend, "border-radius": "100%"};
                                    break;
                            }
                            return bgColor;
                        };

                        attrs.$observe('legendRequired', function (val) {
                            if (val === 'true') {
                                scope.showLegend = true;
                            }
                            else {
                                scope.showLegend = false;
                            }
                        });

                        scope.$watch('refreshChart', function (value) {
                            if (value === false) {
                                return;
                            }
                            if (angular.isDefined(scope.chartConfig)) {
                                areaChartConfig = $extendObj.extendDeep(areaChartConfig, scope.chartConfig);
                            }
                            d3.select("svg#" + attrs.id).remove();
                            d3.selectAll("svg#" + attrs.id + " > *").remove();
                            d3.selectAll("div#areaChartContainer" + " > div").remove();
                            xAxisticks = scope.chartData.length;
                            dataObj = scope.chartData;
                            var yearLabel = '', monthArr = {}, isSingleMonth, singleMonthName;

                            //  On selecting same month in From and To dropdowns, User should be getting graph                            
                            if (dataObj.length === 1) {
                                isSingleMonth = true;
                                var tmp1 = "01-" + dataObj[0].month;
                                var tmp = parseDate(tmp1);
                                singleMonthName = tmp.toString().split(" ")[1];
                                tmp.setMonth(tmp.getMonth() + 1);
                                var nextMonth = tmp.toString().split(" ")[1] + "-" + tmp.getFullYear();
                                dataObj.push({month: nextMonth, usage: dataObj[0].usage, available: dataObj[0].available, usageDataType: dataObj[0].usageDataType, availableDataType: dataObj[0].availableDataType});
                            }

                            dataObj.forEach(function (d) {
                                var tmp = "01-" + d.month;
                                d.numericMonth = parseDate(tmp);
                                d.usage = +d.usage;
                                d.available = +d.available;
                                monthArr[labelFormat(d.numericMonth)] = labelFormat(d.numericMonth);
                            });

                            for (var key in monthArr) {
                                var label = monthArr[key];
                                if (!yearLabel) {
                                    yearLabel = label;
                                }
                                else {
                                    if (!isSingleMonth) {
                                        yearLabel = yearLabel + "-" + label;
                                    }
                                }
                            }

                            //X-Axis Range        
                            var xRange = d3.time.scale()
                                    .range([0, width]);

                            //Y-Axis Range 
                            var yRange = d3.scale.linear()
                                    .range([height, 0]);

                            //X-Axis Domain  
                            xRange.domain(d3.extent(dataObj, function (d) {
                                return d.numericMonth;
                            }));

                            //Y-Axis Domain
                            var yDomainData = [], yAxisData = [];
                            for (var b = 0; b < dataObj.length; b++) {
                                var usageVal = Math.round(parseInt(dataObj[b].usage, 10) / areaChartConfig.yAxisMaxTicks);
                                var availableVal = Math.round(parseInt(dataObj[b].available, 10) / areaChartConfig.yAxisMaxTicks);
                                var val;
                                if (usageVal > availableVal) {
                                    val = usageVal;
                                }
                                else {
                                    val = availableVal;
                                }
                                var Calc = Math.ceil((val / Math.pow(10, ("" + val).length - 1))) * (areaChartConfig.yAxisMaxTicks) * Math.pow(10, ("" + val).length - 1);
                                yDomainData.push({'yAxisVal': Calc});
                            }

                            //Y-Axis Domain
                            yRange.domain([0, d3.max(yDomainData, function (d) {
                                    return (d.yAxisVal);
                                })]);

                            var yTick = d3.max(yDomainData, function (d) {
                                return d.yAxisVal;
                            });

                            yTick = yTick / areaChartConfig.yAxisMaxTicks;
                            for (var x = 0; x <= areaChartConfig.yAxisMaxTicks; x++) {
                                yAxisData.push(yTick * x);
                            }
                            var formatData = function (d) {
                                if (d >= areaChartConfig.amountMDivision) {
                                    return d / areaChartConfig.amountMDivision + areaChartConfig.amountMText;
                                } else if (d >= areaChartConfig.amountKDivision) {
                                    return d / areaChartConfig.amountKDivision + areaChartConfig.amountKText;
                                } else {
                                    return d;
                                }
                            };
                            var xAxis = d3.svg.axis().scale(xRange).orient("bottom").tickFormat(d3.time.format('%b')).ticks(d3.time.months);
                            var yAxis = d3.svg.axis().scale(yRange).orient("left").tickValues(yAxisData).ticks(areaChartConfig.yAxisMaxTicks).tickFormat(formatData);

                            //Draw Line
                            var line = d3.svg.line()
                                    .x(function (d) {
                                        return xRange(d.numericMonth);
                                    })
                                    .y(function (d) {
                                        return yRange(d.available);
                                    }).interpolate(areaChartConfig.lineCurveType);

                            //Draw Area        
                            var area = d3.svg.area()
                                    .x(function (d) {
                                        return xRange(d.numericMonth);
                                    })
                                    .y0(height)
                                    .y1(function (d) {
                                        return yRange(d.usage);
                                    }).interpolate(areaChartConfig.lineCurveType);

                            //Plot Chart
                            var drawChart = d3.select("#areaChartContainer")
                                    .append("svg")
                                    .attr("id", attrs.id)
                                    .data(dataObj)
                                    .attr("width", width + margin.left + margin.right)
                                    .attr("height", parseInt(height, 10) + parseInt(margin.top, 10) + parseInt(margin.bottom, 10))
                                    .append("g")
                                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

                            //Apply gradients 
                            if (!oldIE) {
                                var svgDefs = drawChart.append("defs")
                                        .append("linearGradient")
                                        .attr("id", "areaGradient")
                                        .attr('x1', '0%').attr('y1', '0%')
                                        .attr('x2', '0%').attr('y2', '100%');

                                //Fill gradient      
                                svgDefs.append('stop')
                                        .attr('stop-color', startColor)
                                        .attr("offset", "0%")
                                        .attr("stop-opacity", "1");
                                svgDefs.append('stop')
                                        .attr('stop-color', stopColor)
                                        .attr("offset", "100%")
                                        .attr("stop-opacity", "1");
                            }

                            //Draw X Axis        
                            drawChart.append("g")
                                    .attr("class", "x axis")
                                    .attr("transform", "translate(0," + height + ")")
                                    .call(xAxis).selectAll("text")
                                    .attr("dy", "1.2em");


                            if (isSingleMonth) {
                                drawChart.selectAll('.x.axis').selectAll('.tick').selectAll('text')[1][0].textContent = singleMonthName;
                            }

                            //Plot Area
                            var areaPath = drawChart.append("path")
                                    .datum(dataObj)
                                    .attr("class", "area")
                                    .attr("d", area).style("fill", "url(#areaGradient)").append("title").text(function () {
                                return scope.legendLabel[0].series;
                            });

                            var newDataObj = dataObj;
                            if (isSingleMonth) {
                                newDataObj = dataObj.slice(0, 1);
                            }

                            //Draw Data Points for Area Chart
                            var circle = drawChart.selectAll(".dot")
                                    .data(newDataObj)
                                    .enter().append("circle")
                                    .attr("class", "dot")
                                    .attr("transform", "translate(" + (isSingleMonth === true) * width / 2 + ",0)")
                                    .style("fill", function (d) {
                                        if (d.usage > d.available) {
                                            return scope.overageLegend;
                                        }
                                        else {
                                            return scope.areaLegendColor;
                                        }
                                    })
                                    .attr("r", areaChartConfig.circleRadius)
                                    .attr("cx", function (d) {
                                        return xRange(d.numericMonth);
                                    })
                                    .attr("cy", function (d) {
                                        return yRange(d.usage);
                                    }).on("mouseover", function (d) {
                                var offsetX = Math.ceil(d3.select(this).attr("cx"));
                                show_tooltip_grid_line(d3.select(this).attr("cx"), "circle", "cx");
                                var offsetY = Math.round(d3.select(this).attr("cy"));
                                d3.select('svg#' + attrs.id).style("fill-opacity", areaChartConfig.opacityOnHover);
                                mouseOver(d, offsetX - 3, offsetY - 3);
                            }).on("mouseout", function () {
                                hide_tooltip_grid_line(d3.select(this).attr("cx"), "circle", "cx");
                                d3.select('svg#' + attrs.id).style("fill-opacity", "1");
                                scope.tooltipFlag = false;
                                scope.$apply();
                            });
                            circle.append('desc').append('title').text(function (d) {
                                return(addTitle(d));
                            });

                            //Draw Line Chart
                            drawChart.append("path")
                                    .attr("class", "line")
                                    .style("stroke", lineColor)
                                    //.style("stroke-dasharray", (areaChartConfig.lineStroke, areaChartConfig.lineStroke))
                                    .attr("d", line(dataObj)).append("title").text(function () {
                                return scope.legendLabel[1].series;
                            });

                            //Draw data points for Line Chart
                            if (!oldIE) {
                                var squaredot = drawChart.selectAll(".square-dot")
                                        .data(newDataObj).enter().append("rect")
                                        .attr("transform", "translate(" + (isSingleMonth === true) * width / 2 + ",0)")
                                        .style("fill", scope.lineLegendColor)
                                        .attr("class", "square-dot")
                                        .attr("x", function (d) {
                                            var tmp = xRange(d.numericMonth);
                                            tmp = tmp - 2.5;
                                            return tmp;
                                        })
                                        .attr("y", function (d) {
                                            var tmp = yRange(d.available);
                                            tmp = tmp - 2.5;
                                            return tmp;
                                        })
                                        .attr("width", "5px")
                                        .attr("height", "5px")
                                        .on("mouseover", function (d) {
                                            var offsetX = Math.ceil(d3.select(this).attr("x"));
                                            show_tooltip_grid_line(d3.select(this).attr("x"), "rect", "x");
                                            var offsetY = Math.round(d3.select(this).attr("y"));
                                            d3.select('svg#' + attrs.id).style("fill-opacity", areaChartConfig.opacityOnHover);
                                            mouseOver(d, offsetX, offsetY);
                                        }).on("mouseout", function () {
                                    hide_tooltip_grid_line(d3.select(this).attr("x"), "rect", "x");
                                    d3.select('svg#' + attrs.id).style("fill-opacity", "1");
                                    scope.tooltipFlag = false;
                                    scope.$apply();
                                });
                                squaredot.append('desc').append('title').text(function (d) {
                                    return(addTitle(d));
                                });
                            }
                            else {
                                areaPath.style("fill", startColor);
                                var squareDot = drawChart.selectAll(".square-dot")
                                        .data(newDataObj).enter().append("circle")
                                        .attr("class", "square-dot")
                                        .attr("transform", "translate(" + (isSingleMonth === true) * width / 2 + ",0)")
                                        .style("fill", scope.areaLegendColor)
                                        .attr("r", areaChartConfig.circleRadius)
                                        .attr("cx", function (d) {
                                            return xRange(d.numericMonth);
                                        })
                                        .attr("cy", function (d) {
                                            return yRange(d.available);
                                        }).on("mouseover", function (d) {
                                    var offsetX = Math.ceil(d3.select(this).attr("cx"));
                                    var offsetY = Math.round(d3.select(this).attr("cy"));
                                    show_tooltip_grid_line(d3.select(this).attr("cx"), "circle", "cx");
                                    d3.select('svg#' + attrs.id).style("fill-opacity", areaChartConfig.opacityOnHover);
                                    mouseOver(d, offsetX, offsetY);
                                }).on("mouseout", function () {
                                    hide_tooltip_grid_line(d3.select(this).attr("cx"), "circle", "cx");
                                    d3.select('svg#' + attrs.id).style("fill-opacity", "1");
                                    scope.tooltipFlag = false;
                                    scope.$apply();
                                });
                                squareDot.append('desc').append('title').text(function (d) {
                                    return(addTitle(d));
                                });
                            }

                            //Draw Y Axis
                            var yAxisNodes = drawChart.append("g")
                                    .attr("class", "y axis").attr("id", "yAxisId")
                                    .call(yAxis);

                            yAxisNodes.selectAll("text")
                                    .attr("id", function (d, i) {
                                        return ("yAxisText" + i);
                                    });

                            //Append Year Label 
                            yAxisNodes.append("text")
                                    .attr("transform", "rotate(0)")
                                    .attr("y", areaChartConfig.yearLabelPos[1])
                                    .attr("x", areaChartConfig.yearLabelPos[0]).attr("class", "yearLabel")
                                    .text(yearLabel);
                            //Remove minimum value label form Y Axis
                            drawChart.select("#yAxisText0").remove();

                            // Draw the x Grid lines
                            drawChart.append("g")
                                    .attr("class", "grid")
                                    .attr("id", "xGrid")
                                    .attr("transform", "translate(" + (isSingleMonth === true) * width / 2 + ",0)")
                                    .call(make_x_axis()
                                            .tickSize(-height, 0)
                                            .tickFormat(""));

                            // function for the x grid lines
                            function make_x_axis() {
                                return d3.svg.axis()
                                        .scale(xRange)
                                        .orient("top")
                                        .tickFormat(d3.time.format('%b')).ticks(d3.time.months);
                            }

                            //drawChart.select("#xGrid").selectAll("line").style("stroke", areaChartConfig.gridLineColor);
                            drawChart.select("#xGrid").selectAll("line").style("stroke", "none");
                            drawChart.select("#xGrid").selectAll("line")
                                    .attr("id", function (d, i) {
                                        return ("xAxisLine" + i);
                                    });

                            //Add title for NVDA screen reader
                            function addTitle(d) {
                                return (tooltipFormat(d.numericMonth) + "Used" + d.usage + d.usageDataType + "Available" + d.available + d.availableDataType);
                            }

                            //Show Gridline on Mouse Hover        
                            function show_tooltip_grid_line(offsetX, shape, attr) {
                                try {
                                    for (var i = 0; i < xAxisticks; i++) {
                                        var circle = drawChart.selectAll(shape);
                                        if (circle[0][i].getAttribute(attr) === offsetX) {
                                            drawChart.select("#xAxisLine" + i).style("stroke", areaChartConfig.gridLineColor);
                                        }
                                    }
                                } catch (e) {
                                }
                            }

                            //Hide Gridline on Mouse Hover  
                            function hide_tooltip_grid_line(offsetX, shape, attr) {
                                try {
                                    for (var i = 0; i < xAxisticks; i++) {
                                        var circle = drawChart.selectAll(shape);
                                        if (circle[0][i].getAttribute(attr) === offsetX) {
                                            drawChart.select("#xAxisLine" + i).style("stroke", "transparent");
                                        }
                                    }
                                } catch (e) {
                                }
                            }

                            //Mouse Hover
                            function mouseOver(d, offsetX, offsetY) {
                                scope.underageFlag = false;
                                scope.overageFlag = false;
                                if (d.usage > d.available) {
                                    scope.overageFlag = true;
                                }
                                if (d.usage < d.available) {
                                    scope.underageFlag = true;
                                }
                                scope.dataPoint = {"xData": tooltipFormat(d.numericMonth).replace('-', ' '), "dataUsed": d.usage, "dataAvailable": d.available, "overage": d.overage, "usageDataType": d.usageDataType, "availableDataType": d.availableDataType, "underage": d.underage};
                                scope.$apply();
                                $timeout(function () {
                                    offsetY = offsetY - areaChartConfig.tooltipTopMargin;
                                    var tooltipEl = element.children().eq(2);
                                    var tooltipWidth = tooltipEl[0].offsetWidth;
                                    if (isSingleMonth) {
                                        offsetX = offsetX - (tooltipWidth / 2) + areaChartConfig.tooltipLeftMargin + (width / 2);
                                    } else {
                                        offsetX = offsetX - (tooltipWidth / 2) + areaChartConfig.tooltipLeftMargin;
                                    }
                                    scope.tooltipStyle = {"left": offsetX + "px", "top": offsetY + "px"};
                                    scope.tooltipFlag = true;
                                },0);
                            }
                            scope.refreshChart = false;
                        });
                    }
                };
            }])
        .filter('filterInput', function () {
            return function (input) {
                return input.replace(/ +/g, "").toLowerCase();
            };
        });
angular.module('att.charts.barchartD3', ['att.charts.utilities'])
        
        .constant("BarChartD3Config",
                {
                    "barChartColor": "#0081C2",
                    "gridLineColor": "#808080",
					"yaxisGridLineColor": "#000000",
                    "margin": {
                            top  : 40, 
                            right: 20,
                            bottom: 30,
                            left: 40
                    },
                    "attributes": {
                        "userName": "name",
                        "phoneNumber": "phoneNumber",
                        "charges": "charges"
                    },
                    "yAxisMaxTicks": 4,
                    "chartHeight": 160,
                    "amountFormat" : "$0,000",
                    "amountKDivision" : 1000,
                    "amountKText" : "K",
                    "amountMDivision" : 1000000,    
                    "amountMText" : "M",
                    "xAxistickSize":0,
                    "tooltipTopMargin":69,
                    "tooltipLeftMargin":59,
                    "xAxisTextTopMargin":15,
                    "xAxisTextLeftMargin":7,
                    "barChartSize":25,
                    "barHoverOpacity":0.2
                }) 
                
        .directive('attBarChart', ['BarChartD3Config','$extendObj', function(BarChartD3Config,$extendObj) {
                return {
                    restrict: 'A',
                    scope: {
                        chartData: '=',
                        chartConfig: '=',
                        refreshChart: "="
                    },                   
                    templateUrl: "template/barchartD3/attBarChartD3.html",
                    replace: true,
                    link: function(scope, element, attrs, ctrl) {
                                             
                        scope.$watch('refreshChart', function() {
                        if (angular.isDefined(scope.chartConfig)) {
                                BarChartD3Config = $extendObj.extendDeep(BarChartD3Config, scope.chartConfig);
                        }

                        var width = parseInt(attrs.chartWidth, 10),
                            chartColor = BarChartD3Config.barChartColor,
                            height = BarChartD3Config.chartHeight,
                            userName = BarChartD3Config.attributes.userName,
                            charges = BarChartD3Config.attributes.charges,
                            phoneNumber = BarChartD3Config.attributes.phoneNumber,
                            formatCharges = d3.format(BarChartD3Config.amountFormat),
                            isSingleDataObj = false, padding = 20;
                        var barChartData, yDomainData = [],yAxisData = [];
                        scope.tooltipFlag = false;
                        var compare = function compare(a, b) {
                            if (parseInt(a[charges], 10) > parseInt(b[charges], 10)) {
                                return -1;
                            }
                            if (parseInt(a[charges], 10) < parseInt(b[charges], 10)) {
                                return 1;
                            }
                            return 0;
                        };


                            d3.select("svg#" + attrs.id).remove();
                            d3.selectAll("svg#" + attrs.id + " > *").remove();
                            d3.selectAll("div#barChartContainer" + " > div").remove();

                            barChartData = scope.chartData.sort(compare);
                            barChartData = barChartData.slice(0,BarChartD3Config.barChartSize);
                            if (barChartData.length === 1) {
                                isSingleDataObj = true;
                            }
                            var x = d3.scale.ordinal().rangeBands([0, width]);

                            var y = d3.scale.linear()
                                    .range([height, 0]);

                            x.domain(barChartData.map(function(d, i) {
                                return i + 1;
                            }));
                           
                            for(var b=0; b < barChartData.length; b++){
                                var val = Math.round(parseInt(barChartData[b].charges, 10)/BarChartD3Config.yAxisMaxTicks); 
                                var Calc =  Math.ceil((val / Math.pow(10, ("" + val).length - 1))) * (BarChartD3Config.yAxisMaxTicks) * Math.pow(10, ("" + val).length - 1);
                                yDomainData.push({'yAxisVal':Calc});
                            }
                            
                            //Y-Axis Domain
                            y.domain([0, d3.max(yDomainData, function(d) {
                                return (d.yAxisVal);
                            })]);
                            
                            var yTick = d3.max(yDomainData, function(d) {return d.yAxisVal;});
                            
                            yTick = yTick/BarChartD3Config.yAxisMaxTicks;
                            for(var z = 0; z <= BarChartD3Config.yAxisMaxTicks; z++){
                                yAxisData.push(yTick * z);
                            }
                            var formatMoney = function(d) {
                                if (d >= BarChartD3Config.amountMDivision) {
                                    return "$" + d / BarChartD3Config.amountMDivision + BarChartD3Config.amountMText;
                                }  else if (d >= BarChartD3Config.amountKDivision) {
                                    return "$" + d / BarChartD3Config.amountKDivision + BarChartD3Config.amountKText;
                                } else {
                                    return "$" + d;
                                }
                            };

                            var xAxis = d3.svg.axis()
                                    .scale(x)
                                    .orient("bottom")
                                    .tickSize(BarChartD3Config.xAxistickSize);

                            var yAxis = d3.svg.axis().scale(y).orient("left").tickValues(yAxisData).ticks(BarChartD3Config.yAxisMaxTicks).tickFormat(formatMoney);

                            var svg = d3.select("#barChartContainer")
                                    .append("svg")
                                    .attr("id", attrs.id)
                                    .attr("width", width + BarChartD3Config.margin.left + BarChartD3Config.margin.right)
                                    .attr("height", height + BarChartD3Config.margin.top + BarChartD3Config.margin.bottom)
                                    .append("g")
                                    .attr("transform", "translate(" + BarChartD3Config.margin.left + "," + BarChartD3Config.margin.top + ")");
                            
                            svg.append("g")
                                    .attr("class", "x axis")
                                    .attr("transform", "translate(" + -Math.ceil((x(2) - x(1)) / 2 - BarChartD3Config.xAxisTextTopMargin) + "," + (height + BarChartD3Config.xAxisTextLeftMargin) + ")")
                                    .call(xAxis);
                            
                            var formatPhNumber = function(phoneNum) {
                                if (phoneNum.indexOf("-") === -1) {
                                    return phoneNum.substr(0, 3) + '-' + phoneNum.substr(3, 3) + '-' + phoneNum.substr(6);
                                } else {
                                    return phoneNum;
                                }
                            };
                            
                            var rectBarGroup = svg.append("g")
                                    .attr("class", "group").attr("id", function (d, i) {
                                return ("barGroup" + i);
                            }).attr("transform", "translate(" + (isSingleDataObj === true? ((width / 2) + 11):10) + ",0)");
                            
                            var rectBars = rectBarGroup.selectAll(".bar")
                                    .data(barChartData)
                                    .enter().append("rect")
                                    .attr("style", "fill:" + chartColor)
                                    .attr("class", "bar")
                                    .attr("x", function(d, i) {
                                        return Math.round(x(i + 1));
                                    })
                                    .attr("width", 10)
                                    .attr("y", function(d) {
                                        return y(d[charges]);
                                    })
                                    .attr("height", function(d) {
                                         return (height - y(d[charges]));
                                    })
                                    .on("mouseover", function(d) {
                                        var offsetX = Math.round(d3.select(this).attr("x"));
                                        show_tooltip_grid_line(offsetX);
                                        var offsetY = Math.round(d3.select(this).attr("y"));
                                        svg.select(".grid").selectAll("line").style("stroke", "transparent");
                                        svg.selectAll(".bar").style("fill-opacity",BarChartD3Config.barHoverOpacity);
                                        d3.select(this).style("fill-opacity","1");
                                        mouseOver(d, offsetX, offsetY);
                                    })
                                    .on("mouseout", function() {
                                        var offsetX = Math.round(d3.select(this).attr("x"));
                                        hide_tooltip_grid_line(offsetX);
                                        scope.tooltipFlag = false;
                                        svg.select(".grid").selectAll("line").style("stroke", BarChartD3Config.gridLineColor);
                                        svg.selectAll(".bar").style("fill-opacity","1");
                                        scope.$apply();
                                    });
                            rectBars.append('desc').append('title').text(function(d){ return (d[userName] + "-- Wireless Number" + formatPhNumber(d[phoneNumber]) + "Charges-" + d[charges]);});
                            
                            var yAxisNodes = svg.append("g")
                                    .attr("class", "y axis")
                                    .call(yAxis);

                            yAxisNodes.selectAll("text")
                                    .attr("id", function(d, i) {
                                        return ("yAxisText" + i);
                                    });

                            yAxisNodes.selectAll("line")
                                    .attr("id", function(d, i) {
                                        return ("yAxisLine" + i);
                                    });

                            //Remove minimum value label form Y Axis
                            d3.select("#yAxisText0").remove();
                            d3.select("#yAxisLine0").remove();
                            yAxisNodes.select("path").remove();

                            // Draw the y Grid lines
                            svg.append("g")
                                .attr("class", "grid")
                                .attr("transform", "translate(0,0)")
                                .call(make_y_axis()
                                        .tickSize(-width + x(2) - x(1), 0, 0)
                                        .tickFormat(""));
                            svg.select(".grid").selectAll("line").style("stroke", BarChartD3Config.gridLineColor);
                            // function for the y grid lines
                            function make_y_axis() {
                                return d3.svg.axis()
                                        .scale(y)
                                        .orient("left")
                                        .ticks(BarChartD3Config.yAxisMaxTicks).tickValues(yAxisData);
                            }
                            // Draw the x Grid lines
                            svg.append("g")
                                    .attr("class", "grid").attr("id", "xGrid")
                                    .call(make_x_axis()
                                        .tickSize(-height , 0)
                                        .tickFormat("")).selectAll("line").attr("transform", "translate(" + -Math.ceil((x(2) - x(1)) / 2 - BarChartD3Config.xAxisTextTopMargin) + ",0)")
                                    .attr("id", function (d, i) {
                                        return ("xAxisLine" + i);
                                    });

                            
                            // function for the x grid lines
                            function make_x_axis() {
                                return d3.svg.axis()
                                        .scale(x)
                                        .orient("top")
                                        .ticks(BarChartD3Config.yAxisMaxTicks)
                                        ;
                            }
                            svg.select("#xGrid").selectAll("line")
                                    .attr("id", function(d, i) {
                                        return ("xAxisLine" + i);
                                    });
                                    
                            function show_tooltip_grid_line(offsetX){
                                 for (var i = 0; i < barChartData.length; i++) {
                                     if(Math.round(x(i+1))===offsetX){
                                         svg.select("#xAxisLine"+i).style("stroke", BarChartD3Config.yaxisGridLineColor);
                                     }
                                }
                            }
                            
                            function hide_tooltip_grid_line(offsetX){
                                 for (var i = 0; i < barChartData.length; i++) {
                                     if(Math.round(x(i+1))===offsetX){
                                         svg.select("#xAxisLine"+i).style("stroke", "transparent");
                                     }
                                }
                            }
                            function mouseOver(d, offsetX, offsetY) {
                                offsetY = offsetY - BarChartD3Config.tooltipTopMargin;
                                if (isSingleDataObj) {
                                    offsetX = offsetX - BarChartD3Config.tooltipLeftMargin + (width / 2);
                                } else {
                                    offsetX = offsetX - BarChartD3Config.tooltipLeftMargin;
                                }
                                scope.tooltipFlag = true;
                                scope.tooltipStyle = {"left": offsetX + "px", "top": offsetY + "px"};
                                scope.dataPoint = {"name": d[userName], "phoneNumber": formatPhNumber(d[phoneNumber]), "charges": formatCharges(d[charges])};
                                scope.$apply();
                            }
                            scope.refreshChart = false;
                        });
                    }
                };
            }]);
angular.module('att.charts.coschartD3', ['att.charts.utilities'])
        .constant("CosChartD3Config",
                {
                    "chartcolor": {
                        "paletteColors": ["#097cb5", "#FEFFF7"] //default color options for rendering chart baseColor and NeedleColor,

                    },
                    "defaultcenterlabel": "COS 1",
                    "defaultcentercategory": "Real Time",
                    "zoomLevel": "25",
                    "doughnutratio": 20,
                    "legendreqd": "false",
                    "animduration": "300",
                    "legendposition": "top",
                    "centerTextValueDy": "0",
                    "centerTextValueDx": "0",
                    "centerTextPercentDx": "20",
                    "centerTextPercentDy": "-10",
                    "centerTextLabelDy": "20",
                    "centerTextLabelDx": "0",
                    "arcOverMargin": 5
                })
        .directive('attCosd3Chart', ['CosChartD3Config', '$timeout','$extendObj', function (CosChartD3Config, $timeout,$extendObj) {
                return {
                    restrict: 'A',
                    scope: {
                        chartConfig: '=',
                        initVal: '='
                    },
                    templateUrl: 'template/coschartD3/attCosd3Chart.html',
                    transclude: true,
                    replace: true,
                    controller: ['$scope', '$attrs', function ($scope, $attrs) {
                            if (angular.isDefined($scope.chartConfig)) {
                                CosChartD3Config = $extendObj.extendDeep(CosChartD3Config, $scope.chartConfig);
                            }
                            var legendColor = [];
                            legendColor = CosChartD3Config.chartcolor.paletteColors;
                            $scope.addLegendColor = function () {
                                return {"color": legendColor[0]};
                            };
                            $scope.chartID = $attrs.id;
                            if (!angular.isDefined($attrs.legendRequired)) {
                                $scope.legendRequired = CosChartD3Config.legendreqd;
                            } else {
                                $scope.legendRequired = $attrs.legendRequired;
                            }
                            if (!angular.isDefined($attrs.legendPosition)) {
                                $scope.legendPosition = CosChartD3Config.legendposition;
                            } else {
                                $scope.legendPosition = $attrs.legendPosition;
                            }
                        }],
                    link: function (scope, element, attrs) {
                        // var radius = Math.min(attrs.chartWidth, attrs.chartHeight) / 2,
                        var color = d3.scale.ordinal().range(CosChartD3Config.chartcolor.paletteColors),
                                zoom = parseInt(CosChartD3Config.zoomLevel, 0),data,
                                margin = {// optionally set margins
                                    top: zoom,
                                    right: zoom,
                                    bottom: zoom,
                                    left: zoom
                                },
                        width = attrs.chartWidth,
                                height = attrs.chartHeight,
                                radius = Math.min(
                                        width - (margin.left + margin.right),
                                        height - (margin.top + margin.bottom)) / 2;
                        scope.LegendLabel = CosChartD3Config.defaultcenterlabel;
                        scope.LegendCategory = CosChartD3Config.defaultcentercategory;
                        scope.$watch('initVal', function (value) {
                            if(!angular.isDefined(scope.initVal)){
                                return;
                            }
                            d3.select("svg#" + attrs.id).remove();
                            d3.selectAll("svg#" + attrs.id + " > *").remove();
                            var cosval = parseInt(scope.initVal.value, 10);
                            scope.cosval = cosval + "%";
                            if(cosval>100){
                                data = [
                                        {"name": "cos1", "value": 100},
                                        {"name": "rest", "value": 0}
                                        ];
                                }else{
                                        data = [
                                        {"name": "cos1", "value": cosval},
                                        {"name": "rest", "value": 100 - (parseInt(cosval, 10))}
                                ];
                            }
                            var sliderpercent = parseInt(cosval, 10);
                            if (angular.isDefined(sliderpercent)) {
                                sliderpercent = 0;
                            }
                            element[0].querySelector('.cosd3Container').setAttribute('id', attrs.id);
                            // build chart
                            var svg = d3.select(".cosd3Container#" + attrs.id)
                                    .attr("style", "height: " + attrs.chartHeight + "px;")
                                    .append("svg")
                                    .attr("id", attrs.id)
                                    .attr("width", "100%")
                                    .attr("height", "100%")
                                    .attr('viewBox', '0 0 ' + Math.min(width, height) + ' ' + Math.min(width, height))
                                    .attr('preserveAspectRatio', 'xMinYMin')
                                    .append("g")
                                    .attr("transform", "translate(" + Math.min(width, height) / 2 + "," + Math.min(width, height) / 2 + ")");
                            // set radius
                            var arc = d3.svg.arc()
                                    .innerRadius(radius - CosChartD3Config.doughnutratio)
                                    .outerRadius(radius);
                            // set hovered radius
                            var arcOver = d3.svg.arc()
                                    .innerRadius(radius - CosChartD3Config.doughnutratio)
                                    .outerRadius(radius + CosChartD3Config.arcOverMargin);

                            // add center text element
                            var centerTextValue = svg.append("text")
                                    .attr("y", CosChartD3Config.centerTextValueDy)
                                    .attr("x", CosChartD3Config.centerTextValueDx)
                                    .attr("class", "coschartcenterValue");
                            var centerTextPercent = svg.append("text")
                                    .attr("x", CosChartD3Config.centerTextPercentDx)
                                    .attr("y", CosChartD3Config.centerTextPercentDy)
                                    .attr("class", "coschartcenterPercent");
                            var centerTextLabel = svg.append("text")
                                    .attr("y", CosChartD3Config.centerTextLabelDy)
                                    .attr("x", CosChartD3Config.centerTextLabelDx)
                                    .attr("class", "coschartcenterLabel");
                            centerTextValue.text(cosval);
                            centerTextPercent.text("%");
                            centerTextLabel.text(CosChartD3Config.defaultcenterlabel + "(" + CosChartD3Config.defaultcentercategory + ")");

                            var pie = d3.layout.pie()
                                    .sort(null)
                                    .startAngle(3.2 * Math.PI)
                                    .endAngle(5.2 * Math.PI)
                                    .value(function (d) {
                                        return d.value;
                                    });

                            // set chart attributes and bind hover events
                            var g = svg.selectAll(".arc")
                                    .data(pie(data))
                                    .enter()
                                    .append("g")
                                    .attr("class", "arc")
                                    .attr("id", function (d, i) {
                                        return "cosarc-" + i;
                                    })
                                    .style("cursor", "pointer");

                            g.append("path")
                                    .style("fill", function (d) {
                                        return color(d.data.name);
                                    })
                                    .attr("id", function (d, i) {
                                        return "cospath-" + i;
                                    })
                                    .on("mouseover", function (d) {
                                        d3.select(this).transition()
                                                .duration(CosChartD3Config.animduration)
                                                .attr("d", arcOver);
                                    })
                                    .on("mouseout", function (d) {
                                        d3.select(this).transition()
                                                .duration(CosChartD3Config.animduration)
                                                .attr("d", arc);
                                    })
                                    .transition()
                                    .ease("exp")
                                    .duration(0)
                                    .attrTween("d", tweenPie);

                            // animate function
                            function tweenPie(b) {
                                var i = d3.interpolate({
                                    startAngle: 2.1 * Math.PI,
                                    endAngle: 2.1 * Math.PI
                                }, b);
                                return function (t) {
                                    return arc(i(t));
                                };
                            }
                            if(cosval>0 && cosval<100){
                                d3.select(".cosd3Container path#cospath-1").style('stroke', 'black');
                            }
                            


                        });
                    }
                };
            }]);
angular.module('att.charts.coschartwithbarD3', ['att.charts.utilities'])
.constant("CosChartWithBarD3Config",
        {
            "chartcolor": {
                "paletteColors": ["#0574ac", "#44c8f5", "#4ca90c", "#da0081", "#ff9900", "#81017e"], //["#097cb5", "#FEFFF7"], //default color options for rendering chart baseColor and NeedleColor,
                "zeroColor": "#ffffff",
                "borderColor": "#666666"
            },
            "zoomLevel": "25",
            "doughnutratio": 20,
            "barreqd": "true",
            "barHeight": 50,
            "barWidth": 150,
            "barCornerRadius": 9,
            //"barTextValueDy": "250",
            //"barTextValueDx": "0",
            //"barTextPercentDx": "20",
            //"barTextPercentDy": "-250",
            //"barTextLabelDy": "60",
            //"barTextLabelDx": "-80",
            //"defaultbarlabel": "CoS 2V/CoS 2/CoS 3/CoS 4/CoS 5",
            "animduration": "300",   
            "centerText": {
                'defaultCenterLabel': 'COS 1',
                'defaultCenterCategory': 'Real Time',
                'textValueDy': '0',
                'textValueDx': '0',
                'textPercentDx': '20',
                'textPercentDy': '-10',
                'textLabelDx': '0',
                'textLabelDy': '20',
                'color': '#666666',
                'size': '36px',
                'font': 'omnes_att_light'
            },
            "centerTextValueDy": "0",
            "centerTextValueDx": "0",
            "centerTextPercentDx": "20",
            "centerTextPercentDy": "-10",
            "centerTextLabelDy": "20",
            "centerTextLabelDx": "0",
            "arcOverMargin": 5
        })
.directive('attCosBarD3Chart', ['CosChartWithBarD3Config', '$timeout','$extendObj', function (CosChartWithBarD3Config, $timeout,$extendObj) {return {
            restrict: 'A',
            scope: {
                chartConfig: '=',
                chartData: '=',
                totalCos: '=',
                remainingCos: '=',
                mouseOver: '=?'
            },
            templateUrl: 'template/coschartwithbarD3/attCosBarD3Chart.html',
            transclude: true,
            replace: true,
            controller: ['$scope', '$attrs', function ($scope, $attrs) {
                if (angular.isDefined($scope.chartConfig)) {
                    CosChartWithBarD3Config = $extendObj.extendDeep(CosChartWithBarD3Config, $scope.chartConfig);
                }

                $scope.chartID = $attrs.id;
                if (!angular.isDefined($attrs.barRequired)) {
                    $scope.barRequired = CosChartWithBarD3Config.barreqd;
                } else {
                    $scope.barRequired = $attrs.barRequired;
                }

                }],
            link: function (scope, element, attrs) {
                /*
                    x: x-coordinate
                    y: y-coordinate
                    w: width
                    h: height
                    r: corner radius
                    tl: top_left rounded?
                    tr: top_right rounded?
                    bl: bottom_left rounded?
                    br: bottom_right rounded?
                 */
                function rounded_rect(x, y, w, h, r, tl, tr, bl, br) {
                    var retval;
                    retval  = "M" + (x + r) + "," + y;
                    retval += "h" + (w - 2*r);
                    if (tr) { retval += "a" + r + "," + r + " 0 0 1 " + r + "," + r; }
                    else { retval += "h" + r; retval += "v" + r; }
                    retval += "v" + (h - 2*r);
                    if (br) { retval += "a" + r + "," + r + " 0 0 1 " + -r + "," + r; }
                    else { retval += "v" + r; retval += "h" + -r; }
                    retval += "h" + (2*r - w);
                    if (bl) { retval += "a" + r + "," + r + " 0 0 1 " + -r + "," + -r; }
                    else { retval += "h" + -r; retval += "v" + -r; }
                    retval += "v" + (2*r - h);
                    if (tl) { retval += "a" + r + "," + r + " 0 0 1 " + r + "," + -r; }
                    else { retval += "v" + -r; retval += "h" + r; }
                    retval += "z";
                    return retval;
                }

                var radScale = d3.scale.linear().domain([0, 100]).range([0, 2 * Math.PI]);

                 
                var zoom = parseInt(CosChartWithBarD3Config.zoomLevel, 0),data,
                    margin = {// optionally set margins
                        top: zoom,
                        right: zoom,
                        bottom: zoom,
                        left: zoom
                    },
                    width = attrs.chartWidth,
                    height = attrs.chartHeight,
                    radius = Math.min(
                            width - (margin.left + margin.right),
                            height - (margin.top + margin.bottom)) / 2;


                scope.$watchCollection('chartData', function (value) {
                    var j = 0; // index for for-loops

                    if(!angular.isDefined(scope.chartData) || scope.chartData.length < 1){
                        return;
                    }

                    d3.select("svg#" + attrs.id).remove();
                    d3.selectAll("svg#" + attrs.id + " > *").remove();

                    // SnickrBar
                    d3.select("svg#" + scope.snickrbarId).remove();
                    d3.selectAll("svg#" + scope.snickrbarId + " > *").remove();

                    var cosval = scope.chartData[0];
                    var remainingCosVal = 0, restCosVal = 0;

                    for(j = 1; j < scope.chartData.length; j++)
                    {
                        remainingCosVal += parseInt(scope.chartData[j], 10); //parseInt allows strings to be passed in
                    }
                    restCosVal = 100 - cosval - remainingCosVal;
                    scope.totalCos = cosval + remainingCosVal;
                    scope.remainingCos = remainingCosVal;

                    var data = angular.copy(scope.chartData);
                    if (angular.isDefined(restCosVal)) {
                        data.push(restCosVal);
                    }

                    element[0].querySelector('.cosd3Container').setAttribute('id', attrs.id);
                    // build chart
                    var svg = d3.select(".cosd3Container#" + attrs.id)
                            .attr("style", "height: " + attrs.chartHeight + "px;")
                            .append("svg")
                            .attr("id", attrs.id)
                            .attr("width", "100%")
                            .attr("height", "100%")
                            .attr('viewBox', '0 0 ' + Math.min(width, height) + ' ' + Math.min(width, height))
                            .attr('preserveAspectRatio', 'xMinYMin')
                            .append("g")
                            .attr("transform", "translate(" + Math.min(width, height) / 2 + "," + Math.min(width, height) / 2 + ")");

                    scope.snickrbarId = attrs.id + 'snickrbar';
                    var snickrbar = d3.select(".snickrBar")
                        .attr('style', 'margin-left:' + Math.floor(CosChartWithBarD3Config.barWidth/3) + ';')
                        .style({'background-color': CosChartWithBarD3Config.chartcolor.zeroColor, 'width': CosChartWithBarD3Config.barWidth + 'px', 'border': '1px solid #777', 'border-radius': '10px'})
                        .append("svg")
                        .attr("id", scope.snickrbarId)
                        .attr("width", CosChartWithBarD3Config.barWidth + "px")
                        .attr("height", CosChartWithBarD3Config.barHeight + "px")
                        .append("g");

                    // set radius
                    var arc = d3.svg.arc()
                            .innerRadius(radius - CosChartWithBarD3Config.doughnutratio)
                            .outerRadius(radius);

                    // set hovered radius
                    var arcOver = d3.svg.arc()
                            .innerRadius(radius - CosChartWithBarD3Config.doughnutratio)
                            .outerRadius(radius + CosChartWithBarD3Config.arcOverMargin);

                    // add center text element
                    var centerTextValue = svg.append("text")
                            .attr("y", CosChartWithBarD3Config.centerText.textValueDy)
                            .attr("x", CosChartWithBarD3Config.centerText.textValueDx)
                            .attr("class", "coschartcenterValue");
                    var centerTextPercent = svg.append("text")
                            .attr("x", CosChartWithBarD3Config.centerText.textPercentDx)
                            .attr("y", CosChartWithBarD3Config.centerText.textPercentDy)
                            .attr("class", "coschartcenterPercent");
                    var centerTextLabel = svg.append("text")
                            .attr("y", CosChartWithBarD3Config.centerText.textLabelDy)
                            .attr("x", CosChartWithBarD3Config.centerText.textLabelDx)
                            .attr("class", "coschartcenterLabel");
                    centerTextValue.text(cosval);
                    centerTextPercent.text("%");
                    centerTextLabel.text(CosChartWithBarD3Config.centerText.defaultCenterLabel + "(" + CosChartWithBarD3Config.centerText.defaultCenterCategory + ")");

                    var scale = function(d) {
                        if (d <= 0) {
                            return 0;
                        } else {
                            var r = (d * CosChartWithBarD3Config.barWidth) / 100;
                            if (isNaN(r)) { r = 0; }
                            else if (!isFinite(r)) { r = CosChartWithBarD3Config.barWidth; }

                            return r;
                        }
                    };

                    // Only executed when totalCos is 0
                    if (cosval <= 0) {
                        arc.startAngle(0).endAngle(2 * Math.PI); //just radians
                        svg.append("path")
                                .attr("d", arc)
                                .attr("fill", CosChartWithBarD3Config.chartcolor.zeroColor)
                                .style("stroke", CosChartWithBarD3Config.chartcolor.borderColor)
                                .style("stroke-width", 1);
                    } else {

                        // JVM: This code is temp for just snicker's bar release
                        var temp = 100-data[0];
                        var newData = [];
                        newData.push({'value':data[0]});
                        newData.push({'value':temp});

                        /*for(j = 0; j < data.length; j++) {
                            var obj = angular.extend({}, data[j]);
                            obj.value = data[j];
                            newData.push(obj);
                        }*/


                        //======================================================


                        var pie = d3.layout.pie()
                            .sort(null)
                            .value(function (d,index) {
                                var r = d.value;
                                if (d.value <= 0) { r = 0; }
                                return r;
                            });

                        // set chart attributes and bind hover events
                        var g = svg.selectAll(".arc")
                                .data(pie(newData))
                                .enter()
                                .append("g")
                                .attr("class", "arc")
                                .attr("id", function (d, i) {
                                    return "cosarc-" + i;
                                })
                                .style("cursor", "pointer");

                        g.append("path")
                                .style("fill", function (d, i) {
                                    if (i === CosChartWithBarD3Config.chartcolor.paletteColors.length || i === newData.length-1 || d.data.value === 0) {
                                        return CosChartWithBarD3Config.chartcolor.zeroColor;
                                    }

                                    return CosChartWithBarD3Config.chartcolor.paletteColors[i];
                                })
                                .style("stroke", "white")
                                .style("stroke-width", function(d,index) { if (cosval >= 100) { return 0; } else { return 1; } })
                                .attr("id", function (d, i) {
                                    return "cospath-" + i;
                                })
                                .on("mouseover", function (d, index) {
                                    if (index < newData.length-1) {
                                        d3.select(this).transition()
                                            .duration(CosChartWithBarD3Config.animduration)
                                            .attr("d", arcOver);
                                    }
                                })
                                .on("mouseout", function (d, index) {
                                    if (index < newData.length-1) {
                                        d3.select(this).transition()
                                                .duration(CosChartWithBarD3Config.animduration)
                                                .attr("d", arc);
                                    }
                                })
                                .transition()
                                .ease("exp")
                                .duration(0)
                                .attrTween("d", tweenPie);
                    } // end of else

                    if (!CosChartWithBarD3Config.barreqd) { return; }
                    var snickrData = [];
                    var d = scope.chartData.slice(1, scope.chartData.length);
                    for (j = 0; j < d.length; j++) {
                        if (d[j] !== 0) {
                            var obj = {};
                            obj.data = d[j];
                            obj.color = CosChartWithBarD3Config.chartcolor.paletteColors[j+1];
                            snickrData.push(obj);    
                        }
                    }

                    var prevWidth = 0;
                    var nextX = function(d, i) {
                        if (i === 0) {
                            prevWidth = scale(d);
                            return 0;
                        } else {
                            var p = prevWidth;
                            prevWidth += scale(d);
                            return p;
                        }
                    };

                    var rect = snickrbar.selectAll("path")
                        .data(snickrData)
                        .enter()
                        .append("path")
                        .style("fill", function(d, i) { 
                            if (i >= CosChartWithBarD3Config.chartcolor.paletteColors.length || d.data === 0) {
                                return CosChartWithBarD3Config.chartcolor.zeroColor;
                            }

                            return d.color;
                        })
                        .attr("d", function(d, index) {
                            if (index === 0) {
                                // Left rounded rectangle
                                return rounded_rect(nextX(d.data, index), 0, scale(d.data), CosChartWithBarD3Config.barHeight, CosChartWithBarD3Config.barCornerRadius, true, false, true, false);
                            } else if (index === snickrData.length-1 && remainingCosVal >= 100) {
                                // Right rounded rectangle
                                return rounded_rect(nextX(d.data, index), 0, scale(d.data), CosChartWithBarD3Config.barHeight, CosChartWithBarD3Config.barCornerRadius, false, true, false, true);
                            } else {
                                console.log('normal rect at ' + index);
                                return rounded_rect(nextX(d.data, index), 0, scale(d.data), CosChartWithBarD3Config.barHeight, 0, false, false, false, false); 
                            }
                        });

                    if (remainingCosVal === 0) {
                        snickrbar.attr("fill", CosChartWithBarD3Config.chartcolor.zeroColor);
                    }

                    // add bar text element
                    /*var snickerText = d3.select('.snickrBar svg')
                        .append("g")
                        .style("fill", "black");

                    var barTextValue = snickerText.append("text")
                            .attr("y", CosChartWithBarD3Config.barTextValueDy)
                            .attr("x", CosChartWithBarD3Config.barTextValueDx)
                            .attr("class", "coschartbarcenterValue");
                    var barTextPercent = snickerText.append("text")
                            .attr("x", CosChartWithBarD3Config.barTextPercentDx)
                            .attr("y", CosChartWithBarD3Config.centerTextPercentDy)
                            .attr("class", "coschartbarcenterPercent");
                    var barTextLabel = snickerText.append("text")
                            .attr("y", CosChartWithBarD3Config.barTextLabelDy)
                            .attr("x", CosChartWithBarD3Config.barTextLabelDx)
                            .attr("class", "coschartbarcenterLabel");
                    barTextValue.text(cosval);
                    barTextPercent.text("%");
                    barTextLabel.text(CosChartWithBarD3Config.defaultbarlabel);*/

                    

                    // animate function
                    function tweenPie(b) {
                        var i = d3.interpolate({
                            startAngle: 2.1 * Math.PI,
                            endAngle: 2.1 * Math.PI
                        }, b);
                        return function (t) {
                            return arc(i(t));
                        };
                    }

                    scope.$watch('mouseOver', function(val) {
                        if (!angular.isDefined(scope.mouseOver)) { return; }
                        var id = scope.mouseOver.split('-')[1];
                        if (scope.mouseOver.split('-')[0] === 'true') {
                            if (id === '0') {
                                d3.select("#cospath-" + id).transition()
                                    .duration(CosChartWithBarD3Config.animduration)
                                    .attr("d", arcOver);
                            } else {
                                for (var i = 0; i < newData.length-1; i++) {
                                    d3.select("#cospath-" + (i+1)).transition()
                                        .duration(CosChartWithBarD3Config.animduration)
                                        .attr("d", arcOver);
                                }
                            }
                        } else {
                            if (id === '0') {
                                d3.select("#cospath-" + id).transition()
                                    .duration(CosChartWithBarD3Config.animduration)
                                    .attr("d", arc);
                            } else {
                                for (j = 0; j < newData.length-1; j++) {
                                    d3.select("#cospath-" + (j+1)).transition()
                                        .duration(CosChartWithBarD3Config.animduration)
                                        .attr("d", arc);
                                }
                            }
                        }
                    });

                });

            }
        };
    }]);
angular.module('att.charts.cosdeletionD3', ['att.charts.utilities'])
        .constant("CosdeletionD3Config",
                {
                    "chartcolor": {
                        "MaxBandwidthReached": "#d3d3d3",
                        "ZeroBandwidthSelected": "#FEFFF7",
                        "RemainingBandwidthColor": "#ffffff",
                        "RemainingBandwidthStrokeColor": "#d3d3d3",
                        "RemainingBandwidthStrokeWidth": 1.5,
                        "UsedBandwidthColor": "#0574AC",
                        "UsedBandwidthStrokeColor":"#efefef", //white
                        "UsedBandwidthStrokeWidth":"1"
                    },
                    "defaultcenterlabel": "Kbps",
                    "zoomLevel": "10",
                    "doughnutratio": 21,
                    "animduration": "300",
                    "guageStartAngle":4,
                    "guageEndAngle":8.56,
                    "legendreqd": "false",
                    "initduration": "1000",
                    "legendposition": "top",
                    "centerTextLabelDy": "20",
                    "centerTextValueDy": "0",
                    "centerTextValueDx": "-30",
                    "centerTextLabelDx": "-10",
                    "maxbandwidthTextDx": "25",
                    "maxbandwidthTextDy": "70",
                    "arcOverMargin": 5
                })
        .directive('attCosdeletiond3Chart', ['CosdeletionD3Config', '$timeout','$extendObj', function(CosdeletionD3Config, $timeout,$extendObj) {
                return {
                    restrict: 'A',
                    scope: {
                        maxValue: '=',
                        chartData: '=',
                        chartConfig: '=',
                        resetChart: '=',
                        removeArcs: '='
                    },
                    controller: ['$scope', '$attrs', function($scope, $attrs) {
                        if (angular.isDefined($scope.chartConfig)) {
                            CosdeletionD3Config = $extendObj.extendDeep(CosdeletionD3Config, $scope.chartConfig);
                        }

                        }],
                    link: function(scope, element, attrs) {
                        var zoom = parseInt(CosdeletionD3Config.zoomLevel, 0),
                                margin = {// optionally set margins
                                    top: zoom,
                                    right: zoom,
                                    bottom: zoom,
                                    left: zoom
                                },
                        width = attrs.chartWidth,
                                height = attrs.chartHeight,
                                radius = Math.min(
                                        width - (margin.left + margin.right),
                                        height - (margin.top + margin.bottom)) / 2,arcPath;
                        // set radius
                        var arc = d3.svg.arc()
                                .innerRadius(radius - CosdeletionD3Config.doughnutratio)
                                .outerRadius(radius);
                        var arcOver = d3.svg.arc()
                                        .innerRadius(radius - CosdeletionD3Config.doughnutratio)
                                        .outerRadius(radius + CosdeletionD3Config.arcOverMargin);
                       
                        // Browser onresize event
                        window.onresize = function() {
                            scope.$apply();
                        };
                        scope.$watch('resetChart', function(newValue, oldValue) {
                            if(scope.resetChart === true){
                                scope.resetChart = false;
                                return;
                            }
                            return scope.render(newValue, oldValue);
                        });
                        scope.render = function(newValue, oldValue) {
                            d3.select("svg#" + attrs.id).remove();
                            d3.selectAll("svg#" + attrs.id + " > *").remove();
                            if (!scope.chartData)
                            {
                                return;
                            }
                            var data=angular.copy(scope.chartData);
                            data=data.reverse();
                            var totalBandwidth=0,totalUsed=0,remainingBandwidth=0,dynamicColorRange=[],noCheckBoxSelected=true,selectedBandwidth=0,selectedElem=[];
                            if (angular.isDefined(scope.maxValue)) {
                                totalBandwidth = parseInt(scope.maxValue, 0);
                            }
                            for (var j = 0; j < data.length; j++) {
                                totalUsed = (totalUsed + parseInt(data[j].value, 0));
                                if(angular.equals(data[j].status, true)){
                                    noCheckBoxSelected=false;
                                    selectedBandwidth=selectedBandwidth+data[j].value;
                                    
                                    data[j].value=0;
                                    dynamicColorRange.push(CosdeletionD3Config.chartcolor.MaxBandwidthReached);
                                   
                                }else{
                                    selectedElem.push({index: data[j].id, value: data[j].value});
                                    dynamicColorRange.push(CosdeletionD3Config.chartcolor.UsedBandwidthColor);
                                }
                            }
                           if(scope.removeArcs){
                                while(data.length > 0) {
                                    data.pop();
                                }
                              dynamicColorRange=[];
                              data.push({name: "totalUsed", value: totalUsed-selectedBandwidth,id:"totalUsed"});
                              dynamicColorRange.push(CosdeletionD3Config.chartcolor.UsedBandwidthColor);
                              scope.removeArcs=false;
                           }
                            if(totalUsed>0 && totalUsed<totalBandwidth){
                                remainingBandwidth=totalBandwidth-totalUsed;
                                if(!noCheckBoxSelected){
                                    data.push({name: "selectedBandwidth", value: selectedBandwidth,id:"selectedBandwidth"});
                                    dynamicColorRange.push(CosdeletionD3Config.chartcolor.MaxBandwidthReached);
                                }
                                data.push({name: "remaining", value: remainingBandwidth,id:"remaining"});
                                dynamicColorRange.push(CosdeletionD3Config.chartcolor.RemainingBandwidthColor);
                            }
                            element[0].setAttribute('id', attrs.id);
                            var svg = d3.select(element[0])
                                    .attr("style", "height: " + attrs.chartHeight + "px;")
                                    .append("svg")
                                    .attr("id", attrs.id)
                                    .attr("radius", radius)
                                    .attr("width", attrs.chartWidth)
                                    .attr("height", attrs.chartHeight)
                                    .attr('viewBox', '0 0 ' + Math.min(width, height) + ' ' + Math.min(width, height))
                                    .attr('preserveAspectRatio', 'xMinYMin')
                                    .append("g")
                                    .attr("transform", "translate(" + Math.min(width, height) / 2 + "," + Math.min(width, height) / 2 + ")");

                            var centerTextValue = svg.append("text")
                                    .attr("y", CosdeletionD3Config.centerTextValueDy)
                                    .attr("x", CosdeletionD3Config.centerTextValueDx)
                                    .attr("class", "cosdeletioncenterValue");
                            var centerTextLabel = svg.append("text")
                                    .attr("y", CosdeletionD3Config.centerTextLabelDy)
                                    .attr("x", CosdeletionD3Config.centerTextLabelDx)
                                    .attr("class", "cosdeletioncenterLabel");
                            var maxbandwidthText = svg.append("text")
                                    .attr("y", CosdeletionD3Config.maxbandwidthTextDy)
                                    .attr("x", CosdeletionD3Config.maxbandwidthTextDx)
                                    .attr("class", "cosdeletionmaxbandwidthText");
                         
                            var color = d3.scale.ordinal().range(dynamicColorRange);
                            centerTextValue.text(totalUsed-selectedBandwidth);
                            centerTextLabel.text(CosdeletionD3Config.defaultcenterlabel);
                            maxbandwidthText.text(totalBandwidth + " " + CosdeletionD3Config.defaultcenterlabel);
                            
                            var pie = d3.layout.pie()
                                    .sort(null)
                                    .startAngle(CosdeletionD3Config.guageStartAngle)
                                    .endAngle(CosdeletionD3Config.guageEndAngle)
                                    .value(function(d) {
                                        return d.value;
                                    });
                              
                            var g = svg.selectAll(".arc")
                                    .data(pie(data))
                                    .enter()
                                    .append("g")
                                    .attr("class", "arc")
                                    .attr("id", function(d, i) {
                                        return "cosdeletionarc-" + d.data.id;
                                    })
                                    .style("cursor", "pointer");
                            
                            arcPath = g.append("path")
                                        .style("fill", function(d) {
                                            return color(d.data.name);
                                        }).attr("id", function(d, i) {
                                            return "cosdeletionpath-" + d.data.id;
                                        })
                                        .transition()
                                        .ease("exp")
                                        .duration(0)
                                        .attrTween("d", tweenPie);
                                
                            arcPath = arcPath[0];
                            for (var i = 0; i < arcPath.length; i++) {
                                    var id = arcPath[i].id;
                                    if (id !== 'cosdeletionpath-remaining' && id !== 'cosdeletionpath-selectedBandwidth') {
                                        d3.select("path#" + id).transition().attr("d", arcOver);
                                    }
                                }
                          
                            if (remainingBandwidth > 0) {
                                if(angular.isDefined(newValue) && angular.isDefined(oldValue)){
                                    for (var k = 0; k < selectedElem.length; k++) {
                                         var index =selectedElem[k].index;
                                        d3.select("path#cosdeletionpath-" + index).style('stroke', CosdeletionD3Config.chartcolor.UsedBandwidthStrokeColor).style("stroke-width", CosdeletionD3Config.chartcolor.UsedBandwidthStrokeWidth);
                                    }
                                }
                                d3.select("#" + attrs.id + " path#cosdeletionpath-remaining").style('stroke', CosdeletionD3Config.chartcolor.RemainingBandwidthStrokeColor).style("stroke-width", CosdeletionD3Config.chartcolor.RemainingBandwidthStrokeWidth);
                            } 
                        };
                        
                        function tweenPie(b) {
                                var i = d3.interpolate({
                                    startAngle: 2.1 * Math.PI,
                                    endAngle: 2.1 * Math.PI
                                }, b);
                                return function(t) {
                                    return arc(i(t));
                                };
                            }
                    }
                };
            }]);
angular.module('att.charts.cosmultichartD3', ['att.charts.utilities'])
        .constant("CosMultiChartD3Config",
                {
                    "chartcolor": {
                        "paletteColors": ["#44c6f7", "#4ba90b", "#d70e80", "#ff9900", "#81017e", "#cccccc"], //default color options for rendering chart baseColor and NeedleColor,
                        "zeroTicketColor": ["#d3d3d3"]
                    },
                    "defaultcenterlabel": "CoS",
                    "defaultcentercategory": "2V/2/3/4/5",
                    "zoomLevel": "25",
                    "doughnutratio": 20,
                    "animduration": "300",
                    "legendreqd": "false",
                    "initduration": "1000",
                    "legendposition": "top",
                    "centerTextValueDy": "0",
                    "centerTextValueDx": "0",
                    "centerTextPercentDx": "22",
                    "centerTextPercentDy": "-8",
                    "centerTextLabelDy": "20",
                    "centerTextLabelDx": "0",
                    "arcOverMargin": 5
                })
        .directive('attCosmultid3Chart', ['CosMultiChartD3Config', '$timeout','$extendObj', function (CosMultiChartD3Config, $timeout,$extendObj) {
                return {
                    restrict: 'A',
                    scope: {
                        chartData: '=',
                        chartConfig: '='
                    },
                    templateUrl: 'template/cosmultichartD3/attCosmultid3Chart.html',
                    transclude: true,
                    replace: true,
                    controller: ['$scope', '$attrs', function ($scope, $attrs) {

                            if (angular.isDefined($scope.chartConfig)) {
                                CosMultiChartD3Config = $extendObj.extendDeep(CosMultiChartD3Config, $scope.chartConfig);
                            }

                            $scope.addLegendColor = function (id, item) {
                                return {"color": CosMultiChartD3Config.chartcolor.paletteColors[id]};
                            };

                            $scope.chartID = $attrs.id;
                            if (!angular.isDefined($attrs.legendRequired)) {
                                $scope.legendRequired = CosMultiChartD3Config.legendreqd;
                            } else {
                                $scope.legendRequired = $attrs.legendRequired;
                            }
                            if (!angular.isDefined($attrs.legendPosition)) {
                                $scope.legendPosition = CosMultiChartD3Config.legendposition;
                            } else {
                                $scope.legendPosition = $attrs.legendPosition;
                            }
                        }],
                    link: function (scope, element, attrs) {
                        var zoom = parseInt(CosMultiChartD3Config.zoomLevel, 0),
                                pi = Math.PI,
                                margin = {// optionally set margins
                                    top: zoom,
                                    right: zoom,
                                    bottom: zoom,
                                    left: zoom
                                },
                        width = attrs.chartWidth,
                                height = attrs.chartHeight,
                                radius = Math.min(
                                        width - (margin.left + margin.right),
                                        height - (margin.top + margin.bottom)) / 2;

                        function renderChart() {
                            var color = d3.scale.ordinal().range(CosMultiChartD3Config.chartcolor.paletteColors);
                            d3.select("svg#" + attrs.id).remove();
                            d3.selectAll("svg#" + attrs.id + " > *").remove();
                            var totalCosVal = 0, remainingCosVal;
                            for (var j = 0; j < scope.chartData.length; j++) {
                                var chartObj = scope.chartData[j];
                                totalCosVal = (totalCosVal + parseInt(chartObj.value, 10));
                            }
                            remainingCosVal = 100 - parseInt(totalCosVal, 10);
                            var data = angular.copy(scope.chartData);
                            if (angular.isDefined(remainingCosVal)) {
                                data.push({name: "rest", value: remainingCosVal});
                            }
                            element[0].querySelector('.cosmultid3Container').setAttribute('id', attrs.id);
                            // build chart
                            var svg = d3.select(".cosmultid3Container#" + attrs.id)
                                    .attr("style", "height: " + attrs.chartHeight + "px;")
                                    .append("svg")
                                    .attr("id", attrs.id)
                                    .attr("width", "100%")
                                    .attr("height", "100%")
                                    .attr('viewBox', '0 0 ' + Math.min(width, height) + ' ' + Math.min(width, height))
                                    .attr('preserveAspectRatio', 'xMinYMin')
                                    .append("g")
                                    .attr("transform", "translate(" + Math.min(width, height) / 2 + "," + Math.min(width, height) / 2 + ")");
                            // set radius
                            var arc = d3.svg.arc()
                                    .innerRadius(radius - CosMultiChartD3Config.doughnutratio)
                                    .outerRadius(radius);
                            // set hovered radius
                            var arcOver = d3.svg.arc()
                                    .innerRadius(radius - CosMultiChartD3Config.doughnutratio)
                                    .outerRadius(radius + CosMultiChartD3Config.arcOverMargin);
                            // add center text element
                            var centerTextValue = svg.append("text")
                                    .attr("y", CosMultiChartD3Config.centerTextValueDy)
                                    .attr("x", CosMultiChartD3Config.centerTextValueDx)
                                    .attr("class", "cosmultichartcenterValue");
                            var centerTextPercent = svg.append("text")
                                    .attr("x", CosMultiChartD3Config.centerTextPercentDx)
                                    .attr("y", CosMultiChartD3Config.centerTextPercentDy)
                                    .attr("class", "cosmultichartcenterPercent");
                            var centerTextLabel = svg.append("text")
                                    .attr("y", CosMultiChartD3Config.centerTextLabelDy)
                                    .attr("x", CosMultiChartD3Config.centerTextLabelDx)
                                    .attr("class", "cosmultichartcenterLabel");
                            centerTextValue.text(totalCosVal);
                            centerTextPercent.text("%");
                            centerTextLabel.text(CosMultiChartD3Config.defaultcenterlabel +"  " + CosMultiChartD3Config.defaultcentercategory);

                            if (totalCosVal <= 0) {
                                arc.startAngle(0).endAngle(2 * pi); //just radians
                                svg.append("path")
                                        .attr("d", arc)
                                        .attr("fill", CosMultiChartD3Config.chartcolor.zeroTicketColor);
                            } else {
                                var pie = d3.layout.pie()
                                        .sort(null)
                                        .startAngle(3.2 * Math.PI)
                                        .endAngle(5.2 * Math.PI)
                                        .value(function (d) {
                                            return d.value;
                                        });

                                var g = svg.selectAll(".arc")
                                        .data(pie(data))
                                        .enter()
                                        .append("g")
                                        .attr("class", "arc")
                                        .attr("id", function (d, i) {
                                            return "cosmultiarc-" + i;
                                        })
                                        .style("cursor", "pointer");

                                g.append("path")
                                        .style("fill", function (d) {
                                            return color(d.data.name);
                                        })
                                        .attr("id", function (d, i) {
                                            return "cosmultipath-" + i;
                                        })
                                        .style("stroke", "white")
                                        .style("stroke-width", 1)
                                        .on("mouseover", function (d) {
                                            d3.select(this).transition()
                                                    .duration(CosMultiChartD3Config.animduration)
                                                    .attr("d", arcOver);
                                        })
                                        .on("mouseout", function (d) {
                                            d3.select(this).transition()
                                                    .duration(CosMultiChartD3Config.animduration)
                                                    .attr("d", arc);
                                        })
                                        .transition()
                                        .ease("exp")
                                        .duration(0)
                                        .attrTween("d", tweenPie);


                            }
                            function tweenPie(b) {
                                var i = d3.interpolate({
                                    startAngle: 2.1 * Math.PI,
                                    endAngle: 2.1 * Math.PI
                                }, b);
                                return function (t) {
                                    return arc(i(t));
                                };
                            }
                        }

                        scope.$watch('chartConfig', function(value) {
                            if (angular.isDefined(scope.chartConfig)) {
                                CosMultiChartD3Config = $extendObj.extendDeep(CosMultiChartD3Config, scope.chartConfig);
                            }

                            renderChart();
                        }, true);

                        scope.$watch('chartData', function (value) {
                            renderChart();
                        });
                    }
                };
            }]);
angular.module('att.charts.donutD3', ['att.charts.utilities'])
        .constant("DonutChartD3Config",
                {
                    "chartcolor": {
                        "paletteColors": ["#676767", "#96B1D0", "#0B2477", "#FF9900", "#81017D", "#B6BF00", "#DA0081", "#00C7B2"], //default color options for rendering chart
                        "zeroTicketColor": ["#d3d3d3"],
                        "centerTicketCountHoverColor":["#0574AC"]
                    },
                    "zoomlevel": "25",
                    "defaultcenterlabel": "Total",
                    "donutratio": "1.5",
                    "initduration": "1000",
                    "animduration": "300",
                    "legendreqd": "false",
                    "legendposition": "right",
                    "centerTextLabelDy": "20",
                    "clipMargin": "400",
                    "arcOverMargin":10
                })
        .directive('attDonutd3Chart', ['DonutChartD3Config', '$timeout','$extendObj', function (DonutChartD3Config, $timeout,$extendObj) {
                return {
                    restrict: 'A',
                    scope: {
                        chartData: '=',
                        chartColor: '=',
                        chartConfig: '=',
                        refreshChart: '=',
                        onclickcallback: '&',
                        legendclickcallback: '&',
                        centerclickcallback: '&'
                    },
                    templateUrl: 'template/donutD3/attDonutd3Chart.html',
                    transclude: true,
                    replace: true,
                    controller: ['$scope', '$attrs', function ($scope, $attrs) {
                            if (angular.isDefined($scope.chartConfig)) {
                                DonutChartD3Config = $extendObj.extendDeep(DonutChartD3Config,$scope.chartConfig);
                            }
                            var legendColors = [];
                            legendColors = ($scope.chartColor) ? $scope.chartColor : DonutChartD3Config.chartcolor.paletteColors;
                            $scope.addLegendColor = function (id, item) {
                                if (item.value > 0) {
                                    return {"color": legendColors[id]};
                                } else {
                                    return {"color": DonutChartD3Config.chartcolor.zeroTicketColor};
                                }
                            };
                            $scope.chartID = $attrs.id;
                            if (!angular.isDefined($attrs.legendRequired)) {
                                $scope.legendRequired = DonutChartD3Config.legendreqd;
                            } else {
                                $scope.legendRequired = $attrs.legendRequired;
                            }
                            if (!angular.isDefined($attrs.legendPosition)) {
                                $scope.legendPosition = DonutChartD3Config.legendposition;
                            } else {
                                $scope.legendPosition = $attrs.legendPosition;
                            }
                            $scope.addStyle = function (elem) {
                                if ($scope.legendRequired && angular.isDefined($scope.legendPosition)) {
                                    if ($attrs.legendPosition === "right") {
                                        return "floatLeft";
                                    } else {
                                        if (elem === 'chartLegend')
                                        {
                                            return "floatLeft";
                                        }
                                        else
                                        {
                                            return "floatRight";
                                        }
                                    }
                                } else {
                                    return "floatLeft";
                                }
                            };
                        }],
                    link: function (scope, element, attrs) {
                        scope.$watch('refreshChart', function () {
                            if (angular.isDefined(scope.refreshChart)) {
                                if (scope.refreshChart !== true)
                                {
                                    return;
                                } else {
                                    for (var j = 0; j < scope.chartData.length; j++) {
                                        var chartObj =scope.chartData[j];
                                        var className = (chartObj.name).replace(/ /g, "").toUpperCase();
                                        angular.element(document.querySelector("[chart-legend-id=" + className + "]")).removeClass("active");
                                    }
                                    
                                }

                            }
                            
                            if (angular.isDefined(scope.chartConfig)) {
                                DonutChartD3Config = $extendObj.extendDeep(DonutChartD3Config, scope.chartConfig);
                            }
                            d3.select("svg#" + attrs.id).remove();
                            d3.selectAll("svg#" + attrs.id + " > *").remove();
                            var selectedPath = "",
                                    lastSelected,
                                    pi = Math.PI;
                            var zoom = parseInt(DonutChartD3Config.zoomlevel, 0),
                                    coloroptions = (scope.chartColor) ? scope.chartColor : DonutChartD3Config.chartcolor.paletteColors,
                                    defaultcenterlabel = DonutChartD3Config.defaultcenterlabel,
                                    totalcount = 0,
                                    margin = {// optionally set margins
                                        top: zoom,
                                        right: zoom,
                                        bottom: zoom,
                                        left: zoom
                                    },
                            // set height and width plus margin so zoomed area is not clipped
                           /* width = parseInt(attrs.chartWidth, 0) - (margin.left + margin.right),
                                    height = parseInt(attrs.chartHeight, 0) - (margin.top + margin.bottom),
                                    radius = Math.min(
                                            width - (margin.left + margin.right),
                                            height - (margin.top + margin.bottom)) / 2,*/
                             width = attrs.chartWidth,
                                    height = attrs.chartHeight,
                                    radius = Math.min(
                                            width - (margin.left + margin.right),
                                            height - (margin.top + margin.bottom)) / 2, 
                                    color = d3.scale.ordinal().range(coloroptions);
                            if (angular.isDefined(scope.chartData)) {
                                for (var i in scope.chartData) {
                                    if (angular.isDefined(scope.chartData[i].value)) {
                                        totalcount = (totalcount + parseInt(scope.chartData[i].value, 10));
                                    }
                                }
                            }
                            
                            var data=[];
                            for (var z = 0; z < scope.chartData.length; z++) {
                                var chartElem =scope.chartData[z];
                                if(chartElem.value <= 0){
                                    data.push({index: z, value: chartElem.value});
                                }      
                            }
                            var chartStyle = d3.select(".chartContainer#" + attrs.id)
                                    .attr("style");
                            var chart = d3.select(".chartContainer#" + attrs.id)
                                    .attr("style", "height: " + attrs.chartHeight + "px;width:" + attrs.chartWidth + "px;")
                                    .append("svg")
                                    .attr("id", attrs.id)
                                    .attr("width", '100%')
                                    .attr("height", '100%')
                                    .attr('viewBox', '0 0 ' + Math.min(width, height) + ' ' + Math.min(width, height))
                                    .attr('preserveAspectRatio', 'xMinYMin')
                                    .append("g")
                                    .attr("transform", "translate(" + Math.min(width, height) / 2 + "," + Math.min(width, height) / 2 + ")");
                            // set radius
                            var arc = d3.svg.arc()
                                    .innerRadius(radius / DonutChartD3Config.donutratio)
                                    .outerRadius(radius);
                            // add center text element
                            var centerTextValue = chart.append("text")
                                    .attr("class", "donutcenterValue")
                                    .attr("id", attrs.id + "count")
                                    .on("click", function (d) {
                                        var value = d3.select("g > text#" + attrs.id + "count").text();
                                        var label = d3.select("g > text#" + attrs.id + "category").text();
                                        if (angular.isDefined(scope.onclickcallback)) {
                                            scope.centerclickcallback({count: value, category: label});
                                        }
                                    });
                            var centerTextLabel = chart.append("text")
                                    .attr("y", DonutChartD3Config.centerTextLabelDy)
                                    .attr("class", "donutcenterLabel")
                                    .attr("id", attrs.id + "category");

                            centerTextValue.text(totalcount);
                            centerTextLabel.text(defaultcenterlabel);
                            if (totalcount <= 0) {
                                arc.startAngle(0).endAngle(2 * pi); //just radians
                                chart.append("path")
                                        .attr("d", arc)
                                        .attr("fill", DonutChartD3Config.chartcolor.zeroTicketColor);
                            } else {
                                // set hovered radius
                                var arcOver = d3.svg.arc()
                                        .innerRadius(radius / DonutChartD3Config.donutratio)
                                        .outerRadius(radius + DonutChartD3Config.arcOverMargin);
                                var pie = d3.layout.pie()
                                        .sort(null)
                                        .startAngle(1.1 * Math.PI)
                                        .endAngle(3.1 * Math.PI)
                                        .value(function (d) {
                                            return d.value;
                                        });
                                var g = chart.selectAll(".arc")
                                        .data(pie(scope.chartData))
                                        .enter()
                                        .append("g")
                                        .attr("class", "arc")
                                        .style("cursor", "pointer");
                                // set fill color and animate tween
                                g.append("path")
                                        .style("fill", function (d) {
                                            return color(d.data.name);
                                        })
                                        .attr("path-id",function (d, i) {
                                            return "b-" + i;
                                        })
                                        .style("stroke", "white")
                                        .style("stroke-width", 1)
                                        .on("mouseover", function (d) {
                                            d3.select(this).transition()
                                                    .duration(DonutChartD3Config.animduration)
                                                    .attr("d", arcOver);
                                            
                                            centerTextValue.text(d3.select(this).datum().data.value);
                                            centerTextLabel.text(d3.select(this).datum().data.name);
                                            var className = d3.select(this).datum().data.name.replace(/ /g, "").toUpperCase();
                                            angular.element(document.querySelector("[chart-legend-id=" + className + "]")).addClass("active");
                                        })
                                        .on("mouseout", function (d) {
                                            if (!this.getAttribute("clicked")) {
                                                d3.select(this).transition()
                                                        .duration(DonutChartD3Config.animduration)
                                                        .attr("d", arc);
                                                if (selectedPath.length === 0) {
                                                    centerTextValue.text(totalcount);
                                                    centerTextLabel.text(defaultcenterlabel);
                                                } else {
                                                    centerTextValue.text(selectedPath.datum().data.value);
                                                    centerTextLabel.text(selectedPath.datum().data.name);
                                                }
                                                var className = d3.select(this).datum().data.name.replace(/ /g, "").toUpperCase();
                                                angular.element(document.querySelector("[chart-legend-id=" + className + "]")).removeClass("active");
                                            } else if (angular.isDefined(lastSelected) && lastSelected!=="") {
                                                lastSelected.transition()
                                                        .duration(DonutChartD3Config.animduration)
                                                        .attr("d", arc);
                                                if (angular.isDefined(lastSelected)) {
                                                    var prevclassName = lastSelected.datum().data.name.replace(/ /g, "").toUpperCase();
                                                    angular.element(document.querySelector("[chart-legend-id=" + prevclassName + "]")).removeClass("active");
                                                }
                                            }
                                        })
                                        .on("click", function (d, i) {
                                            if (angular.isDefined(scope.onclickcallback)) {
                                                scope.onclickcallback({name: d3.select(this).datum().data.name, value: d3.select(this).datum().data.value});
                                            }
                                            if (!this.getAttribute("clicked")) {
                                                this.setAttribute("clicked", "clicked");
                                                if (selectedPath !== "") {
                                                    lastSelected = selectedPath;
                                                    selectedPath[0][0].removeAttribute("clicked");
                                                }
                                                selectedPath = d3.select(this);
                                            } else {
                                                if (this.getAttribute("path-id") === selectedPath.attr("path-id")) {
                                                    selectedPath[0][0].removeAttribute("clicked");
                                                    selectedPath = "";
                                                }
                                            }

                                        })
                                        .transition()
                                        .ease("exp")
                                        .duration(DonutChartD3Config.initduration)
                                        .attrTween("d", tweenPie);
                                // animate function
                                scope.legendMouseOver = function (item, id, e) {
                                        selectedId="b-"+id;
                                    if (item.value > 0) {
                                        scope.LegendClass = "";
                                        this.LegendClass = "active";
                                        centerTextValue.text(item.value);
                                        centerTextLabel.text(item.name);
                                        d3.selectAll(".chartContainer#"+attrs.id+" "+"path").each(function(d, i) {
                                             if(this.getAttribute("path-id")==selectedId){
                                                 d3.select(this).transition()
                                                .duration(DonutChartD3Config.animduration)
                                                .attr("d", arcOver);
                                             }
                                        });
                                        
                                        d3.select("#TicketGraphcount").style('fill',DonutChartD3Config.chartcolor.centerTicketCountHoverColor);  
                                    }
                                };
                                scope.legendMouseLeave = function (item, id, e) {
                                    selectedId="b-"+id;
                                    hoveredLegendItem=this;
                                    if (item.value > 0) {
                                        if (selectedPath.length === 0) {
                                            centerTextValue.text(totalcount);
                                            centerTextLabel.text(defaultcenterlabel);
                                        } else {
                                            centerTextValue.text(selectedPath.datum().data.value);
                                            centerTextLabel.text(selectedPath.datum().data.name);
                                        }
                                        d3.selectAll(".chartContainer#"+attrs.id+" "+"path").each(function(d, i) {
                                             if(this.getAttribute("path-id")==selectedId){
                                                if(this.getAttribute("clicked")===null){
                                                    hoveredLegendItem.LegendClass = "";
                                                    d3.select(this).transition().duration(DonutChartD3Config.animduration).attr("d", arc); 
                                                }
                                             }
                                        });
                                        d3.select("#TicketGraphcount").style('fill', "#000");

                                    }
                                };
                            }
                            function tweenPie(b) {
                                var i = d3.interpolate({
                                    startAngle: 1.1 * Math.PI,
                                    endAngle: 1.1 * Math.PI
                                }, b);
                                return function (t) {
                                    return arc(i(t));
                                };
                            }
                            $timeout(function () {
                                var h = element.find("ul")[0].clientHeight,
                                        fHeight = (parseInt(height, 10) - h),
                                        className = scope.addStyle('chartContainer');
                                fHeight = (fHeight < 0) ? 0 : fHeight;     
                                angular.element(element[0].querySelector('.chartContainer')).addClass(className);
                                angular.element(element[0].querySelector(".chartLegend")).css({
                                    "margin-top": (fHeight / 2) + "px"
                                });
                            }, 200);
                            for (var k = 0; k < data.length; k++) {
                                var index =data[k].index;
                                var zeroValIndex="b-"+index;
                                if(data[k].value <= 0){
                                 removeStrokelines(zeroValIndex);
                                }
                                       
                            }
                            
                            function removeStrokelines(zeroValIndex){
                                d3.selectAll(".chartContainer#"+attrs.id+" "+"path").each(function(d, i) {
                                             if(this.getAttribute("path-id")==zeroValIndex){
                                                d3.select(this).style('stroke', "").style("stroke-width", "");
                                             }
                                  });
                            }
                            scope.refreshChart = false;
                        });

                    }
                };
            }])
        .filter('filterData', function () {
            return function (input) {
                return input.replace(/ /g, "").toUpperCase();
            };
        });
angular.module('att.charts.donutFusion', [])
        .constant("DonutChartFusionConfig",
                {
                    "chart": {
                        "caption": "Tickets",
                        "startingAngle": 0,
                        "theme": "fist",
                        "captionPadding": "0",
                        "paletteColors": "#FF0066,#00CCFF,#00CC66,#6699FF,#000099,#660066,#FF6600,#4D4D4D", //default color options for rendering chart
                        "enableSmartLabels": "0",
                        "enableMultiSlicing": "1",
                        "donutRadius": "65",
                        "pieRadius": "110",
                        "showLabels": "0",
                        "showValues": "0",
                        "showBorder": "0",
                        "showpercentintooltip": "0",
                        "bgcolor": "#FFFFFF",
                        "defaultCenterLabel": "Total",
                        "showshadow": 0,
                        "centerLabel": "$value $label",
                        "plotHoverEffect": 1,
                        "plotBorderHoverDashGap": "50",
                        "enableRotation": "0",
                        "baseFont": "Arial",
                        "baseFontColor": "#666666",
                        "use3DLighting": "0",
                        "numbersuffix": "",
                        "defaultAnimation": "0",
                        "showValuesOnHover": 0
                    }
                })
        .directive('attDonutfusionChart', ['DonutChartFusionConfig', '$timeout', function (DonutChartFusionConfig, $timeout) {
                'use strict';
                return {
                    restrict: 'A',
                    scope: {
                        chartData: '=',
                        onclickcallback: '&'
                    },
                    templateUrl: 'template/donutFusion/attDonutfusionChart.html',
                    transclude: true,
                    replace: true,
                    link: function (scope, element, attrs, controller, transclude) {
                        var totalItems = 0;
                        if (angular.isDefined(scope.chartData)) {
                            for (var i in scope.chartData) {
                                if(angular.isDefined(scope.chartData[i].value)){
                                totalItems = (totalItems + parseInt(scope.chartData[i].value, 10));
                            }
                            }
                        }
                        DonutChartFusionConfig.data = scope.chartData;
                        var donutChartDataSource = DonutChartFusionConfig;
                        var defaultCenterLabelText;
                        defaultCenterLabelText = totalItems + ' ' + DonutChartFusionConfig.chart.defaultCenterLabel;
                        donutChartDataSource.chart.defaultCenterLabel = defaultCenterLabelText;

                        var initChart = function () {
                            FusionCharts.ready(function () {
                                var donutChart = new FusionCharts({
                                    type: 'doughnut2d',
                                    renderAt: "donutFusionChart",
                                    //width: '100%',
                                    width: attrs.chartWidth,
                                    height: attrs.chartHeight,
                                    dataFormat: 'json',
                                    dataSource: donutChartDataSource,
                                    events: {
                                        //dataPlotRollOver event is raised whenever you hover over a data plot (column, anchor of line or area, pie etc.)
                                        "dataPlotRollOver": function (evtObj, argumentsObject) {
                                            var selectedLabelArr = argumentsObject.toolText.split(",");
                                            var className = selectedLabelArr[0].replace(/ /g, "").toUpperCase();
                                            angular.element(document.querySelector("[chart-legend-id=" + className + "_fusion" + "]")).addClass("active");
                                        },
                                        "dataPlotRollOut": function (evtObj, argObj) {
                                            var selectedLabelArr = argObj.toolText.split(",");
                                            var className = selectedLabelArr[0].replace(/ /g, "").toUpperCase();
                                            angular.element(document.querySelector("[chart-legend-id=" + className + "_fusion" + "]")).removeClass("active");
                                        },
                                        "dataPlotClick": function (evtObj, argObj) {
                                            if(angular.isDefined(scope.onclickcallback)){
                                            scope.onclickcallback({evtObj:evtObj, argObj:argObj});
                                            }
                                        }
                                    }
                                });
                                donutChart.render();
                                DonutChartFusionConfig.chart.defaultCenterLabel = DonutChartFusionConfig.chart.defaultCenterLabel;
                            });
                        };
                        initChart();
                        $timeout(function () {
                            var w = element.find("ul")[0].clientHeight,
                                    fWidth = (parseInt(attrs.chartHeight, 10) - w);
                            element.find("ul").css("margin-top", (fWidth / 2) + "px");

                        }, 200);
                    },
                    controller: function ($scope, $element, $attrs) {
                        var legendColors = [];
                        legendColors = DonutChartFusionConfig.chart.paletteColors.split(",");
                        $scope.addLegendColor = function (id) {
                            return {"color": legendColors[id]};
                        };
                        $scope.legendRequired = $attrs.legendRequired;

                        $scope.addStyle = function (elem) {
                            if ($scope.legendRequired && angular.isDefined($attrs.legendPosition)) {
                                if ($attrs.legendPosition === "right") {
                                    if (elem === 'chartLegend')
                                    {
                                        return {"float": "left"};
                                    }
                                    else
                                    {
                                        return {"float": "left"};
                                    }
                                } else {
                                    if (elem === 'chartLegend')
                                    {
                                        return {"float": "left"};
                                    }
                                    else
                                    {
                                        return {"float": "right"};
                                    }
                                }
                            } else {
                                return {"float": "left"};
                            }
                        };
                    }
                };
            }])
        .filter('filterData', function () {
            return function (input) {
                return input.replace(/ /g, "").toUpperCase();
            };
        });
angular.module('att.charts.horseshoeD3', ['att.charts.utilities'])
        .constant("HorseShoeChartD3Config",
                {
                    "chartcolor": {
                        "paletteColors": ["#1072b8", "#FEFFF7"], //default color options for rendering chart baseColor and NeedleColor,
                        "paletteBorderColors": ["#1072b8", "#000"]
                    },
                    "defaultcenterlabel": "COS 1",
                    "defaultcentercategory": "Real Time",
                    "animduration": 500,
                    "legendreqd": "false",
                    "legendposition": "top",
                    "centerTextLabelDy": "20"
                })
        .directive('attHorseshoed3Chart', ['HorseShoeChartD3Config', '$timeout','$extendObj', function (HorseShoeChartD3Config, $timeout,$extendObj) {
                return {
                    restrict: 'A',
                    scope: {
                        chartConfig: '=',
                        initVal: '=',
                        horseShoeId: '@'
                    },
                    templateUrl: 'template/horseshoeD3/attHorseshoeD3Chart.html',
                    transclude: true,
                    replace: true,
                    controller: ['$scope', '$attrs',function ($scope, $attrs) {
                        if (angular.isDefined($scope.chartConfig)) {
                            HorseShoeChartD3Config = $extendObj.extendDeep(HorseShoeChartD3Config, $scope.chartConfig);
                        }
                        var legendColor = [];
                        legendColor = HorseShoeChartD3Config.chartcolor.paletteColors;
                        $scope.addLegendColor = function () {
                            return {"color": legendColor[0]};
                        };
                        if (!angular.isDefined($attrs.legendRequired)) {
                            $scope.legendRequired = HorseShoeChartD3Config.legendreqd;
                        } else {
                            $scope.legendRequired = $attrs.legendRequired;
                        }
                        if (!angular.isDefined($attrs.legendPosition)) {
                            $scope.legendPosition = HorseShoeChartD3Config.legendposition;
                        } else {
                            $scope.legendPosition = $attrs.legendPosition;
                        }
                    }],
                    link: function (scope, element, attrs) {
                        var radius = Math.min(attrs.chartWidth, attrs.chartHeight) / 2;
                        scope.LegendLabel = HorseShoeChartD3Config.defaultcenterlabel;
                        scope.LegendCategory = HorseShoeChartD3Config.defaultcentercategory;
                        var duration = HorseShoeChartD3Config.animduration,
                                guageVal = parseInt(scope.initVal.value, 10);
                        scope.guageVal = guageVal + "%";
                        var dataset = {
                            lower: calcPercent(0),
                            upper: calcPercent(guageVal)
                        },
                        pie = d3.layout.pie().sort(null),
                                format = d3.format(".0%");
                        var arc = d3.svg.arc()
                                .innerRadius(radius - 20)
                                .outerRadius(radius)
                                .startAngle(function (d) {
                                    return d.startAngle + 2.6 * Math.PI / 2;
                                })
                                .endAngle(function (d) {
                                    return d.endAngle + 2.6 * Math.PI / 2;
                                });

                        element[0].querySelector('.horseshoed3Container').setAttribute('id', scope.horseShoeId);

                        var svg = d3.select(".horseshoed3Container#" + scope.horseShoeId)
                                .attr("style", "height: " + attrs.chartHeight + "px;")
                                .append("svg")
                                .attr("id", scope.horseShoeId)
                                .attr("width", "100%")
                                .attr("height", "100%")
                                .attr('viewBox', '0 0 ' + Math.min(attrs.chartWidth, attrs.chartHeight) + ' ' + Math.min(attrs.chartWidth, attrs.chartHeight))
                                .attr('preserveAspectRatio', 'xMinYMin')
                                .append("g")
                                .attr("transform", "translate(" + Math.min(attrs.chartWidth, attrs.chartHeight) / 2 + "," + Math.min(attrs.chartWidth, attrs.chartHeight) / 2 + ")");
                        var path = svg.selectAll("path")
                                .data(pie(dataset.lower))
                                .enter().append("path")
                                .attr("class", function (d, i) {
                                    return "color" + i;
                                })
                                .attr("fill", HorseShoeChartD3Config.chartcolor.paletteColors[1])
                                .attr("stroke", HorseShoeChartD3Config.chartcolor.paletteBorderColors[1])
                                .attr("d", arc)
                                .each(function (d) {
                                    this._current = d;
                                }); // store the initial values
                        // add center text element
                        var centerTextValue = svg.append("text")
                                .attr("class", "horseshoecenterValue");
                        var centerTextLabel = svg.append("text")
                                .attr("y", HorseShoeChartD3Config.centerTextLabelDy)
                                .attr("class", "horseshoecenterLabel");
                        if (typeof (guageVal) === "string") {
                            centerTextValue.text(guageVal);
                            centerTextLabel.text(HorseShoeChartD3Config.defaultcenterlabel + "(" + HorseShoeChartD3Config.defaultcentercategory + ")");
                        }
                        else {
                            var progress = 0;
                            var timeout = setTimeout(function () {
                                clearTimeout(timeout);
                                path = path.data(pie(dataset.upper)); // update the data
                                path.transition().duration(duration).attrTween("d", function (a, index) {
                                    angular.element(this)
                                            .attr("fill", HorseShoeChartD3Config.chartcolor.paletteColors[index])
                                            .attr("stroke", HorseShoeChartD3Config.chartcolor.paletteBorderColors[index]);

                                    var i = d3.interpolate(this._current, a);
                                    var i2 = d3.interpolate(progress, guageVal);
                                    this._current = i(0);
                                    return function (t) {
                                        centerTextValue.text(format(i2(t) / 100));
                                        centerTextLabel.text(HorseShoeChartD3Config.defaultcenterlabel + "(" + HorseShoeChartD3Config.defaultcentercategory + ")");
                                        return arc(i(t));
                                    };
                                }); // redraw the arcs

                            }, 200);
                        }
                        scope.$watch('initVal', function (value) {
                            scope.guageVal = value.value + "%";
                            path.data(pie(calcPercent(value.value)));
                            path.transition().duration(duration).attrTween("d", function (a, index) {
                                angular.element(this)
                                        .attr("fill", HorseShoeChartD3Config.chartcolor.paletteColors[index])
                                        .attr("stroke", HorseShoeChartD3Config.chartcolor.paletteBorderColors[index]);

                                var i = d3.interpolate(this._current, a);
                                var i2 = d3.interpolate(progress, value.value);
                                this._current = i(0);
                                return function (t) {
                                    centerTextValue.text(format(i2(t) / 100));
                                    centerTextLabel.text(HorseShoeChartD3Config.defaultcenterlabel + "(" + HorseShoeChartD3Config.defaultcentercategory + ")");
                                    return arc(i(t));
                                };
                            }); // redraw the arcs

                        });
                        function calcPercent(guageVal) {
                            return [guageVal, 100 - guageVal];
                        }

                    }
                };
            }]);
angular.module('att.charts.radialguageD3', ['att.charts.utilities'])
        .constant("RadialGuageChartD3Config",
                {
                    "chartcolor": {
                        "MaxBandwidthReached": ["#efefef"],
                        "ZeroBandwidthSelected": ["#FEFFF7"],
                        "RemainingBandwidthColor": ["#FEFFF7"],
                        "RemainingBandwidthStrokeColor": ["#000000"],
                        "UsedBandwidthColor": ["#1072b8"],
                        "MouseOverArcFillColor": ["#0091d9"],
                        "RemainingBandwidthStrokeWidth":1,
                        "UsedBandwidthStrokeWidth":1,
                        "UsedBandwidthStrokeColor":["#efefef"]
                    },
                    "defaultcenterlabel": "Kbps",
                    "maxAllowedUnitsLabel": "Kbps",
                    "zoomLevel": "25",
                    "doughnutratio": 20,
                    "animduration": "300",
                    "guageStartAngle":4,
                    "guageEndAngle":8.56,
                    "legendreqd": "false",
                    "legendposition": "top",
                    "centerTextLabelDy": "20",
                    "centerTextValueDy": "0",
                    "centerTextValueDx": "0",
                    "centerTextLabelDx": "0",
                    "maxbandwidthTextDx": "65",
                    "maxbandwidthTextDy": "100",
                    "arcOverMargin": 5
                })
        .directive('attRadialguaged3Chart', ['RadialGuageChartD3Config', '$timeout','$extendObj', function(RadialGuageChartD3Config, $timeout,$extendObj) {
                return {
                    restrict: 'A',
                    scope: {
                        maxValue: '=',
                        chartData: '=',
                        chartConfig: '=',
                        resetChart: '=',
                        mouseOver: '=?'
                    },
                    controller: ['$scope', '$attrs', function($scope, $attrs) {
                        if (angular.isDefined($scope.chartConfig)) {
                            RadialGuageChartD3Config = $extendObj.extendDeep(RadialGuageChartD3Config, $scope.chartConfig);
                        }

                        }],
                    link: function(scope, element, attrs) {
                        var zoom = parseInt(RadialGuageChartD3Config.zoomLevel, 0),
                                margin = {// optionally set margins
                                    top: zoom,
                                    right: zoom,
                                    bottom: zoom,
                                    left: zoom
                                },
                        width = attrs.chartWidth,
                                height = attrs.chartHeight,
                                radius = Math.min(
                                        width - (margin.left + margin.right),
                                        height - (margin.top + margin.bottom)) / 2;
                        // set radius
                        var arc = d3.svg.arc()
                                .innerRadius(radius - RadialGuageChartD3Config.doughnutratio)
                                .outerRadius(radius);
                        // set hovered radius
                        var arcOver = d3.svg.arc()
                                .innerRadius(radius - RadialGuageChartD3Config.doughnutratio)
                                .outerRadius(radius + RadialGuageChartD3Config.arcOverMargin);
                        // Browser onresize event
                        window.onresize = function() {
                            scope.$apply();
                        };

                        scope.$watch('mouseOver', function(val) {
                            if (!angular.isDefined(scope.mouseOver)) { return; }
                            var id = parseInt(scope.mouseOver.split('-')[1], 10);
                            if (scope.mouseOver.split('-')[0] === 'true') {
                                d3.select('path#radialguagepath-' + (id)).transition()
                                    .duration(parseInt(RadialGuageChartD3Config.animduration,10))
                                    .attr('d', arcOver);
                                
                            } else {
                                d3.select('path#radialguagepath-' + (id)).transition()
                                    .duration(parseInt(RadialGuageChartD3Config.animduration,10))
                                    .attr('d', arc);
                            }
                        });

                        scope.$watch('resetChart', function() {
                            if (angular.isDefined(scope.resetChart)) {
                                if (angular.isDefined(scope.chartData)) {
                                    scope.render(scope.chartData, scope.chartData);
                                }
                                scope.resetChart = false;
                            }
                        });
                        scope.$watch('chartData', function(newValue, oldValue) {
                            return scope.render(newValue, oldValue);
                        }, true);
                        scope.render = function(newValue, oldValue) {
                            d3.select("svg#" + attrs.id).remove();
                            d3.selectAll("svg#" + attrs.id + " > *").remove();
                            if (!scope.chartData)
                            {
                                return;
                            }
                            var color, selectedSlider, totalBandwidth = 0, totalUsed = 0, remainingBandwidth = 0;
                            if (angular.isDefined(scope.maxValue)) {
                                totalBandwidth = parseInt(scope.maxValue, 0);
                            }
                            angular.forEach(newValue, function(val, key) {
                                totalUsed = (totalUsed + parseInt(val.value, 10));
                                if (newValue[key].value !== oldValue[key].value) {
                                    selectedSlider = key;
                                }
                            });
                            var data = angular.copy(scope.chartData);
                            if (totalUsed < totalBandwidth) {
                                remainingBandwidth = totalBandwidth - parseInt(totalUsed, 0);
                                data.push({name: "remaining", value: remainingBandwidth});
                            }
                            element[0].setAttribute('id', attrs.id);
                            var svg = d3.select(element[0])
                                    .attr("style", "height: " + attrs.chartHeight + "px;")
                                    .append("svg")
                                    .attr("id", attrs.id)
                                    .attr("width", "100%")
                                    .attr("height", "100%")
                                    .attr('viewBox', '0 0 ' + Math.min(width, height) + ' ' + Math.min(width, height))
                                    .attr('preserveAspectRatio', 'xMinYMin')
                                    .append("g")
                                    .attr("transform", "translate(" + Math.min(width, height) / 2 + "," + Math.min(width, height) / 2 + ")");

                            var centerTextValue = svg.append("text")
                                    .attr("y", RadialGuageChartD3Config.centerTextValueDy)
                                    .attr("x", RadialGuageChartD3Config.centerTextValueDx)
                                    .attr("class", "radialguagecenterValue");
                            var centerTextLabel = svg.append("text")
                                    .attr("y", RadialGuageChartD3Config.centerTextLabelDy)
                                    .attr("x", RadialGuageChartD3Config.centerTextLabelDx)
                                    .attr("class", "radialguagecenterLabel");
                            var maxbandwidthText = svg.append("text")
                                    .attr("y", RadialGuageChartD3Config.maxbandwidthTextDy)
                                    .attr("x", RadialGuageChartD3Config.maxbandwidthTextDx)
                                    .attr("class", "radialmaxbandwidthText");
                            if (totalUsed > totalBandwidth) {
                                totalUsed = totalBandwidth;
                                data = [
                                    {
                                        name: "totalUsed",
                                        value: "100"
                                    }
                                ];
                                color = d3.scale.ordinal().range(RadialGuageChartD3Config.chartcolor.MaxBandwidthReached);
                            } else if (totalUsed <= 0) {
                                data = [
                                    {
                                        name: "totalUnUsed",
                                        value: "100"
                                    }
                                ];
                                color = d3.scale.ordinal().range(RadialGuageChartD3Config.chartcolor.ZeroBandwidthSelected);

                            } else {
                                var Colorpalette = [];
                                if (newValue === oldValue) {
                                    data = [
                                        {
                                            name: "totalUsed",
                                            value: totalUsed
                                        }
                                    ];
                                    if (remainingBandwidth > 0) {
                                        data.push({name: "remaining", value: remainingBandwidth});
                                    }
                                }
                                for (var j = 0; j < data.length - 1; j++) {
                                    Colorpalette[j] = RadialGuageChartD3Config.chartcolor.UsedBandwidthColor;
                                }
                                if (remainingBandwidth > 0) {
                                    Colorpalette.push(RadialGuageChartD3Config.chartcolor.RemainingBandwidthColor);
                                }
                                color = d3.scale.ordinal().range(Colorpalette);
                            }

                            centerTextValue.text(totalUsed);
                            centerTextLabel.text(RadialGuageChartD3Config.defaultcenterlabel);
                            maxbandwidthText.text(totalBandwidth + " " + RadialGuageChartD3Config.maxAllowedUnitsLabel);
                            var pie = d3.layout.pie()
                                    .sort(null)
                                    .startAngle(RadialGuageChartD3Config.guageStartAngle)
                                    .endAngle(RadialGuageChartD3Config.guageEndAngle)
                                    .value(function(d) {
                                        return d.value;
                                    });

                            var g = svg.selectAll(".arc")
                                    .data(pie(data))
                                    .enter()
                                    .append("g")
                                    .attr("class", "arc")
                                    .attr("id", function(d, i) {
                                        return "radialguagearc-" + i;
                                    })
                                    .style("cursor", "pointer");

                            if (newValue === oldValue) {
                                g.append("path")
                                        .style("fill", function(d) {
                                            return color(d.data.name);
                                        })
                                        .attr("id", function(d, i) {
                                            return "radialguagepath-" + i;
                                        })
                                        .transition()
                                        .ease("exp")
                                        .duration(0)
                                        .attrTween("d", tweenPie);
                            } else {
                                g.append("path")
                                        .style("fill", function(d) {
                                            return color(d.data.name);
                                        })
                                        .attr("id", function(d, i) {
                                            return "radialguagepath-" + i;
                                        })
                                        .style("stroke", RadialGuageChartD3Config.chartcolor.RemainingBandwidthColor)
                                        .style("stroke-width", RadialGuageChartD3Config.chartcolor.UsedBandwidthStrokeWidth)
                                        .transition()
                                        .ease("exp")
                                        .duration(0)
                                        .attrTween("d", tweenPie);
                            }
                            if (totalUsed === totalBandwidth || remainingBandwidth > 0) {
                                var count = data.length - 1;
                                d3.select("#" + attrs.id + " path#radialguagepath-" + count).style('stroke', RadialGuageChartD3Config.chartcolor.RemainingBandwidthStrokeColor).style("stroke-width", RadialGuageChartD3Config.chartcolor.RemainingBandwidthStrokeWidth);
                            }
                            if (angular.isDefined(selectedSlider) && remainingBandwidth > 0) {

                                d3.select("#" + attrs.id + " path#radialguagepath-" + selectedSlider).transition()
                                        .duration(RadialGuageChartD3Config.animduration)
                                        .attr("d", arcOver);
                                d3.select("#" + attrs.id + " path#radialguagepath-" + selectedSlider).style('fill', RadialGuageChartD3Config.chartcolor.MouseOverArcFillColor);
                            }
                            function tweenPie(b) {
                                var i = d3.interpolate({
                                    startAngle: 2.1 * Math.PI,
                                    endAngle: 2.1 * Math.PI
                                }, b);
                                return function(t) {
                                    return arc(i(t));
                                };
                            }

                        };


                    }
                };
            }]);
angular.module('att.charts.stackedBarchart', ['att.charts.utilities'])
        .constant("stackBarChartConfig",
                {
                    "chartcolor": ["#B2B2B2", "#00CC00"],
                    "gridLineColor": "#CCCCCC",
                    "yAxisMaxTicks": 4,
                    "chartHeight": "200",
                    "chartOpacity": 0.3,
                    "amountKDivision": 1000,
                    "amountKText": "K",
                    "amountMDivision": 1000000,
                    "amountMText": "M",
                    "yearLabelPos": {"x": 10, "y": 15},
                    "tooltipTopMargin": 110,
                    "tooltipLeftMargin": 54,
                    "margin": {
                        top: 20,
                        right: 80,
                        bottom: 30,
                        left: 52
                    }
                })
        .directive('stackedBarChart', ['stackBarChartConfig', '$extendObj', '$timeout', function (stackBarChartConfig, $extendObj, $timeout) {
                return {
                    restrict: 'A',
                    scope: {
                        chartData: '=',
                        legendRequired: "=",
                        refreshChart: "=",
                        chartConfig: "="
                    },
                    templateUrl: "template/stackedBarchart/stackedBarchart.html",
                    replace: true,
                    controller: ['$scope', '$attrs', function ($scope, $attrs) {
                            if (angular.isDefined($scope.chartConfig)) {
                                stackBarChartConfig = $extendObj.extendDeep(stackBarChartConfig, $scope.chartConfig);
                            }
                            $scope.chartID = $attrs.id;
                            $scope.legendColors = stackBarChartConfig.chartcolor;
                            $scope.addLegendColor = function (id) {
                                var bgColor = null;
                                bgColor = {"background-color": $scope.legendColors[id]};
                                return bgColor;
                            };
                        }],
                    link: function (scope, element, attrs, ctrl) {
                        scope.tooltipFlag = false;
                        var dataObj, idx = 0, xAxisTicks, margin = stackBarChartConfig.margin,
                                width = parseInt(attrs.chartWidth, 10),
                                height = parseInt(stackBarChartConfig.chartHeight, 10),
                                padding = 20,
                                parseDate = d3.time.format("%d-%b-%Y").parse,
                                tooltipFormat = d3.time.format("%B-%Y"),
                                labelFormat = d3.time.format("%Y");
                        attrs.$observe('legendRequired', function (val) {
                            if (val === 'true') {
                                scope.showLegend = true;
                            }
                            else {
                                scope.showLegend = false;
                            }
                        });
                        scope.$watch('refreshChart', function (value) {
                            if (value === false) {
                                return;
                            }

                            if (angular.isDefined(scope.chartConfig)) {
                                stackBarChartConfig = $extendObj.extendDeep(stackBarChartConfig, scope.chartConfig);
                            }

                            d3.select("svg#" + attrs.id).remove();
                            d3.selectAll("svg#" + attrs.id + " > *").remove();
                            d3.selectAll("div#stackBarChartContainer" + " > div").remove();
                            if (scope.chartData[0].values.length === 0) {
                                scope.refreshChart = false;
                                return;
                            }

                            var xMonth = [], yDomainData = [], yAxisData = [], yValue = [], tempData = [], tooltipData = {}, yearArr = {}, isSingleMonth = false;

                            //X-Axis Range        
                            var xRange = d3.time.scale().range([padding, width - padding * 2]);

                            //Y-Axis Range 
                            var yRange = d3.scale.linear().range([height - padding, padding]);

                            dataObj = scope.chartData;

                            dataObj.forEach(function (data) {
                                var obj = data.values;
                                xAxisTicks = obj.length;
                                if (obj.length == 1) {
                                    isSingleMonth = true;
                                }
                                obj.forEach(function (d) {
                                    var tmp = "01-" + d.month;
                                    d.numericMonth = parseDate(tmp);
                                    yValue.push({'value': +d.value});
                                    d.value = +d.value;
                                    xMonth.push({'numericMonth': d.numericMonth});
                                });
                            });

                            //ToolTip Data
                            for (var z = 0; z < dataObj.length; z++) {
                                var tempSeries = dataObj[z].series;
                                for (var j = 0; j < dataObj[z].values.length; j++) {
                                    var months = dataObj[z].values[j].month;
                                    var tempVal = dataObj[z].values[j].value;
                                    var percent = dataObj[z].values[j].percent;
                                    tempData.push({"month": months, "series": tempSeries, "value": tempVal, "percent": percent});
                                }
                            }

                            for (var k = 0; k < tempData.length; k++) {
                                var seriesName = tempData[k].series;
                                var seriesVal = tempData[k].value;
                                var seriesPer = tempData[k].percent;
                                var tempObj = tooltipData[tempData[k].month];
                                if (!(tempObj)) {
                                    tempObj = [];
                                    tooltipData[tempData[k].month] = tempObj;
                                    tooltipData[tempData[k].month]['seriesPer'] = seriesPer;
                                }
                                tempObj.push({seriesName: seriesName, seriesVal: seriesVal});
                            }

                            //X-Axis Domain  
                            xRange.domain(d3.extent(xMonth, function (d) {
                                return d.numericMonth;
                            }));

                            for (var b = 0; b < tempData.length; b++) {
                                var val = Math.round(parseInt(tempData[b].value, 10) / stackBarChartConfig.yAxisMaxTicks);
                                var Calc = Math.ceil((val / Math.pow(10, ("" + val).length - 1))) * (stackBarChartConfig.yAxisMaxTicks) * Math.pow(10, ("" + val).length - 1);
                                yDomainData.push({'yAxisVal': Calc});
                            }

                            //Y-Axis Domain
                            yRange.domain([0, d3.max(yDomainData, function (d) {
                                    return (d.yAxisVal);
                                })]);

                            var yTick = d3.max(yDomainData, function (d) {
                                return d.yAxisVal;
                            });

                            yTick = yTick / stackBarChartConfig.yAxisMaxTicks;
                            for (var x = 0; x <= stackBarChartConfig.yAxisMaxTicks; x++) {
                                yAxisData.push(yTick * x);
                            }

                            var formatMoney = function (d) {
                                if (d >= stackBarChartConfig.amountMDivision) {
                                    return d / stackBarChartConfig.amountMDivision + stackBarChartConfig.amountMText;
                                } else if (d >= stackBarChartConfig.amountKDivision) {
                                    return d / stackBarChartConfig.amountKDivision + stackBarChartConfig.amountKText;
                                } else {
                                    return d;
                                }
                            };
                            var xAxis = d3.svg.axis().scale(xRange).orient("bottom").tickFormat(d3.time.format('%b')).ticks(d3.time.months);
                            var xAxisGrid = d3.svg.axis().scale(xRange).orient("top").ticks(d3.time.months).tickFormat('').tickSize(-height + 2 * padding, 0);

                            var yAxis = d3.svg.axis().scale(yRange).orient("left").tickValues(yAxisData).ticks(stackBarChartConfig.yAxisMaxTicks).tickFormat(formatMoney);
                            var yAxisGrid = d3.svg.axis().scale(yRange).orient("left").tickValues(yAxisData).ticks(stackBarChartConfig.yAxisMaxTicks).tickSize(-width, 0, 0);


                            //Calculate values for Stack
                            var stack = d3.layout.stack()
                                    .values(function (d) {
                                        return d.values;
                                    })
                                    .x(function (d) {
                                        return d.numericMonth;
                                    })
                                    .y(function (d) {
                                        return d.value;
                                    });

                            //Plot Chart
                            var drawBarChart = d3.select("#stackBarChartContainer")
                                    .append("svg")
                                    .attr("id", attrs.id)
                                    .data(dataObj)
                                    .attr("width", width + margin.left + margin.right)
                                    .attr("height", parseInt(height, 10) + parseInt(margin.top, 10) + parseInt(margin.bottom, 10))
                                    .append("g")
                                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

                            //Draw X Axis        
                            drawBarChart.append("g")
                                    .attr("class", "x axis")
                                    .attr("transform", "translate(0," + (height - padding) + ")")
                                    .call(xAxis);

                            if (isSingleMonth) {
                                drawBarChart.select(".x").select(".tick").attr("transform", "translate(" + (isSingleMonth === true) * (width / 2 + 20) + ",0)");
                            }

                            //Draw Y Grid Lines
                            var yGrid = drawBarChart.append("g")
                                    .attr("class", "grid").attr("id", "stackBarYGrid")
                                    .attr("transform", "translate(0,0)")
                                    .call(yAxisGrid);
                            yGrid.selectAll("line").style("stroke", stackBarChartConfig.gridLineColor);
                            yGrid.selectAll("text").remove();

                            //Plot Bars for multiple Stacks
                            var barGroup = drawBarChart.selectAll(".group")
                                    .data(stack(dataObj))
                                    .enter().append("g")
                                    .attr("class", "group").attr("id", function (d, i) {
                                return ("stackBar" + i);
                            })
                                    .attr("transform", "translate(" + (isSingleMonth === true) * width / 2 + ",0)");

                            barGroup.append("title").text(function (d) {
                                return d.series;
                            });

                            var bars = barGroup.selectAll("rect")
                                    .data(function (d) {
                                        return d.values;
                                    })
                                    .enter().append("rect")
                                    .attr("class", "bar")
                                    .attr("x", function (d) {
                                        return (xRange(d.numericMonth) - 10);
                                    })
                                    .attr("y", function (d) {
                                        return yRange(d.value);
                                    })
                                    .attr("width", "20")
                                    .attr("height", function (d) {
                                        return ((height - yRange(d.value)) - padding);
                                    }).on("mouseover", function (d) {
                                var offsetX = d3.select(this).attr("x");
                                var offsetY = d3.select(this).attr("y");
                                show_tooltip_grid_line(offsetX, "x");
                                drawBarChart.selectAll(".bar").style("fill-opacity", stackBarChartConfig.chartOpacity);
                                d3.select(this).style("fill-opacity", "1");
                                mouseOver(d, offsetX, offsetY);
                            }).on("mouseout", function () {
                                hide_tooltip_grid_line(d3.select(this).attr("x"), "x");
                                scope.tooltipFlag = false;
                                drawBarChart.selectAll(".bar").style("fill-opacity", "1");
                                scope.$apply();
                            });

                            bars.append('desc').append('title').text(function (d) {
                                var nvdaObj = tooltipData[d.month];
                                return (addTitle(nvdaObj, d));
                            });

                            //Fill color in Bars
                            for (var a = 0; a < dataObj.length; a++) {
                                var rectBars = drawBarChart.select('#stackBar' + a).selectAll('rect');
                                rectBars.attr('fill', scope.legendColors[a]).attr("fill-opacity", "1");
                            }

                            // Draw the x Grid lines
                            var xGrid = drawBarChart.append("g")
                                    .attr("class", "grid").attr("id", "stackBarXGrid")
                                    .attr("transform", "translate(" + (isSingleMonth === true) * width / 2 + ",0)")
                                    .call(xAxisGrid);

                            xGrid.selectAll("line").attr("transform", "translate(" + -Math.ceil((xRange(2) - xRange(1)) / 2) + "," + padding + ")")
                                    .attr("id", function (d, i) {
                                        return ("xGridLine" + i);
                                    });

                            //Draw dual Y axis on change of Year
                            var yAxisObj = dataObj[0].values, yAxisIdx = 0;
                            for (var i = 0; i < yAxisObj.length; i++) {
                                var tmpObj = [];

                                tmpObj.push({"year": labelFormat(yAxisObj[i].numericMonth), "transform": xRange(yAxisObj[i].numericMonth)});
                                if (!yearArr[labelFormat(yAxisObj[i].numericMonth)]) {
                                    yearArr[labelFormat(yAxisObj[i].numericMonth)] = tmpObj;
                                }
                            }
                            for (var key in yearArr) {
                                var obj = yearArr[key];
                                var yAxisNodes = drawBarChart.append("g")
                                        .attr("class", "y axis").attr("id", yAxisId(yAxisIdx))
                                        .attr("transform", "translate(" + (obj[0].transform - padding) + ",0)")
                                        .call(yAxis);

                                yAxisNodes.selectAll("text").attr('class', 'ticktext');

                                //Append Year Label 
                                yAxisNodes.append("text")
                                        .attr("transform", "rotate(0)")
                                        .attr("y", stackBarChartConfig.yearLabelPos.y)
                                        .attr("x", stackBarChartConfig.yearLabelPos.x)
                                        .text(obj[0].year).attr("class", "yearLabel");
                                yAxisIdx++;
                            }

                            function yAxisId(yAxisIdx) {
                                return ("yAxis" + yAxisIdx);
                            }

                            //function to select first element
                            d3.selection.prototype.first = function () {
                                return d3.select(this[0][0]);
                            };

                            //Remove minimum value label form Y Axis
                            var tickLabels = d3.select("#yAxis0").selectAll('.ticktext');
                            //tickLabels.first().remove();
                            d3.select("#yAxis1").selectAll('.ticktext').remove();

                            //Add title for NVDA screen reader
                            function addTitle(nvdaObj, d) {
                                var temp = "";
                                for (var y = 0; y < nvdaObj.length; y++) {
                                    temp = temp + nvdaObj[y].seriesName + nvdaObj[y].seriesVal;
                                }
                                return (tooltipFormat(d.numericMonth) + "--" + temp + "Retainibility Percentage" + nvdaObj.seriesPer);
                            }

                            //MouseOver Event
                            function mouseOver(d, offsetX, offsetY) {
                                for (var key in tooltipData) {
                                    if (key == d.month) {
                                        scope.stackDataPoint = tooltipData[key];
                                        break;
                                    }
                                }
                                scope.monthPoint = {"xData": tooltipFormat(d.numericMonth).replace('-', ' ')};
                                scope.$apply();
                                $timeout(function () {
                                    offsetY = offsetY - stackBarChartConfig.tooltipTopMargin;
                                    var tooltipEl = element.children().eq(2);
                                    var tooltipWidth = tooltipEl[0].offsetWidth;
                                    if (isSingleMonth) {
                                        offsetX = offsetX - (tooltipWidth / 2) + stackBarChartConfig.tooltipLeftMargin + (width / 2);
                                    } else {
                                        offsetX = offsetX - (tooltipWidth / 2) + stackBarChartConfig.tooltipLeftMargin;
                                    }
                                    scope.tooltipStyle = {"left": offsetX + "px", "top": offsetY + "px"};
                                    scope.tooltipFlag = true;
                                }, 0);
                            }

                            //Show Grid Line on Over
                            function show_tooltip_grid_line(offsetX, attr) {
                                var dataLength;
                                dataLength = scope.chartData[0].values.length;
                                for (var i = 0; i < dataLength; i++) {
                                    var bar = drawBarChart.selectAll(".bar");
                                    if (bar[0][i].getAttribute(attr) === offsetX) {
                                        drawBarChart.select("#xGridLine" + i).style("stroke", stackBarChartConfig.gridLineColor);
                                    }
                                }
                            }

                            //Hide Grid Line
                            function hide_tooltip_grid_line(offsetX, attr) {
                                var dataLength;
                                dataLength = scope.chartData[0].values.length;
                                for (var i = 0; i < dataLength; i++) {
                                    var bar = drawBarChart.selectAll(".bar");
                                    if (bar[0][i].getAttribute(attr) === offsetX) {
                                        drawBarChart.select("#xGridLine" + i).style("stroke", "transparent");
                                    }
                                }
                            }
                            scope.refreshChart = false;
                        });
                    }
                };
            }])
        .filter('filterInput', function () {
            return function (input) {
                return input.replace(/ +/g, "").toLowerCase();
            };
        });
angular.module('att.charts.stackedareachart', ['att.charts.utilities'])
        .constant("stackChartConfig",
                {
                    "chartcolor": ["#9966FF", "#E68A2E", "#4DDB4D"],
                    "gridLineColor": "#808080",
                    "lineCurveType": 'linear',
                    "yAxisMaxTicks": 4,
                    "chartHeight": "200",
                    "shapes": ['circle', 'rect', 'triangle'],
                    "shapeSize": [3, 5, 6],
                    "chartOpacity": 0.6,
                    "yearLabelPos": {"x": 8, "y": -5},
                    "tooltipTopMargin": 100,
                    "tooltipLeftMargin": 42,
                    "yAxisUnitRange": 500000,
                    "margin": {
                        top: 30,
                        right: 80,
                        bottom: 30,
                        left: 52
                    }
                })
        .directive('stackedAreaChart', ['stackChartConfig','$extendObj','$timeout', function (stackChartConfig,$extendObj,$timeout) {
                return {
                    restrict: 'A',
                    scope: {
                        chartData: '=',
                        legendRequired: "=",
                        refreshChart: "=",
                        chartConfig: "="
                    },
                    templateUrl: "template/stackedareachart/stackedAreaChart.html",
                    replace: true,
                    controller: ['$scope', '$attrs', function ($scope, $attrs) {
                            var extendDeep = function extendDeep(dst) {
                              angular.forEach(arguments, function(obj) {
                                if (obj !== dst) {
                                  angular.forEach(obj, function(value, key) {
                                    if (dst[key] && dst[key].constructor && dst[key].constructor === Object) {
                                      extendDeep(dst[key], value);
                                    } else {
                                      dst[key] = value;
                                    }     
                                  });   
                                }
                              });
                              return dst;
                            };
                            if (angular.isDefined($scope.chartConfig)) {
                                stackChartConfig = $extendObj.extendDeep(stackChartConfig, $scope.chartConfig);
                            }
                            $scope.chartID = $attrs.id;
                            $scope.legendColors = stackChartConfig.chartcolor;
                            $scope.legendTooltipColors = angular.copy(stackChartConfig.chartcolor).reverse();
                            $scope.addLegendColor = function (id) {
                                var bgColor = null;
                                switch (id) {
                                    case 0:
                                        bgColor = {"background-color": $scope.legendColors[id], "border-radius": "100%"};
                                        break;
                                    case 1:
                                        bgColor = {"background-color": $scope.legendColors[id]};
                                        break;
                                    case 2:
                                        bgColor = {"width": "0", "height": "0", "border-left": "8px solid transparent", "border-right": "8px solid transparent", "border-bottom-style": "solid", "border-bottom-width": "8px", "border-bottom-color": $scope.legendColors[id]};
                                        break;
                                }
                                return bgColor;
                            };
                            $scope.addToolTipLegendColor = function (id) {
                                var bgColor = null;
                                switch (id) {
                                    case 2:
                                        bgColor = {"background-color": $scope.legendTooltipColors[id], "border-radius": "100%"};
                                        break;
                                    case 1:
                                        bgColor = {"background-color": $scope.legendTooltipColors[id]};
                                        break;
                                    case 0:
                                        bgColor = {"width": "0", "height": "0", "border-left": "6px solid transparent", "border-right": "6px solid transparent", "border-bottom-style": "solid", "border-bottom-width": "12px", "border-bottom-color": $scope.legendTooltipColors[id]};
                                        break;
                                }
                                return bgColor;
                            };
                        }],
                    link: function (scope, element, attrs, ctrl) {
                        scope.tooltipFlag = false;
                        var dataObj, idx = 0, xAxisTicks, margin = stackChartConfig.margin,
                                width = parseInt(attrs.chartWidth, 10),
                                height = stackChartConfig.chartHeight,
                                parseDate = d3.time.format("%d-%b-%Y").parse,
                                tooltipFormat = d3.time.format("%B-%Y"),
                                labelFormat = d3.time.format("%Y");
                        attrs.$observe('legendRequired', function (val) {
                            if (val === 'true') {
                                scope.showLegend = true;
                            }
                            else {
                                scope.showLegend = false;
                            }
                        });
                        scope.$watch('refreshChart', function (value) {
                            if (value === false) {
                                return;
                            }
                            if (angular.isDefined(scope.chartConfig)) {
                                stackChartConfig = $extendObj.extendDeep(stackChartConfig, scope.chartConfig);
                            }
                            d3.select("svg#" + attrs.id).remove();
                            d3.selectAll("svg#" + attrs.id + " > *").remove();
                            d3.selectAll("div#stackChartContainer" + " > div").remove();
                            if (scope.chartData[0].values.length === 0) {
                                scope.refreshChart = false;
                                return;
                            }

                            var xMonth = [], yValue = [], tempData = [], tooltipData = {}, yearArr = {}, yearLabel = '', isSingleMonth = false, singleMonthName, currencyFormat = 'K';

                            //X-Axis Range        
                            var xRange = d3.time.scale()
                                    .range([0, width]);

                            //Y-Axis Range 
                            var yRange = d3.scale.linear()
                                    .range([height, 0]);

                            dataObj = scope.chartData;

                            //  On selecting same month in From and To dropdowns, User should be getting graph                            
                            if (dataObj[0].values.length === 1) {
                                isSingleMonth = true;
                                dataObj.forEach(function (data) {
                                    var tmp1 = "01-" + data.values[0].month;
                                    var tmp = parseDate(tmp1);
                                    singleMonthName = tmp.toString().split(" ")[1];
                                    tmp.setMonth(tmp.getMonth() + 1);
                                    var nextMonth = tmp.toString().split(" ")[1] + "-" + tmp.getFullYear();
                                    data.values.push({month: nextMonth, value: data.values[0].value});
                                });
                            }

                            dataObj.forEach(function (data) {
                                var obj = data.values;
                                xAxisTicks = obj.length;
                                obj.forEach(function (d) {
                                    var tmp = "01-" + d.month;
                                    d.numericMonth = parseDate(tmp);
                                    yValue.push({'value': +d.value});
                                    d.value = +d.value;
                                    xMonth.push({'numericMonth': d.numericMonth});
                                    if (d.value > stackChartConfig.yAxisUnitRange) {
                                        currencyFormat = 'M';
                                    }
                                });
                            });

                            //ToolTip Data
                            for (var z = 0; z < dataObj.length; z++) {
                                var tempSeries = dataObj[z].series;
                                for (var j = 0; j < dataObj[z].values.length; j++) {
                                    var months = dataObj[z].values[j].month;
                                    var tempVal = dataObj[z].values[j].value;
                                    tempData.push({"month": months, "series": tempSeries, "value": tempVal});
                                }
                            }
                            for (var k = 0; k < tempData.length; k++) {
                                var seriesName = tempData[k].series;
                                var seriesVal = tempData[k].value;
                                var tempObj = tooltipData[tempData[k].month];
                                if (!(tempObj)) {
                                    tempObj = [];
                                    tooltipData[tempData[k].month] = tempObj;
                                }
                                tempObj.push({seriesName: seriesName, seriesVal: seriesVal});
                            }
                            for (var d in tooltipData) {
                                var tooltipObj = tooltipData[d];
                                var temp = 0, monthTotalVal = 0;
                                for (var y = 0; y < tooltipObj.length; y++) {
                                    temp = parseFloat(tooltipObj[y].seriesVal);
                                    monthTotalVal = monthTotalVal + temp;
                                    tooltipObj['total'] = monthTotalVal;
                                }
                            }
                            var yDomainData = [],yAxisData = [],yTicksData = [];
                            for (var month in tooltipData) {
                                yDomainData.push({"total": tooltipData[month].total});
                            }
                           
                            //Format yAxis Labels
                            var formatCurrency = function (d) {
                                if (currencyFormat === 'M') {
                                    return "$" + d / 1000000 + "M";
                                } else {
                                    return "$" + d / 1000 + "K";
                                }
                            };
                            
                            //X-Axis Domain  
                            xRange.domain(d3.extent(xMonth, function (d) {
                                return d.numericMonth;
                            }));
                            
                            for (var b = 0; b < yDomainData.length; b++) {
                                var val = Math.round(parseInt(yDomainData[b].total, 10) / stackChartConfig.yAxisMaxTicks);
                                var Calc = Math.ceil((val / Math.pow(10, ("" + val).length - 1))) * (stackChartConfig.yAxisMaxTicks) * Math.pow(10, ("" + val).length - 1);
                                yAxisData.push({'yAxisVal': Calc});
                            }
 
                            //Y-Axis Domain
                            yRange.domain([0, d3.max(yAxisData, function (d) {
                                    return (d.yAxisVal);
                                })]);
 
                            var yTick = d3.max(yAxisData, function (d) {
                                return d.yAxisVal;
                            });
 
                            yTick = yTick / stackChartConfig.yAxisMaxTicks;
                            for (var c = 0; c <= stackChartConfig.yAxisMaxTicks; c++) {
                                yTicksData.push(yTick * c);
                            }
                            
                            var xAxis = d3.svg.axis().scale(xRange).orient("bottom").tickFormat(d3.time.format('%b')).ticks(d3.time.months);
                            var yAxis = d3.svg.axis().scale(yRange).orient("left").tickValues(yTicksData).ticks(stackChartConfig.yAxisMaxTicks).tickFormat(formatCurrency);
                            var yAxisGrid = d3.svg.axis().scale(yRange).orient("left").tickValues(yTicksData).ticks(stackChartConfig.yAxisMaxTicks).tickSize(-width, 0, 0);                            

                            //Calculate values for Stack
                            var stack = d3.layout.stack()
                                    .values(function (d) {
                                        return d.values;
                                    })
                                    .x(function (d) {
                                        return d.numericMonth;
                                    })
                                    .y(function (d) {
                                        return d.value;
                                    });

                            //Draw Area  
                            var area = d3.svg.area()
                                    .x(function (d) {
                                        return xRange(d.numericMonth);
                                    })
                                    .y0(function (d) {
                                        return yRange(d.y0);
                                    })
                                    .y1(function (d) {
                                        return yRange(d.y0 + d.y);
                                    }).interpolate(stackChartConfig.lineCurveType);

                            //Plot Chart
                            var drawChart = d3.select("#stackChartContainer")
                                    .append("svg")
                                    .attr("id", attrs.id)
                                    .data(dataObj)
                                    .attr("width", width + margin.left + margin.right)
                                    .attr("height", parseInt(height, 10) + parseInt(margin.top, 10) + parseInt(margin.bottom, 10))
                                    .append("g")
                                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

                            //Draw X Axis        
                            drawChart.append("g")
                                    .attr("class", "x axis")
                                    .attr("transform", "translate(0," + height + ")")
                                    .call(xAxis);

                            //Plot Area for multiple Stacks
                            drawChart.selectAll(".layer")
                                    .data(stack(dataObj))
                                    .enter().append("path")
                                    .attr("class", "layer")
                                    .attr("d", function (d) {
                                        return area(d.values);
                                    }).style("fill", function (d, i) {
                                return scope.legendColors[i];
                            }).style("opacity", stackChartConfig.chartOpacity).style("stroke", "#ddd").append("title")
                                    .text(function (d) {
                                        return d.series;
                                    });

                            //Draw dual Y axis on change of Year
                            var yAxisObj = dataObj[0].values, yAxisIdx = 0;
                            for (var i = 0; i < yAxisObj.length; i++) {
                                var tmpObj = [];
                                tmpObj.push({"year": labelFormat(yAxisObj[i].numericMonth), "transform": xRange(yAxisObj[i].numericMonth)});
                                if (!yearArr[labelFormat(yAxisObj[i].numericMonth)]) {
                                    yearArr[labelFormat(yAxisObj[i].numericMonth)] = tmpObj;
                                }
                            }
                            for (var key in yearArr) {
                                var obj = yearArr[key];
                                var yAxisNodes = drawChart.append("g")
                                        .attr("class", "y axis").attr("id", yAxisId(yAxisIdx))
                                        .attr("transform", "translate(" + obj[0].transform + ",0)")
                                        .call(yAxis);

                                yAxisNodes.selectAll("text").attr('class', 'ticktext');
                                //Append Year Label 
                                yAxisNodes.append("text")
                                        .attr("transform", "rotate(0)")
                                        .attr("y", stackChartConfig.yearLabelPos.y)
                                        .attr("x", stackChartConfig.yearLabelPos.x)
                                        .text(obj[0].year).attr("class", "yearLabel");
                                yAxisIdx++;
                            }

                            //Remove next year Y axis in case of single Month selection
                            if (isSingleMonth) {
                                d3.select("#yAxisId1").remove();
                            }

                            function yAxisId(yAxisIdx) {
                                return ("yAxisId" + yAxisIdx);
                            }

                            //function to select first element
                            d3.selection.prototype.first = function () {
                                return d3.select(this[0][0]);
                            };

                            //Remove minimum value label form Y Axis
                            var tickLabels = d3.select("#yAxisId0").selectAll('.ticktext');
                            tickLabels.first().remove();
                            d3.select("#yAxisId1").selectAll('.ticktext').remove();

                            if (isSingleMonth) {
                                drawChart.selectAll('.x.axis').selectAll('.tick').selectAll('text')[1][0].textContent = singleMonthName;
                            }

                            //Append Data Points         
                            for (var x = 0; x < dataObj.length; x++) {
                                drawChart.append('g').attr('id', "pathId" + x).attr("transform", "translate(" + (isSingleMonth === true) * width / 2 + ",0)");
                                if (idx >= stackChartConfig.shapes.length) {
                                    idx = 0;
                                }
                                var shape = stackChartConfig.shapes[idx], shapeToAppend;
                                if (shape == 'rect' || shape == 'triangle') {
                                    shapeToAppend = 'polygon';
                                }
                                else {
                                    shapeToAppend = 'circle';
                                }

                                var newDataObj;
                                if (isSingleMonth) {
                                    newDataObj = dataObj[x].values.slice(0, 1);
                                } else {
                                    newDataObj = dataObj[x].values;
                                }

                                var dataPoints = drawChart.select('#pathId' + x).selectAll(shapeToAppend)
                                        .data(newDataObj)
                                        .enter()
                                        .append(shapeToAppend)
                                        .attr('class', shape);
                                drawShapes(shape, dataPoints);
                                idx++;
                            }

                            //Fill color in dataPoints
                            for (var a = 0; a <= dataObj.length; a++) {
                                var Dots = drawChart.selectAll('.' + stackChartConfig.shapes[a]);
                                Dots.attr('fill', scope.legendColors[a]);
                            }

                            //function to draw multiple shapes for Data Points
                            function drawShapes(shape, dataPoints) {
                                switch (shape) {
                                    case 'circle':
                                        var circle = dataPoints.attr("cx", function (d) {
                                            return xRange(d.numericMonth);
                                        }).attr("cy", function (d) {
                                            return yRange(d.y + d.y0);
                                        }).attr("r", stackChartConfig.shapeSize[0]).on("mouseover", function (d) {
                                            show_tooltip_grid_line(d3.select(this).attr("cx"), "circle", "cx");
                                            var offsetX = Math.ceil(d3.select(this).attr("cx"));
                                            var offsetY = Math.round(d3.select(this).attr("cy"));
                                            d3.selectAll('svg#' + attrs.id + " " + "path").style("fill-opacity", stackChartConfig.chartOpacity);
                                            d3.selectAll('svg#' + attrs.id + " " + "polygon").style("fill-opacity", stackChartConfig.chartOpacity);
                                            d3.selectAll('svg#' + attrs.id + " " + "circle").style("fill-opacity", stackChartConfig.chartOpacity);
                                            mouseOver(d, offsetX, offsetY);
                                        }).on("mouseout", function () {
                                            hide_tooltip_grid_line(d3.select(this).attr("cx"), "circle", "cx");
                                            d3.selectAll('svg#' + attrs.id + " " + "path").style("fill-opacity", 1);
                                            d3.selectAll('svg#' + attrs.id + " " + "polygon").style("fill-opacity", 1);
                                            d3.selectAll('svg#' + attrs.id + " " + "circle").style("fill-opacity", 1);
                                            scope.tooltipFlag = false;
                                            scope.$apply();
                                        });
                                        circle.append('desc').append('title').text(function (d) {
                                            var nvdaObj = tooltipData[d.month];
                                            return (addTitle(nvdaObj, d));
                                        });
                                        break;
                                    case 'rect':
                                        var rect = dataPoints.attr("points", function (d) {
                                            var x = parseInt(xRange(d.numericMonth), 10);
                                            x = x + (stackChartConfig.shapeSize[1] / 2);
                                            var y = parseInt(yRange(d.y + d.y0), 10);
                                            y = y - (stackChartConfig.shapeSize[1] / 2);
                                            var x1 = parseInt((x - stackChartConfig.shapeSize[1]), 10);
                                            var y1 = y;
                                            var x2 = x1;
                                            var y2 = parseInt((y1 + stackChartConfig.shapeSize[1]), 10);
                                            var x3 = x;
                                            var y3 = y2;
                                            var tmp = x2 + "," + y2 + " " + x3 + "," + y3 + " " + x + "," + y + " " + x1 + "," + y1;
                                            return tmp;
                                        }).attr('x', function (d) {
                                            return xRange(d.numericMonth);
                                        }).attr('y', function (d) {
                                            return yRange(d.y + d.y0);
                                        }).on("mouseover", function (d) {
                                            show_tooltip_grid_line(d3.select(this).attr("x"), "rect", "x");
                                            var offsetX = Math.ceil(d3.select(this).attr("x"));
                                            var offsetY = Math.round(d3.select(this).attr("y"));
                                            d3.selectAll('svg#' + attrs.id + " " + "path").style("fill-opacity", stackChartConfig.chartOpacity);
                                            d3.selectAll('svg#' + attrs.id + " " + "polygon").style("fill-opacity", stackChartConfig.chartOpacity);
                                            d3.selectAll('svg#' + attrs.id + " " + "circle").style("fill-opacity", stackChartConfig.chartOpacity);
                                            mouseOver(d, offsetX, offsetY);
                                        }).on("mouseout", function () {
                                            hide_tooltip_grid_line(d3.select(this).attr("x"), "rect", "x");
                                            d3.selectAll('svg#' + attrs.id + " " + "path").style("fill-opacity", 1);
                                            d3.selectAll('svg#' + attrs.id + " " + "polygon").style("fill-opacity", 1);
                                            d3.selectAll('svg#' + attrs.id + " " + "circle").style("fill-opacity", 1);
                                            scope.tooltipFlag = false;
                                            scope.$apply();
                                        });
                                        rect.append('desc').append('title').text(function (d) {
                                            var nvdaObj = tooltipData[d.month];
                                            return (addTitle(nvdaObj, d));
                                        });
                                        break;
                                    case 'triangle':
                                        var triangle = dataPoints.attr("points", function (d) {
                                            var x = parseInt(xRange(d.numericMonth), 10);
                                            var y = parseInt(yRange(d.y + d.y0), 10);
                                            var x1 = parseInt((x - stackChartConfig.shapeSize[2]), 10);
                                            var y1 = parseInt((y - stackChartConfig.shapeSize[2]), 10);
                                            var x2 = parseInt((x + stackChartConfig.shapeSize[2]), 10);
                                            var y2 = y1;
                                            var tmp = x1 + "," + y1 + " " + x + "," + y + " " + x2 + "," + y2;
                                            return tmp;
                                        }).attr("transform", function (d) {
                                            var x = parseInt(xRange(d.numericMonth), 10);
                                            var y = parseInt(yRange(d.y + d.y0), 10);
                                            y = y - 2;
                                            var tmp = "rotate(180," + x + "," + y + ")";
                                            return tmp;
                                        }).attr('x', function (d) {
                                            return xRange(d.numericMonth);
                                        }).attr('y', function (d) {
                                            return yRange(d.y + d.y0);
                                        }).on("mouseover", function (d) {
                                            show_tooltip_grid_line(d3.select(this).attr("x"), "triangle", "x");
                                            var offsetX = Math.ceil(d3.select(this).attr("x"));
                                            var offsetY = Math.round(d3.select(this).attr("y"));
                                            d3.selectAll('svg#' + attrs.id + " " + "path").style("fill-opacity", stackChartConfig.chartOpacity);
                                            d3.selectAll('svg#' + attrs.id + " " + "polygon").style("fill-opacity", stackChartConfig.chartOpacity);
                                            d3.selectAll('svg#' + attrs.id + " " + "circle").style("fill-opacity", stackChartConfig.chartOpacity);
                                            mouseOver(d, offsetX, offsetY);
                                        }).on("mouseout", function () {
                                            hide_tooltip_grid_line(d3.select(this).attr("x"), "triangle", "x");
                                            d3.selectAll('svg#' + attrs.id + " " + "path").style("fill-opacity", 1);
                                            d3.selectAll('svg#' + attrs.id + " " + "polygon").style("fill-opacity", 1);
                                            d3.selectAll('svg#' + attrs.id + " " + "circle").style("fill-opacity", 1);
                                            scope.tooltipFlag = false;
                                            scope.$apply();
                                        });
                                        triangle.append('desc').append('title').text(function (d) {
                                            var nvdaObj = tooltipData[d.month];
                                            return (addTitle(nvdaObj, d));
                                        });
                                        break;
                                }
                            }

                            //Draw Grid Lines
                            drawChart.append("g")
                                    .attr("class", "grid")
                                    .attr("id", "yStackGrid")
                                    .attr("transform", "translate(0,0)")
                                    .call(yAxisGrid)
                                    .selectAll("text").remove();

                            // Draw the x Grid lines
                            drawChart.append("g")
                                    .attr("class", "grid")
                                    .attr("id", "xGrid")
                                    .attr("transform", "translate(" + (isSingleMonth === true) * width / 2 + ",0)")
                                    .call(make_x_axis()
                                            .tickSize(-height, 0)
                                            .tickFormat(""));

                            // function for the x grid lines
                            function make_x_axis() {
                                return d3.svg.axis()
                                        .scale(xRange)
                                        .orient("top")
                                        .tickFormat(d3.time.format('%b')).ticks(d3.time.months);
                            }

                            drawChart.select("#xGrid").selectAll("line").style("stroke", "none");
                            drawChart.select("#xGrid").selectAll("line")
                                    .attr("id", function (d, i) {
                                        return ("xAxisLine" + i);
                                    });
                            //Add title for NVDA screen reader
                            function addTitle(nvdaObj, d) {
                                var temp = "";
                                for (var y = 0; y < nvdaObj.length; y++) {
                                    temp = temp + nvdaObj[y].seriesName + nvdaObj[y].seriesVal;
                                }
                                return (tooltipFormat(d.numericMonth) + "--" + temp + "Total Charges" + nvdaObj.total);
                            }

                            //Show Grid Lines on Mouse Hover        
                            function show_tooltip_grid_line(offsetX, shape, attr) {
                                try {
                                    var dataLength;
                                    if (isSingleMonth) {
                                        dataLength = 1;
                                    } else {
                                        dataLength = scope.chartData[0].values.length;
                                    }

                                    for (var i = 0; i < dataLength; i++) {
                                        var circle = drawChart.selectAll("." + shape);
                                        if (circle[0][i].getAttribute(attr) === offsetX) {
                                            drawChart.select("#xAxisLine" + i).style("stroke", stackChartConfig.gridLineColor);
                                        }
                                    }
                                } catch (e) {
                                }
                            }

                            //Hide grid Lines
                            function hide_tooltip_grid_line(offsetX, shape, attr) {
                                try {
                                    var dataLength;
                                    if (isSingleMonth) {
                                        dataLength = 1;
                                    } else {
                                        dataLength = scope.chartData[0].values.length;
                                    }

                                    for (var i = 0; i < dataLength; i++) {
                                        var circle = drawChart.selectAll("." + shape);
                                        if (circle[0][i].getAttribute(attr) === offsetX) {
                                            drawChart.select("#xAxisLine" + i).style("stroke", "transparent");
                                        }
                                    }
                                } catch (e) {
                                }
                            }

                            //MouseOver Event
                            function mouseOver(d, offsetX, offsetY) {
                                for (var key in tooltipData) {
                                    if (key == d.month) {
                                        var data = angular.copy(tooltipData);
                                        scope.stackDataPoint = data[key].reverse();
                                        break;
                                    }
                                }
                                var tmp = 0, monthTotalVal = 0;
                                for (var b = 0; b < scope.stackDataPoint.length; b++) {
                                    tmp = parseFloat(scope.stackDataPoint[b].seriesVal);
                                    monthTotalVal = monthTotalVal + tmp;
                                }
                                scope.total = monthTotalVal;
                                scope.stackDataPoint.total = monthTotalVal;
                                
                                scope.monthPoint = {"xData": tooltipFormat(d.numericMonth).replace('-', ' ')};
                                scope.$apply();
                                $timeout(function(){
                                  offsetY = offsetY - stackChartConfig.tooltipTopMargin;
                                var tooltipEl = element.children().eq(2);
                                var tooltipWidth = tooltipEl[0].offsetWidth;
                                if (isSingleMonth) {
                                    offsetX = offsetX - (tooltipWidth/2) + stackChartConfig.tooltipLeftMargin + (width / 2);
                                } else {
                                    offsetX = offsetX - (tooltipWidth/2) + stackChartConfig.tooltipLeftMargin;
                                }  
                                scope.tooltipStyle = {"left": offsetX + "px", "top": offsetY + "px"};    
                                scope.tooltipFlag = true;    
                                },0);
                                

                                
                                
                            }
                            //fix for removing upper text if it is below to year label
//                            try {
//                                var lastTickPostion = drawChart.selectAll('#yAxisId0').selectAll('.tick')[0].length - 1;
//                                var lastGridPos = drawChart.select("#yStackGrid").selectAll('.tick')[0].length - 1;
//                                var val = drawChart.selectAll('#yAxisId0').selectAll('.tick')[0][lastTickPostion].attributes.transform.value.split(',')[1].split(')')[0];
//                                if (val < 20) {
//                                    drawChart.selectAll('#yAxisId0').selectAll('text')[0][lastTickPostion - 1].remove();
//                                    drawChart.select("#yStackGrid").selectAll('.tick')[0][lastGridPos].remove();
//                                }
//                            } catch (e) {
//                            }
                            scope.refreshChart = false;
                        });
                    }
                };
            }])
        .filter('filterInput', function () {
            return function (input) {
                return input.replace(/ +/g, "").toLowerCase();
            };
        });
angular.module("template/areachartD3/attAreaChartD3.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/areachartD3/attAreaChartD3.html",
    "<div class=\"areaChartD3\">\n" +
    "    <ul class=\"legendsList\" ng-show=\"showLegend\">\n" +
    "        <li ng-repeat=\"label in legendLabel\" id=\"{{label.series|filterInput}}\">\n" +
    "            <i class=\"legend-icon\" ng-style=\"addLegendColor($index)\"></i>{{label.series}}\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "    <div id=\"areaChartContainer\">\n" +
    "    </div>\n" +
    "    <div ng-class=\"{'tooltip--on': tooltipFlag,'tooltip': !tooltipFlag}\" ng-style=\"tooltipStyle\">\n" +
    "        <span class=\"title\">{{dataPoint.xData}}</span>\n" +
    "        <p>\n" +
    "            <span class=\"alignLeft\"><i class=\"legend-icon\" ng-style=\"addLegendColor(1)\"></i>Available</span>\n" +
    "            <span class=\"alignRight\">{{dataPoint.dataAvailable}} <span>{{dataPoint.usageDataType}}</span></span>\n" +
    "        </p>\n" +
    "        <div style=\"clear:both\"></div>          \n" +
    "        <p>\n" +
    "            <span class=\"alignLeft\"><i class=\"legend-icon\" ng-style=\"addLegendColor(0)\"></i>Used</span>\n" +
    "            <span class=\"alignRight\">{{dataPoint.dataUsed}} <span>{{dataPoint.availableDataType}}</span></span>\n" +
    "        </p>\n" +
    "        <div style=\"clear:both\"></div>\n" +
    "        <p ng-show=\"overageFlag\">\n" +
    "            <span class=\"alignLeft\"><i class=\"legend-icon\" ng-style=\"addLegendColor(2)\"></i>Overage</span> \n" +
    "            <span class=\"alignRight\">{{dataPoint.overage}}</span>\n" +
    "        </p>\n" +
    "        <div style=\"clear:both\"></div>\n" +
    "        <p ng-show=\"underageFlag\">\n" +
    "            <span class=\"alignLeft\">Buffer</span> \n" +
    "            <span class=\"alignRight\">{{dataPoint.underage}}</span>\n" +
    "        </p>\n" +
    "        <div style=\"clear:both\"></div>\n" +
    "        <span class=\"tooltipArrow\"></span>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("template/barchartD3/attBarChartD3.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/barchartD3/attBarChartD3.html",
    "<div class=\"barChartD3\">\n" +
    "    <div id=\"barChartContainer\">\n" +
    "        <span class=\"tooltip\" ng-class=\"{'tooltip--on': tooltipFlag}\" ng-show=\"tooltipFlag\" ng-style=\"tooltipStyle\">\n" +
    "            <span class=\"title\">{{dataPoint.name}}</span>\n" +
    "            <span><span class=\"alignText\">Wireless number</span><span style=\"float:right\">{{dataPoint.phoneNumber}}</span></span>\n" +
    "            <span><span class=\"alignText\">Charges</span><span style=\"float:right\">{{dataPoint.charges}}</span></span>\n" +
    "            <span class=\"tooltipArrow\"></span>\n" +
    "        </span>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("template/coschartD3/attCosd3Chart.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/coschartD3/attCosd3Chart.html",
    "<div class=\"attCosd3Chart\">\n" +
    "    <div class=\"chartLegend\" ng-if=\"legendPosition == 'top' && legendRequired\">\n" +
    "        <ul>\n" +
    "            <li ng-style=\"addLegendColor()\">\n" +
    "                <div>\n" +
    "                    <span class=\"LegendLabel\">{{LegendLabel}}</span> <span class=\"LegendCategory\">{{LegendCategory}}</span> <span class=\"legendItemValue\">({{cosval}})</span>\n" +
    "                </div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "    <div id={{chartID}} class=\"cosd3Container\"></div>\n" +
    "    <div class=\"chartLegend\" ng-if=\"legendPosition == 'bottom' && legendRequired\">\n" +
    "        <ul>\n" +
    "            <li ng-style=\"addLegendColor()\">\n" +
    "                <div>\n" +
    "                    <span class=\"LegendLabel\">{{LegendLabel}}</span> <span class=\"LegendCategory\">{{LegendCategory}}</span> <span class=\"legendItemValue\">({{cosval}})</span>\n" +
    "                </div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("template/coschartwithbarD3/attCosBarD3Chart.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/coschartwithbarD3/attCosBarD3Chart.html",
    "<div class=\"attCosWithBard3Chart\">\n" +
    "    <div id={{chartID}} class=\"cosd3Container\"></div>\n" +
    "    <div id={{snickrbarId}} class=\"snickrBar\" ng-if=\"barRequired\"></div>\n" +
    "</div>");
}]);

angular.module("template/cosmultichartD3/attCosmultid3Chart.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/cosmultichartD3/attCosmultid3Chart.html",
    "<div class=\"attCosmultid3Chart\">\n" +
    "    <div class=\"chartLegend\" ng-if=\"legendPosition == 'top' && legendRequired\">\n" +
    "        <ul>\n" +
    "          <li ng-repeat=\"item in chartData\" ng-style=\"addLegendColor($index,item)\" chart-legend-id=\"{{item.name}}\">\n" +
    "                <div>\n" +
    "                    <span class=\"LegendLabel\">{{item.name}}</span> <span class=\"LegendCategory\">{{item.category}}</span> <span class=\"legendItemValue\">({{item.value}}%)</span>\n" +
    "                </div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "    <div id={{chartID}} class=\"cosmultid3Container\"></div>\n" +
    "    <div class=\"chartLegend\" ng-if=\"legendPosition == 'bottom' && legendRequired\">\n" +
    "        <ul>\n" +
    "            <li ng-repeat=\"item in chartData\" ng-style=\"addLegendColor($index,item)\" chart-legend-id=\"{{item.name}}\">\n" +
    "                <div>\n" +
    "                    <span class=\"LegendLabel\">{{item.name}}</span> <span class=\"LegendCategory\">{{item.category}}</span> <span class=\"legendItemValue\">({{item.value}}%)</span>\n" +
    "                </div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("template/donutD3/attDonutd3Chart.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/donutD3/attDonutd3Chart.html",
    "<div class=\"attDonutd3Chart\">\n" +
    "    <div id={{chartID}} class=\"chartContainer\" ng-class=\"addStyle('chartContainer')\"></div>\n" +
    "    <div ng-show=\"legendRequired\" chartLegend class=\"chartLegend\" ng-class=\"addStyle('chartLegend')\">\n" +
    "        <ul>\n" +
    "            <li ng-repeat=\"item in chartData\" ng-style=\"addLegendColor($index,item)\" chart-legend-id=\"{{item.name|filterData}}\" ng-mouseover=\"legendMouseOver(item,$index,$event)\" ng-mouseleave=\"legendMouseLeave(item,$index,$event)\" ng-class=\"LegendClass\" ng-click=\"legendclickcallback({name:item.name , value:item.value})\">\n" +
    "                <div ng-class=\"{'emptyticket-category': item.value ===0}\" >\n" +
    "                    <span class=\"legendItemName\">{{item.name}}</span> <span class=\"legendItemValue\">({{item.value}})</span>\n" +
    "                </div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "    <div style=\"clear: both;\"></div>\n" +
    "</div>");
}]);

angular.module("template/donutFusion/attDonutfusionChart.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/donutFusion/attDonutfusionChart.html",
    "<div class=\"attDonutfusionChart\">\n" +
    "    <div id=\"donutFusionChart\" chartContainer class=\"chartContainer\" ng-style=\"addStyle('chartContainer')\"></div>\n" +
    "    <div ng-show=\"legendRequired\" chartLegend class=\"chartLegend\" ng-style=\"addStyle('chartLegend')\">\n" +
    "        <ul>\n" +
    "            <li ng-repeat=\"item in chartData\" ng-show=\"item.value > 0\" ng-style=\"addLegendColor($index)\">\n" +
    "                <div chart-legend-id=\"{{item.name|filterData}}_fusion\">\n" +
    "                    <span class=\"legendItemName\">{{item.name}}</span> <span class=\"legendItemValue\">({{item.value}})</span>\n" +
    "                </div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "    <div style=\"clear: both;\">\n" +
    "    </div>\n" +
    "</div>    ");
}]);

angular.module("template/horseshoeD3/attHorseshoeD3Chart.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/horseshoeD3/attHorseshoeD3Chart.html",
    "<div class=\"attHorseshoed3Chart\">\n" +
    "    <div class=\"chartLegend\" ng-if=\"legendPosition == 'top' && legendRequired\">\n" +
    "        <ul>\n" +
    "            <li ng-style=\"addLegendColor()\">\n" +
    "                <div>\n" +
    "                    <span class=\"LegendLabel\">{{LegendLabel}}</span> <span class=\"LegendCategory\">{{LegendCategory}}</span> <span class=\"legendItemValue\">({{guageVal}})</span>\n" +
    "                </div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "    <div class=\"horseshoed3Container\"></div>\n" +
    "    <div class=\"chartLegend\" ng-if=\"legendPosition == 'bottom' && legendRequired\">\n" +
    "        <ul>\n" +
    "            <li ng-style=\"addLegendColor()\">\n" +
    "                <div>\n" +
    "                    <span class=\"LegendLabel\">{{LegendLabel}}</span> <span class=\"LegendCategory\">{{LegendCategory}}</span> <span class=\"legendItemValue\">({{guageVal}})</span>\n" +
    "                </div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("template/stackedBarchart/stackedBarchart.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/stackedBarchart/stackedBarchart.html",
    "<div class=\"stackedBarChart\">\n" +
    "    <ul class=\"legendsList\" ng-show=\"showLegend\">\n" +
    "        <li ng-repeat=\"label in chartData\" id=\"{{label.series|filterInput}}\">\n" +
    "            <i class=\"legend-icon\" ng-style=\"addLegendColor($index)\"></i>{{label.series}}\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "    <div id=\"stackBarChartContainer\"></div>\n" +
    "    <div ng-class=\"{'tooltip--on': tooltipFlag,'tooltip': !tooltipFlag}\" ng-style=\"tooltipStyle\">\n" +
    "        <span class=\"title\">{{monthPoint.xData}}</span>\n" +
    "        <div ng-repeat=\"label in stackDataPoint\">\n" +
    "            <p>\n" +
    "                <span class=\"alignLeft\"><i class=\"legend-icon-stackedchart\" ng-style=\"addLegendColor($index)\"></i>{{label.seriesName}}</span>\n" +
    "                <span class=\"alignRight\">{{label.seriesVal| number:0}}</span>\n" +
    "            </p> \n" +
    "\n" +
    "            <div style=\"clear:both\"></div>\n" +
    "        </div>\n" +
    "        <p>\n" +
    "            <span class=\"alignLeft\"><i class=\"legend-icon-stackedchart\"></i>Retainability percentage</span>\n" +
    "            <span class=\"alignRight\">{{stackDataPoint.seriesPer}}</span>\n" +
    "\n" +
    "        </p>\n" +
    "        <div style=\"clear:both\"></div>\n" +
    "        <span class=\"tooltipArrow\"></span>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("template/stackedareachart/stackedAreaChart.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/stackedareachart/stackedAreaChart.html",
    "<div class=\"stackedAreaChart\">\n" +
    "    <ul class=\"legendsList\" ng-show=\"showLegend\">\n" +
    "        <li ng-repeat=\"label in chartData\" id=\"{{label.series|filterInput}}\">\n" +
    "            <i class=\"legend-icon\" ng-style=\"addLegendColor($index)\"></i>{{label.series}}\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "    <div id=\"stackChartContainer\"></div>\n" +
    "    <div ng-class=\"{'tooltip--on': tooltipFlag,'tooltip': !tooltipFlag}\" ng-style=\"tooltipStyle\">\n" +
    "        <span class=\"title\">{{monthPoint.xData}}</span>\n" +
    "        <div ng-repeat=\"label in stackDataPoint\">\n" +
    "            <p>\n" +
    "                <span class=\"alignLeft\"><i class=\"legend-icon-stackedchart\" ng-style=\"addToolTipLegendColor($index)\"></i>{{label.seriesName}}</span>\n" +
    "                <span class=\"alignRight\">{{label.seriesVal| currency}}</span>\n" +
    "            </p>\n" +
    "            <div style=\"clear:both\"></div>  \n" +
    "        </div>\n" +
    "        <p>\n" +
    "            <span class=\"alignLeft\">Total charges</span>\n" +
    "            <span class=\"alignRight\">{{stackDataPoint.total| currency}}</span>\n" +
    "        </p>\n" +
    "        <div style=\"clear:both\"></div>\n" +
    "        <span class=\"tooltipArrow\"></span>\n" +
    "    </div>\n" +
    "</div>");
}]);
