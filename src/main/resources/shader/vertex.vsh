#version 450 core

in vec3 position;

out vec3 color;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(){
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
    color = vec3(position.x*0.01 + 0.25, 0.17, position.y*0.01 + 0.25);

}