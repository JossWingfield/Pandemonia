#type vertex
#version 330 core
layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aTexCoord;

out vec2 vTexCoord;

void main() {
    gl_Position = vec4(aPos, 0.0, 1.0);
    vTexCoord = aTexCoord;
}
#type fragment
#version 330 core

in vec2 vTexCoord;
out vec4 fragColor;

uniform sampler2D uTexture;
uniform float uIntensity;     // overall brightness
uniform float uDecay;         // decay per sample
uniform float uDensity;       // step size
uniform int uSamples;         // number of samples
uniform float uVerticalBias;  // 0 = normal, 1 = fully vertical
uniform float uTime;          // for flicker
uniform float uTexelSizeY;    // 1.0 / texture height

void main() {
    vec2 uv = vTexCoord;
    vec4 color = vec4(0.0);
    float weight = 1.0;

    // Direction for rays (vertical bias pushes downward)
    vec2 delta = vec2(0.0, 1.0); // straight down
    delta.x = delta.x * (1.0 - uVerticalBias);

    // Step per sample
    vec2 step = delta * uDensity;

    // Flicker effect
    float flicker = 0.9 + 0.1 * sin(uTime * 5.0 + uv.y * 10.0); // subtle per-row flicker

    for(int i = 0; i < uSamples; i++) {
        uv += step * uTexelSizeY; // move one pixel down each step
        vec4 sample = texture(uTexture, uv);
        color += sample * weight;
        weight *= uDecay; // decay
    }

    // Multiply by intensity and flicker
    fragColor = color * uIntensity * flicker;

    // Optional: fade out at bottom
    // Slower fade
	float fade = 1.0 - pow(vTexCoord.y, 0.5); // square root curve, slower fade
	fragColor.a *= fade;
}