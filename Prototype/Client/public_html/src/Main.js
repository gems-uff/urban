/* 
 * Urban data visualization.
 * Universidade Federal Fluminense.
 * 
 * Copyright (C) 2014 - Prof. Marcos Lage
 */

var map;
var gl;
var shader;
var canvasLayer;

var nPoints;

var pixelsToWebGLMatrix = new Float32Array(16);
var mapMatrix = new Float32Array(16);

function init() {
    // initialize the map
    var mapOptions = {
        zoom: 13, 
        center: new google.maps.LatLng(42.3605261, -71.0581373),
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var mapDiv = document.getElementById('map-div');
    map = new google.maps.Map(mapDiv, mapOptions);
    
    // initialize the CanvasLayer
    var canvasLayerOptions = {
        map: map,
        resizeHandler: resize,
        animate: false,
        updateHandler: update
    };
    canvasLayer = new CanvasLayer(canvasLayerOptions);
    
    // initialize WebGL
    gl = canvasLayer.canvas.getContext('webgl') || canvasLayer.canvas.getContext('experimental-webgl');
    
    // initialize the shaders
    shader = new ShaderProgram(gl);
    shader.create("./shaders/shader.vert", "./shaders/shader.frag");
    
    bindData();
}

function bindData() {
    var dataLoader = new ReadData("http://localhost:8000/api?stations");
   
    var data = dataLoader.accessSQLite();
    nPoints = data.length / 2;

    // load x,y coords in a world coordinate bounding box
    var rawData = new Float32Array(data);
    
    // create webgl buffer, bind it, and load rawData into it
    var pointArrayBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, pointArrayBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, rawData, gl.STATIC_DRAW);

    // enable the 'worldCoord' attribute in the shader to receive buffer
    var attributeLoc = gl.getAttribLocation(shader.myProgram, 'worldCoord');
    gl.enableVertexAttribArray(attributeLoc);

    // tell webgl how buffer is laid out (pairs of x,y coords)
    gl.vertexAttribPointer(attributeLoc, 2, gl.FLOAT, false, 0, 0);
}

function resize() {
    var width  = canvasLayer.canvas.width;
    var height = canvasLayer.canvas.height;

    gl.viewport(0, 0, width, height);

    // matrix which maps pixel coordinates to WebGL coordinates
    pixelsToWebGLMatrix.set([2 / width, 0, 0, 0, 0, -2 / height, 0, 0,
        0, 0, 0, 0, -1, 1, 0, 1]);
}

function update() {
    gl.clear(gl.COLOR_BUFFER_BIT);

    var mapProjection = map.getProjection();

    // copy pixel->webgl matrix
    mapMatrix.set(pixelsToWebGLMatrix);

    // Scale to current zoom (worldCoords * 2^zoom)
    var scale = Math.pow(2, map.zoom);
    Utils.scaleMatrix(mapMatrix, scale, scale);

    // translate to current view (vector from topLeft to 0,0)
    var offset = mapProjection.fromLatLngToPoint(canvasLayer.getTopLeft());
    Utils.translateMatrix(mapMatrix, -offset.x, -offset.y);

    // attach matrix value to 'mapMatrix' uniform in shader
    var matrixLoc = gl.getUniformLocation(shader.myProgram, 'mapMatrix');
    gl.uniformMatrix4fv(matrixLoc, false, mapMatrix);

    // draw!
    gl.drawArrays(gl.POINTS, 0, nPoints);
}

document.addEventListener('DOMContentLoaded', init, false);

