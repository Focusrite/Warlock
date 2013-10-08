layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec2 vertexUV;

uniform mat4 MVP;
uniform vec4 color;
uniform int shaderMode;

out vec4 clr;
out int renderMode;
out vec2 UV;

void main(){
    // Output position of the vertex, in clip space : MVP * position
    vec4 v = vec4(vertexPosition,1);
    gl_Position = MVP * v;
    clr = color;
    renderMode = shaderMode;
    UV = vertexUV;
}