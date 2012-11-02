#version 150

uniform sampler2D diffuse;
in vec4 Color;
in vec2 Tex;
out vec4 outColor;

void main()
{
    outColor = texture(diffuse, Tex);
}
