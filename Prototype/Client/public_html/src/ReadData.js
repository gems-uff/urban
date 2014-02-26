/* 
 * Urban data visualization.
 * Universidade Federal Fluminense.
 * 
 * Copyright (C) 2014 - Prof. Marcos Lage
 */

function ReadData(url) { 
    this.url = url;
};

ReadData.prototype.url = '';

ReadData.prototype.accessFile = function() {

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", this.url, false);
    xmlhttp.send();

    var csv = xmlhttp.responseText;

    var allTextLines = csv.split(/\r\n|\n/);
    var lines = [];
    for (var i = 1; i < allTextLines.length; i++) {
        var data = allTextLines[i].split(',');   
        console.log(data);
        var pnt = Utils.LatLongToPixelXY(data[data.length - 2], data[data.length - 1]);

        lines.push(pnt.x);
        lines.push(pnt.y);        
    }
    return lines;
};

ReadData.prototype.accessSQLite = function() {

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", this.url, false);
    xmlhttp.send();

    var myJson = xmlhttp.responseText;    
    myJson = JSON.parse(myJson);

    var lines = [];
    for (var i=0; i<myJson.length; i++) {
        var data = myJson[i];   
        var pnt = Utils.LatLongToPixelXY(data[0], data[1]);

        lines.push(pnt.x);
        lines.push(pnt.y);        
    }
    return lines;
};