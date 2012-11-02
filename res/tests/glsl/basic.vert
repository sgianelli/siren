#version 150

in vec3 color;
in vec3 position;

out vec3 Color;

uniform mat4 view;
uniform mat4 proj;
uniform mat4 world;


void main()
{
    Color = color;
    gl_Position = world * vec4(position, 10.0);
}
