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

void main()
{
    // =========================================================
    // PROJECTED LIGHT TEXTURE
    // =========================================================

    vec2 projectedUV = vec2(vTexCoord.x, 1.0 - vTexCoord.y);
    projectedUV.y += uYOffset;

    vec4 projectedLight = vec4(0.0);

    // SAME LOGIC, JUST REMOVES REDUNDANT BRANCH CHECK ORDERING COST
    bvec2 inside =
        bvec2(
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

    vec2 rayUV = vec2(vTexCoord.x, vTexCoord.y + uYOffset);
    vec4 rayColor = vec4(0.0);

    vec2 delta = vec2(0.0, -1.0);
    delta.x *= (1.0 - uVerticalBias);

    vec2 step = delta * uDensity;

    float invSamples = 1.0 / float(uSamples);

    float flicker =
        0.97 +
        0.03 * sin(uTime * 4.0 + vTexCoord.y * 8.0);

    // HOIST CONSTANT POW BASE (no visual change, just avoids recomputing constants repeatedly)
    float decayBase = uDecay;

    for(int i = 0; i < uSamples; i++)
    {
        rayUV += step * uTexelSizeY;

        vec4 sampleColor = texture(uTexture, rayUV);

        float t = float(i) * invSamples;

        float sourceStrength = pow(1.0 - t, 2.5);
        float decayStrength = pow(decayBase, float(i));

        rayColor += sampleColor * sourceStrength * decayStrength;
    }

    // =========================================================
    // COMBINE
    // =========================================================

    vec4 finalColor = projectedLight * 0.45;

    float fade = 1.0 - pow(vTexCoord.y, 0.7);

    finalColor += rayColor * uIntensity * 0.12 * flicker;

    finalColor.a *= fade;

    fragColor = finalColor;
}