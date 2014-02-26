/* 
 * Urban data visualization.
 * Universidade Federal Fluminense.
 * 
 * Copyright (C) 2014 - Prof. Marcos Lage
 */

function ShaderProgram(glContext) {
    this.glContext = glContext;
};

ShaderProgram.prototype.glContext = '';
ShaderProgram.prototype.myProgram = '';

ShaderProgram.prototype.loadShader = function(url) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", url, false);
    xmlhttp.send();
    return xmlhttp.responseText;
};

ShaderProgram.prototype.create = function(vertUrl, fragUrl) {
    // create vertex shader
    var vertexSrc = this.loadShader(vertUrl);
    console.log(vertexSrc);

    var vertexShader = this.glContext.createShader(this.glContext.VERTEX_SHADER);
    if (vertexShader === 0) {
        throw "could not created the vertex shader.";
    }

    this.glContext.shaderSource(vertexShader, vertexSrc);
    this.glContext.compileShader(vertexShader);
    
    // validate compilation
    if ( !this.glContext.getShaderParameter(vertexShader, this.glContext.COMPILE_STATUS) ) {
        throw this.glContext.getShaderInfoLog(vertexShader);
    }    

    // create fragment shader
    var fragmentSrc = this.loadShader(fragUrl);
    console.log(fragmentSrc);

    var fragmentShader = this.glContext.createShader(this.glContext.FRAGMENT_SHADER);
    if (fragmentShader === 0) {
        throw "could not created the fragment shader";
    }

    this.glContext.shaderSource(fragmentShader, fragmentSrc);
    this.glContext.compileShader(fragmentShader);
    
    // validate compilation
    if ( !this.glContext.getShaderParameter(fragmentShader, this.glContext.COMPILE_STATUS) ) {
        throw this.glContext.getShaderInfoLog(fragmentShader);
    }
    
    // create the shader program. If OK, create vertex and fragment shaders
    this.myProgram = this.glContext.createProgram();
    
    // load and compile the two shaders
    this.glContext.attachShader(this.myProgram, vertexShader);
    this.glContext.attachShader(this.myProgram, fragmentShader);
    
    // now link the program
    this.glContext.linkProgram(this.myProgram);
    // validate linking
    if ( !this.glContext.getProgramParameter(this.myProgram, this.glContext.LINK_STATUS) ) {
        throw "could not link the shader program";
    }
    
    // now makes the program active
    this.glContext.useProgram(this.myProgram);
    this.glContext.validateProgram(this.myProgram);
    if ( !this.glContext.getProgramParameter(this.myProgram, this.glContext.VALIDATE_STATUS) ) {
        throw  "could not validate the shader program";
    }
};




