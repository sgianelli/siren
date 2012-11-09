#version 330

// Same inputs as before
uniform sampler2D diffuse;  
in vec4 Color;
in vec2 Tex;
out vec4 outColor;

// 9x9 gaussian
const float pi = 3.14159265f;
const float sigma = 5;     
const float blur_size = 1.0f / 480.0f;  
const float num_blur_pixels = 4.0f;
const vec2 blur = vec2(0.0f, 1.0f);

void main() 
{
    // (See GPU Gems 3 pp. 877 - 889)
    // Incremental Gaussian Coefficent Calculation 
    vec3 inc_gauss;
    inc_gauss.x = 1.0f / (sqrt(2.0f * pi) * sigma);
    inc_gauss.y = exp(-0.5f / (sigma * sigma));
    inc_gauss.z = inc_gauss.y * inc_gauss.y;

    vec4 avg_val = vec4(0.0f, 0.0f, 0.0f, 0.0f);
    float coeff_sum = 0.0f;

    // Take the central sample first...
    avg_val += texture(diffuse, Tex.xy) * inc_gauss.x;
    coeff_sum += inc_gauss.x;
    inc_gauss.xy *= inc_gauss.yz;

    // Go through the remaining 8 vertical samples (4 on each side of the center)
    for (float i = 1.0f; i <= num_blur_pixels; i++) { 
        avg_val += texture(diffuse, Tex.xy - i * blur_size * blur) * inc_gauss.x;         
        avg_val += texture(diffuse, Tex.xy + i * blur_size * blur) * inc_gauss.x;         
        coeff_sum += 2 * inc_gauss.x;
        inc_gauss.xy *= inc_gauss.yz;
    }

    outColor = avg_val / coeff_sum;
}
