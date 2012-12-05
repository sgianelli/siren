#version 330

uniform sampler2D diffuse;
uniform vec4 hsv;
in vec4 Color;
in vec2 Tex;
layout(location = 0) out vec4 outColor;

void main()
{
    if (false && (Tex.x < 0.02 || Tex.x > 0.98 || Tex.y < 0.02 || Tex.y > 0.98)) {
        outColor = vec4(1.0, 0.0, 0.0, 1.0);
    } else {
        outColor = Color * texture(diffuse, Tex);
    }
}
