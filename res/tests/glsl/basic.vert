#version 150

in vec4 position;
in vec4 color;
in vec2 tex;

out vec4 Color;
out vec2 Tex;

uniform mat4 world;

void main()
{
    gl_Position = world * position;
    Tex = tex;
    Color = color;
}
