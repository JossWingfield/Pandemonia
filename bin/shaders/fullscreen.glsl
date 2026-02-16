#type vertex
#version 330 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aUV;

out vec2 vUV;

void main() {
    vUV = aUV;
    gl_Position = vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 330 core

in vec2 vUV;
out vec4 FragColor;

uniform sampler2D uScene;
uniform sampler2D uEmissive;
uniform sampler2D uOcclusion;

uniform vec2 uScreenSize;

uniform vec3 uAmbientColor;
uniform float uAmbientIntensity;

uniform int uNumLights;

#define MAX_SHADOW_CASTERS 64

// Building shadows (quad method)
uniform int uNumBuildingCasters;
uniform vec2 uBuildingCasterLeft[MAX_SHADOW_CASTERS];
uniform vec2 uBuildingCasterRight[MAX_SHADOW_CASTERS];
uniform float uBuildingCasterStrength[MAX_SHADOW_CASTERS];
uniform float uBuildingShadowLengthMultiplier;

// Player shadows (old method)
uniform int uNumPlayerCasters;
uniform vec2 uPlayerCasterPos[MAX_SHADOW_CASTERS];
uniform vec2 uPlayerCasterSize[MAX_SHADOW_CASTERS];
uniform float uPlayerCasterStrength[MAX_SHADOW_CASTERS];

struct Light {
    vec2 position;
    vec3 color;
    float radius;
    float intensity;
};

uniform Light uLights[64];

// New boolean uniform to toggle occlusion
uniform bool uOcclusionEnabled;
uniform bool uShadowsEnabled;

float edgeTest(vec2 a, vec2 b, vec2 p)
{
    vec2 edge = b - a;
    vec2 toPoint = p - a;
    return edge.x * toPoint.y - edge.y * toPoint.x;
}

void main()
{
    vec4 scene = texture(uScene, vUV);
    vec3 baseColor = scene.rgb;
    float alpha = scene.a;

    if (alpha <= 0.001)
        discard;

    vec2 fragPos = vec2(
        vUV.x * uScreenSize.x,
        (1.0 - vUV.y) * uScreenSize.y
    );

    // -------- Ambient --------
    vec3 lighting = uAmbientColor * uAmbientIntensity;

    // -------- Lights --------
    for (int i = 0; i < uNumLights; i++)
    {
        Light L = uLights[i];

        vec2 lightToFrag = fragPos - L.position;
        float fragDist = length(lightToFrag);

        if (fragDist >= L.radius)
            continue;

        float falloff = 1.0 - (fragDist / L.radius);
        vec3 lightContribution = L.color * falloff * L.intensity;

        lighting += lightContribution;

        if (!uShadowsEnabled)
            continue;

        for (int s = 0; s < uNumBuildingCasters; s++)
{
    vec2 left  = uBuildingCasterLeft[s];
    vec2 right = uBuildingCasterRight[s];

    vec2 edgeMid = (left + right) * 0.5;

    vec2 dirL = normalize(left - L.position);
    vec2 dirR = normalize(right - L.position);

    float projectionLength = (L.radius - length(edgeMid - L.position)) * uBuildingShadowLengthMultiplier;
    projectionLength = max(projectionLength, 1.0);

    vec2 farLeft  = left  + dirL * projectionLength;
    vec2 farRight = right + dirR * projectionLength;

    bool inside =
        edgeTest(left, right, fragPos)     >= 0.0 &&
        edgeTest(right, farRight, fragPos) >= 0.0 &&
        edgeTest(farRight, farLeft, fragPos) >= 0.0 &&
        edgeTest(farLeft, left, fragPos)   >= 0.0;

    if (inside)
    {
        vec2 nearCenter = (left + right) * 0.5;
        vec2 farCenter  = (farLeft + farRight) * 0.5;

        vec2 shadowAxis = normalize(farCenter - nearCenter);
        float forwardDist = dot(fragPos - nearCenter, shadowAxis);

        float fade = 1.0 - (forwardDist / projectionLength);
        fade = clamp(fade, 0.0, 1.0);

        lighting -= lightContribution * fade * uBuildingCasterStrength[s];
    }
}

// --- Player Shadows (old method) ---
for (int s = 0; s < uNumPlayerCasters; s++)
{
    vec2 casterPos = uPlayerCasterPos[s];
    vec2 casterSize = uPlayerCasterSize[s];
    float casterStrength = uPlayerCasterStrength[s];

    float casterDist = length(casterPos - L.position);

    if (casterDist < L.radius * 3.0)
    {
        vec2 shadowDir = normalize(casterPos - L.position);
        vec2 fragToCaster = fragPos - casterPos;

        float angle = atan(shadowDir.y, shadowDir.x);
        float cosA = cos(angle);
        float sinA = sin(angle);

        vec2 shadowSpace = vec2(
            fragToCaster.x * cosA + fragToCaster.y * sinA,
            -fragToCaster.x * sinA + fragToCaster.y * cosA
        );

        if (shadowSpace.x > 0.0)
        {
            float dynamicLength = casterSize.x * (1.0 + (L.radius - casterDist) / L.radius);

            float sx = shadowSpace.x / dynamicLength;
            float sy = shadowSpace.y / casterSize.y;

            float shadowMask = 1.0 - length(vec2(sx, sy));
            shadowMask = clamp(shadowMask, 0.0, 1.0);
            shadowMask = smoothstep(0.0, 1.0, shadowMask);

            lighting -= lightContribution * shadowMask * casterStrength;
        }
    }
}
    }

    // -------- Occlusion --------
    float occlusion = 1.0;
    if (uOcclusionEnabled)
    {
        float occ = texture(uOcclusion, vUV).r;
        occlusion = smoothstep(0.0, 1.0, occ);
    }

    vec3 litScene = baseColor * lighting * occlusion;

    // -------- Emissive --------
    vec3 emissive = texture(uEmissive, vUV).rgb;
    litScene += emissive;

    FragColor = vec4(litScene, alpha);
}