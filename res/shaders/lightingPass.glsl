#type vertex
#version 330 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aUV;

out vec2 vUV;

void main()
{
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
uniform sampler2D uShadowCaster;

uniform vec2 uScreenSize;

uniform vec3 uAmbientColor;
uniform float uAmbientIntensity;

uniform int uNumLights;

struct Light
{
    vec2 position;
    vec3 color;
    float radius;
    float intensity;
};

uniform Light uLights[64];

uniform bool uOcclusionEnabled;
uniform bool uShadowsEnabled;

const int MAX_SHADOW_STEPS = 140;

void main()
{
    // =========================================================
    // BASE SCENE
    // =========================================================

    vec4 scene = texture(uScene, vUV);

    vec3 baseColor = scene.rgb;
    float alpha = scene.a;

    if(alpha <= 0.001)
        discard;

    // =========================================================
    // OCCLUSION EARLY OUT
    // =========================================================

    float occlusion = 1.0;

    if(uOcclusionEnabled)
        occlusion = texture(uOcclusion, vUV).r;

    // Fully occluded pixels skip ALL lighting/shadow work
    if(occlusion <= 0.001)
    {
        vec3 emissive = texture(uEmissive, vUV).rgb;

        FragColor = vec4(emissive, alpha);
        return;
    }

    // =========================================================
    // PIXEL SNAP
    // =========================================================

    float pixelSize = 6.0;

    vec2 fragPos = vec2(
        vUV.x * uScreenSize.x,
        (1.0 - vUV.y) * uScreenSize.y
    );

    fragPos = floor(fragPos / pixelSize) * pixelSize;

    // Cached reciprocal screen size
    vec2 invScreenSize = 1.0 / uScreenSize;

    // =========================================================
    // INITIAL LIGHTING
    // =========================================================

    vec3 lighting = uAmbientColor * uAmbientIntensity;

    // =========================================================
    // LIGHT LOOP
    // =========================================================

    for(int i = 0; i < uNumLights; i++)
    {
        Light L = uLights[i];

        vec2 toFrag = fragPos - L.position;

        // -----------------------------------------------------
        // DISTANCE SQUARED CHECK
        // -----------------------------------------------------

        float distSq = dot(toFrag, toFrag);
        float radiusSq = L.radius * L.radius;

        if(distSq >= radiusSq)
            continue;

        // Only sqrt if inside radius
        float dist = sqrt(distSq);

        // Prevent divide by zero
        if(dist <= 0.001)
            continue;

        // -----------------------------------------------------
        // FALLOFF
        // -----------------------------------------------------

        float falloff = 1.0 - (dist / L.radius);

        // Skip tiny contributions
        if(falloff < 0.02)
            continue;

        // -----------------------------------------------------
        // DIRECTION
        // -----------------------------------------------------

        vec2 dir = toFrag / dist;

        // -----------------------------------------------------
        // SHADOWS
        // -----------------------------------------------------

        float visibility = 1.0;

        if(uShadowsEnabled)
        {
            float stepSize = pixelSize;

            bool hit = false;
            float hitDistance = 0.0;

            // Integer loop (faster)
            int steps = int(dist / stepSize);
            steps = min(steps, MAX_SHADOW_STEPS);

            vec2 samplePos = L.position;

            for(int s = 0; s < steps; s++)
            {
                float t = float(s) * stepSize;

                samplePos += dir * stepSize;

                vec2 uv = vec2(
                    samplePos.x * invScreenSize.x,
                    1.0 - (samplePos.y * invScreenSize.y)
                );

                float mask = texture(uShadowCaster, uv).r;

                if(mask > 0.1)
                {
                    hit = true;
                    hitDistance = t;
                    break;
                }
            }

            // -------------------------------------------------
            // FINITE SHADOW LENGTH
            // -------------------------------------------------

            if(hit)
            {
                float shadowLength = 300.0;

                float behindDist = dist - hitDistance;

                if(behindDist < shadowLength)
                {
                    float fade = 1.0 - (behindDist / shadowLength);

                    visibility = 1.0 - fade;
                }
            }
        }

        // -----------------------------------------------------
        // LIGHT CONTRIBUTION
        // -----------------------------------------------------

        vec3 lightContribution =
            L.color *
            falloff *
            L.intensity;

        lighting += lightContribution * visibility;
    }

    // =========================================================
    // FINAL LIGHTING
    // =========================================================

    vec3 litScene = baseColor * lighting * occlusion;

    // =========================================================
    // EMISSIVE
    // =========================================================

    vec3 emissive = texture(uEmissive, vUV).rgb;

    litScene += emissive;

    FragColor = vec4(litScene, alpha);
}