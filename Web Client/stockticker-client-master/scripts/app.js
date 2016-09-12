var host = "http://localhost:1337/";
var maxChartLegends = 20;
var stockValues = [];
var stocks = [];
var fetchCnt;
var fetchNr;
var colors = [
    {r: 161, g: 166, b: 230},
    {r: 161, g: 230, b: 165},
    {r: 230, g: 161, b: 170},
    {r: 216, g: 227, b: 98},
    {r: 227, g: 167, b: 98},
    {r: 98, g: 227, b: 220},
    {r: 222, g: 222, b: 222}
];
var lastUsedColor = 0;

function setOptions(data) {
    if (data != null && data != "") {
        var sel = $('#stock_selection');
        var options = [];
        $.each(data, function (key, val) {
            var s = '<option value="' + val.id + '">' + val.name + '</option>';
            options.push(s);
            var o = new Object();
            o.id = val.id;
            o.name = val.name;
            stocks.push(o);
        });
        var h = options.join(" ");
        sel.html(h);
    } else {
        toastr.error('invalid stock data');
    }
}

function getOptions() {
    $.get(host + "options", function (data) {
        if (data != null && data != "") {
            setOptions(data);
        } else {
            toastr.error('stock option retrieval failed');
        }
    });
}

function getSelectedIds() {
    return $('#stock_selection').val();
}

function parseDateInput(date) {
    try {
        var dateParts = date.split(".");
        var d = new Date(dateParts[2], (dateParts[1] - 1), dateParts[0]);
        var now = new Date().getTime();
        var time = d.getTime();
        if (now < time){
            toastr.warning('future date selected, aborting...');
            return null;
        }
        return d;
    } catch (e) {
        toastr.warning('invalid date!');
        return null;
    }
}

function findNameForId(id) {
    for (var i = 0; i < stocks.length; i++) {
        if (stocks[i].id == id) {
            return stocks[i].name;
        }
    }
}

function getNextColor() {
    var col = colors[lastUsedColor];
    lastUsedColor++;
    if (lastUsedColor == colors.length) {
        lastUsedColor = 0;
    }
    return col;
}

function componentToHex(c) {
    var hex = c.toString(16);
    return hex.length == 1 ? "0" + hex : hex;
}

function rgbToHex(color) {
    return "#" + componentToHex(color.r) + componentToHex(color.g) + componentToHex(color.b);
}

function createLegend(name, color) {
    return '<span style="color:' + rgbToHex(color) + '">' + name + '</span>';
}

function createDataset(values, color) {
    var priceList = [];
    for (var i = 0; i < values.values.length; i++) {
        priceList.push(values.values[i].price);
    }
    var step = 1;
    if (priceList.length>maxChartLegends){
        var newList = [];
        var lastPrice = null;
        step = Math.ceil(priceList.length / maxChartLegends);
        for (var i = 0; i < priceList.length; i += step) {
            if (i >= priceList.length) {
                var temp = priceList[priceList.length-1];
                if (temp != lastPrice) {
                    newList.push(temp);
                }
            } else {
                var temp = priceList[i];
                newList.push(temp);
                lastPrice = temp;
            }
        }
        priceList = newList;
    }
    var o = new Object();
    var col = 'rgba(' + color.r + ',' + color.g + ',' + color.b + ',1)';
    o.strokeColor = col;
    o.pointColor = col;
    o.pointStrokeColor = '#fff';
    o.data = priceList;
    return o;
}

function createTimeLabels(times) {
    var ret = [];
    for (var i = 0; i < times.length; i++) {
        var d = times[i];
        var date = d.getDate();
        var month = d.getMonth()+1;
        var hours = d.getHours();
        var mins = d.getMinutes();
        var secs = d.getSeconds();
        var s = date>9?date:'0'+date;
        s += '.';
        s += month>9?month:'0'+month;
        s += '.';
        s += d.getFullYear();
        s += ' ';
        s += hours>9?hours:'0'+hours;
        s += ':';
        s += mins>9?mins:'0'+mins;
        s += ':';
        s += secs>9?secs:'0'+secs;
        ret.push(s);
    }
    return ret;
}

function createChartData(sets, times) {
    var data = new Object();
    data.labels = createTimeLabels(times);
    data.datasets = sets;
    return data;
}

function paintChart(ctx, data) {
    new Chart(ctx).Line(data, {datasetFill: false, scaleShowGridLines : true, scaleGridLineColor : "rgba(0,0,0,.05)", scaleGridLineWidth : 1, pointDot : true});
}

function paintLegend(legends) {
    var d = $('#legend_div');
    var h = '</hr></br><p>Caption: </p></br>'
    h += legends.join('</br>');
    d.html(h);
}

function getTimes(values) {
    var times = [];
    var step = 1;
    var lastTime = null;
    if (values.length > maxChartLegends) {
        step = Math.ceil(values.length / maxChartLegends);
    }
    for (var i = 0; i < values.length; i += step) {
        if (i >= values.length) {
            var temp = new Date(values[values.length - 1].time);
            if (temp != lastTime) {
                times.push(temp);
            }
        } else {
            var time = values[i].time;
            var d = new Date(time);
            times.push(d);
            lastTime = d;
        }
    }
    return times;
}

function continueLoad() {
    var legends = [];
    var sets = [];
    if (stockValues.length < 1){
        toastr.error('no values retrieved');
        return;
    }
    for (var i = 0; i < stockValues.length; i++) {
        var val = stockValues[i];
        var c = getNextColor();
        legends.push(createLegend(val.name, c));
        sets.push(createDataset(val, c));
    }
    var times = getTimes(stockValues[0].values);
    var cData = createChartData(sets, times);
    var ctx = $("#myChart").get(0).getContext("2d");
    paintChart(ctx, cData);
    paintLegend(legends);
}

function getValues(id, start, end) {
    var req = 'prize?id=' + id + '&start=' + start.getTime() + '&end=' + end.getTime();
    $.get(host + req, function (data) {
        if (data != null && data != "") {
            var o = new Object();
            o.id = id;
            o.name = findNameForId(id);
            o.values = data;
            stockValues.push(o);
        } else {
            toastr.error('stock value retrieval failed for: ' + id);
        }
        fetchCnt++;
        if (fetchCnt == fetchNr) {
            continueLoad();
        }
    });
}

function loadChart() {
    idl = getSelectedIds();
    if (idl == null) {
        toastr.warning('nothing selected!');
    } else {
        lastUsedColor = 0;
        var start = parseDateInput($('#start').val());
        if (start == null) return;
        var end = parseDateInput($('#end').val());
        if (end == null) return;
        if (end.getTime()<start.getTime()){
            toastr.warning('start date has to be before end date');
            return;
        }
        var today = new Date();
        today.setHours(0,0,0,0);
        if (end.getTime()==today.getTime()){
            today.setHours(23,59,59,0);
            end = today;
        }
        fetchCnt = 0;
        fetchNr = idl.length;
        stockValues = [];
        $.each(idl, function (index, value) {
            getValues(value, start, end);
        });
    }
}
