#version 330

// Interpolated values from the vertex shaders
in vec2 UV;
in vec4 clr;
in int renderMode;

uniform sampler2D myTextureSampler;

void main(){
   if(renderMode == 1) {
      vec4 sampledColor = texture( myTextureSampler, UV );
      float r = clr.r * sampledColor.r;
      float g = clr.g * sampledColor.g;
      float b = clr.b * sampledColor.b;
      float a = clr.a * sampledColor.a;
      if(a < 0.1) {
         discard;
      }
      gl_FragColor = vec4(r, g, b, a);
   }
   else {
      if(clr.a < 0.1) {
         discard;
      }
      gl_FragColor = clr;
   }
}