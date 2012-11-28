#version 330

uniform sampler2D diffuse;
in vec3 Color;
in vec2 Tex;
out vec4 outColor;

void main()
{
    outColor = texture(diffuse, Tex) * vec4(Color, 1.0);
}
