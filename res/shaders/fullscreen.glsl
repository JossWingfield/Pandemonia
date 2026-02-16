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

uniform int uNumShadowCasters;
uniform float uShadowCasterStrength[MAX_SHADOW_CASTERS];
uniform vec2 uShadowCasterLeft[MAX_SHADOW_CASTERS];
uniform vec2 uShadowCasterRight[MAX_SHADOW_CASTERS];
uniform float uShadowLengthMultiplier;

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

        // ---------- Shadow Casters ----------
        for (int s = 0; s < uNumShadowCasters; s++)
        {
            vec2 left  = uShadowCasterLeft[s];
            vec2 right = uShadowCasterRight[s];

            // Project endpoints away from light
            vec2 edgeMid = (left + right) * 0.5;

			vec2 dirL = normalize(left  - L.position);
			vec2 dirR = normalize(right - L.position);
			
			float projectionLength = (L.radius - length(edgeMid - L.position)) * uShadowLengthMultiplier;
			projectionLength = max(projectionLength, 1.0);

            vec2 farLeft  = left  + dirL * projectionLength;
            vec2 farRight = right + dirR * projectionLength;

            // Ensure correct winding (counter-clockwise)
            // Quad: left → right → farRight → farLeft
            bool inside =
                edgeTest(left, right, fragPos)     >= 0.0 &&
                edgeTest(right, farRight, fragPos) >= 0.0 &&
                edgeTest(farRight, farLeft, fragPos) >= 0.0 &&
                edgeTest(farLeft, left, fragPos)   >= 0.0;

if (inside)
{
    // Compute center of near and far edges
    vec2 nearCenter = (left + right) * 0.5;
    vec2 farCenter  = (farLeft + farRight) * 0.5;

    vec2 shadowAxis = normalize(farCenter - nearCenter);

    // Distance along true quad axis
    float forwardDist = dot(fragPos - nearCenter, shadowAxis);

    float fade = 1.0 - (forwardDist / projectionLength);
    fade = clamp(fade, 0.0, 1.0);

    lighting -= lightContribution * fade * uShadowCasterStrength[s];
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