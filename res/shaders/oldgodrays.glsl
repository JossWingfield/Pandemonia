#type vertex
#version 330 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aTexCoord;

out vec2 vTexCoord;

void main()
{
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
uniform float uYOffset;

uniform vec2 uScreenSize;

uniform vec3 uAmbientColor;
uniform float uAmbientIntensity;

void main()
{
    // =========================================================
    // PIXEL SNAP
    // =========================================================

    float pixelSize = 6.0;

    vec2 fragPos = vec2(
        vTexCoord.x * uScreenSize.x,
        (1.0 - vTexCoord.y) * uScreenSize.y
    );

    fragPos = floor(fragPos / pixelSize) * pixelSize;

    vec2 snappedUV = vec2(
        fragPos.x / uScreenSize.x,
        1.0 - (fragPos.y / uScreenSize.y)
    );

    // =========================================================
    // PROJECTED LIGHT TEXTURE
    // =========================================================

    vec2 projectedUV = vec2(
        snappedUV.x,
        1.0 - snappedUV.y
    );

    projectedUV.y += uYOffset;

    vec4 projectedLight = vec4(0.0);

    bvec2 inside = bvec2(
        projectedUV.x >= 0.0 && projectedUV.x <= 1.0,
        projectedUV.y >= 0.0 && projectedUV.y <= 1.0
    );

    if(all(inside))
    {
        projectedLight = texture(uTexture, projectedUV);
    }

    // =========================================================
    // GOD RAYS
    // =========================================================

    vec2 rayUV = vec2(
        vTexCoord.x,
        vTexCoord.y + uYOffset
    );

    vec4 rayColor = vec4(0.0);

    vec2 delta = vec2(0.0, -1.0);
    delta.x *= (1.0 - uVerticalBias);

    vec2 step = delta * uDensity;

    float invSamples = 1.0 / float(uSamples);

float column =
    floor(vTexCoord.x * uScreenSize.x / pixelSize);

float columnNoise =
    fract(sin(column * 127.1) * 43758.5453);

float phaseOffset =
    columnNoise * 6.2831853;

// stronger temporal + spatial variation
float wave =
    sin(uTime * 5.0 + phaseOffset + vTexCoord.y * 10.0);

// convert wave (-1..1) into stronger intensity modulation
float shimmer = 0.90 + 0.10 * wave;

// optional micro flicker for energy feel
float microFlicker =
    0.92 + 0.08 * sin(uTime * 12.0 + columnNoise * 20.0);

float flicker = shimmer * microFlicker;

    float decayBase = uDecay;

    // Pixel snap size in UV space
    vec2 pixelUV = pixelSize / uScreenSize;

    for(int i = 0; i < uSamples; i++)
    {
        rayUV += step * uTexelSizeY;

        // =====================================================
        // PIXEL PERFECT SNAP
        // =====================================================

        vec2 snappedUV =
            floor(rayUV / pixelUV) * pixelUV;

        vec4 sampleColor =
            texture(uTexture, snappedUV);

        float t = float(i) * invSamples;

        float sourceStrength =
            pow(1.0 - t, 2.5);

        float decayStrength =
            pow(decayBase, float(i));

        rayColor +=
            sampleColor *
            sourceStrength *
            decayStrength;
    }

    // =========================================================
    // COMBINE
    // =========================================================

    vec4 finalColor = projectedLight * 0.45;

    float fade =
        1.0 - pow(snappedUV.y, 0.7);

    vec3 rays =
        rayColor.rgb *
        uIntensity *
        0.12 *
        flicker;

    // =========================================================
// AMBIENT TINTING (STRONGER + MORE DIRECT)
// =========================================================

// Base ambient influence (much stronger than before)
float ambientBlend = uAmbientIntensity;

// Preserve ray brightness
float luminance =
    dot(rays, vec3(0.299, 0.587, 0.114));

// Convert ambient into a directional colour influence
vec3 ambientColor =
    uAmbientColor * (0.6 + ambientBlend);

// Stronger colour shift (NOT just normalised direction)
vec3 tinted =
    rays * ambientColor;

// Blend between original rays and tinted rays
rays = mix(
    rays,
    tinted,
    ambientBlend * 0.9
);

    finalColor.rgb += rays;

    finalColor.a *= fade;

    fragColor = finalColor;
}