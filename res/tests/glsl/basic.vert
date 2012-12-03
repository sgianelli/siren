#version 330

in vec2 position;
in vec4 color;
in vec2 tex;

out vec4 Color;
out vec2 Tex;

uniform mat4 world;
uniform mat4 projection;
uniform mat4 scale;

void main()
{
    gl_Position = projection * world * vec4(position, 0, 1.0);
    Tex = tex;
    Color = color;
}
