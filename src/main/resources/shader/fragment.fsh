#version 450 core

in vec3 color;
uniform float opacity;

out vec4 fragColor;

void main(){
    fragColor = vec4(color, opacity);
}
