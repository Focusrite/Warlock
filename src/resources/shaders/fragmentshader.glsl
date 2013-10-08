#version 330 core

// Interpolated values from the vertex shaders
in vec2 UV;
in vec4 clr;
in int renderMode;

out vec4 color;

uniform sampler2D myTextureSampler;

void main(){
   color = clr;
   if(renderMode == 1) {
      color = texture( myTextureSampler, UV ).rgba;
   }
}