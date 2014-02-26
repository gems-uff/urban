precision mediump float;

void main() {
    float dist  = length(gl_PointCoord.xy - vec2(.5, .5));
    float alpha = (dist > 0.5)?0.0:(dist > 0.4)?1.0:0.7;
    
    // set pixels in points to green
    gl_FragColor = vec4(.3, .58, 0.8, 1.0)*alpha;
}
