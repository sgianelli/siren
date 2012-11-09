#version 330

uniform sampler2D diffuse;
in vec4 Color;
in vec2 Tex;
layout(location = 0) out vec4 outColor;

void main()
{
    outColor = texture(diffuse, Tex);
}
