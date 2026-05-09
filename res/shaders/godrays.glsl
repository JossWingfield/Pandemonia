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

uniform float uIntensity;
uniform float uDecay;
uniform float uDensity;
uniform int uSamples;
uniform float uVerticalBias;
uniform float uTime;
uniform float uTexelSizeY;

// Distance below the window
uniform float uYOffset;

void main() {

    // ---------------------------------------------------
    // PROJECTED LIGHT TEXTURE
    // ---------------------------------------------------

    vec2 projectedUV = vTexCoord;

    // Flip vertically
    projectedUV.y = 1.0 - projectedUV.y;

    // Move projection downward
    projectedUV.y += uYOffset;

    // Prevent texture wrapping artifacts
    vec4 projectedLight = vec4(0.0);

    if(projectedUV.x >= 0.0 && projectedUV.x <= 1.0 &&
       projectedUV.y >= 0.0 && projectedUV.y <= 1.0)
    {
        projectedLight = texture(uTexture, projectedUV);
    }

    // ---------------------------------------------------
    // GOD RAYS
    // ---------------------------------------------------

    vec2 rayUV = vTexCoord;
    rayUV.y += uYOffset;

    vec4 rayColor = vec4(0.0);

    float weight = 1.0;

    // Rays travel downward
    vec2 delta = vec2(0.0, -1.0);

    // Optional horizontal suppression
    delta.x *= (1.0 - uVerticalBias);

    vec2 step = delta * uDensity;

    // Subtle flicker
    float flicker =
        0.97 +
        0.03 * sin(uTime * 4.0 + vTexCoord.y * 8.0);

    for(int i = 0; i < uSamples; i++) {

    rayUV += step * uTexelSizeY;

    vec4 sampleColor = texture(uTexture, rayUV);

    // Distance from source
    float t = float(i) / float(uSamples);

    // Brightest near window
    float sourceStrength = pow(1.0 - t, 2.5);

    // Beam persistence
    float decayStrength = pow(uDecay, float(i));

    rayColor += sampleColor
              * sourceStrength
              * decayStrength;
}

    // ---------------------------------------------------
// COMBINE
// ---------------------------------------------------

// Visible projected texture
vec4 finalColor = projectedLight * 0.45;

// Soft downward fade
float fade = 1.0 - pow(vTexCoord.y, 0.7);


// Add rays ONCE
finalColor += rayColor * uIntensity * 0.12 * flicker;

finalColor.a *= fade;

fragColor = finalColor;
}