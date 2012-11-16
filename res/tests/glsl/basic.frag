#version 330

uniform sampler2D diffuse;
uniform vec3 hsv;
in vec4 Color;
in vec2 Tex;
layout(location = 0) out vec4 outColor;

vec4 rgb2hsv(vec4 rgba)
{
    float mrgba, max, delta;
    float h, s, v;

    mrgba = rgba.r < rgba.g ? rgba.r : rgba.g;
    mrgba = mrgba  < rgba.b ? mrgba  : rgba.b;

    max = rgba.r > rgba.g ? rgba.r : rgba.g;
    max = max  > rgba.b ? max  : rgba.b;

    v = max;                                
    delta = max - mrgba;
    if (max > 0.0) {
        s = (delta / max);                 
    } else {
        s = 0.0;
        h = 0.0;
        return vec4(h, s, v, rgba.a);
    }

    if (rgba.r >= max) {
        h = (rgba.g - rgba.b) / delta;        
    } else if( rgba.g >= max ) {
        h = 2.0 + (rgba.b - rgba.r) / delta;  // between cyan & yellow
    } else {
        h = 4.0 + (rgba.r - rgba.g) / delta;  // between magenta & cyan
    }

    h *= 60.0;                              

    if (h < 0.0) {
        h += 360.0;
    }

    return vec4(h, s, v, rgba.a);
}


vec4 hsv2rgb(vec4 hsva)
{
    float hh, p, q, t, ff;
    int i;
    vec4 rgba;

    rgba.a = hsva.a;

    float h = hsva.r, s = hsva.g, v = hsva.b, a = hsva.a;

    if (s <= 0.0) {       
        rgba.r = 0.0;
        rgba.g = 0.0;
        rgba.b = 0.0;
        return rgba;
    }

    hh = hsva.r;

    if (hh >= 360.0) {
        hh = 0.0;
    }

    hh /= 60.0;
    i = int(hh);
    ff = hh - i;
    p = v * (1.0 - s);
    q = v * (1.0 - (s * ff));
    t = v * (1.0 - (s * (1.0 - ff)));

    switch(i) {
    case 0:
        rgba.r = v;
        rgba.g = t;
        rgba.b = p;
        break;
    case 1:
        rgba.r = q;
        rgba.g = v;
        rgba.b = p;
        break;
    case 2:
        rgba.r = p;
        rgba.g = v;
        rgba.b = t;
        break;
    case 3:
        rgba.r = p;
        rgba.g = q;
        rgba.b = v;
        break;
    case 4:
        rgba.r = t;
        rgba.g = p;
        rgba.b = v;
        break;
    case 5:
    default:
        rgba.r = v;
        rgba.g = p;
        rgba.b = q;
        break;
    }
    return rgba;     
}

void main()
{
    if ((Tex.x < 0.02 || Tex.x > 0.98 || Tex.y < 0.02 || Tex.y > 0.98)) {
        outColor = vec4(1.0, 0.0, 0.0, 1.0);
    } else {
        outColor = hsv2rgb(vec4(hsv, 1.0) * rgb2hsv(texture(diffuse, Tex)));
    }
}
