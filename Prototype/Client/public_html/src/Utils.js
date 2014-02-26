/* 
 * Urban data visualization.
 * Universidade Federal Fluminense.
 * 
 * Copyright (C) 2014 - Prof. Marcos Lage
 */

function Utils(){ };

Utils.scaleMatrix = function(matrix, scaleX, scaleY) {
    // scaling x and y, which is just scaling first two columns of matrix
    matrix[0] *= scaleX;
    matrix[1] *= scaleX;
    matrix[2] *= scaleX;
    matrix[3] *= scaleX;

    matrix[4] *= scaleY;
    matrix[5] *= scaleY;
    matrix[6] *= scaleY;
    matrix[7] *= scaleY;
};

Utils.translateMatrix = function(matrix, tx, ty) {
    // translation is in last column of matrix
    matrix[12] += matrix[0] * tx + matrix[4] * ty;
    matrix[13] += matrix[1] * tx + matrix[5] * ty;
    matrix[14] += matrix[2] * tx + matrix[6] * ty;
    matrix[15] += matrix[3] * tx + matrix[7] * ty;
};

Utils.LatLongToPixelXY = function(latitude, longitude) {
    var pixelsPerLonDegree = 256 / 360;
    var pixelsPerLonRadian = 256 / (2 * Math.PI);

    var siny = Utils.bound(Math.sin(latitude* Math.PI/180), -0.9999, 0.9999);

    var pixelX = 128 + longitude * pixelsPerLonDegree;
    var pixelY = 128 + 0.5 * Math.log((1 + siny) / (1 - siny)) * -pixelsPerLonRadian;
    
    return {x: pixelX, y: pixelY};
};

Utils.bound = function(value, opt_min, opt_max) {
  if (opt_min !== null) value = Math.max(value, opt_min);
  if (opt_max !== null) value = Math.min(value, opt_max);
  return value;
};