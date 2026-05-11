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
uniform sampler2D uShadowCaster;

uniform vec2 uScreenSize;

uniform vec3 uAmbientColor;
uniform float uAmbientIntensity;

uniform int uNumLights;

struct Light {
    vec2 position;
    vec3 color;
    float radius;
    float intensity;
};

uniform Light uLights[64];

uniform bool uOcclusionEnabled;
uniform bool uShadowsEnabled;

vec2 toUV(vec2 p)
{
    return vec2(
        p.x / uScreenSize.x,
        1.0 - (p.y / uScreenSize.y)
    );
}

void main()
{
    vec4 scene = texture(uScene, vUV);

    vec3 baseColor = scene.rgb;
    float alpha = scene.a;

    if(alpha <= 0.001)
        discard;

    // ---------------- PIXEL SNAP ----------------
    float pixelSize = 6.0;

    vec2 fragPos = vec2(
        vUV.x * uScreenSize.x,
        (1.0 - vUV.y) * uScreenSize.y
    );

    fragPos = floor(fragPos / pixelSize) * pixelSize;

    vec3 lighting = uAmbientColor * uAmbientIntensity;

    // =========================================================
    // LIGHT LOOP
    // =========================================================

    for(int i = 0; i < uNumLights; i++)
{
    Light L = uLights[i];

    vec2 toFrag = fragPos - L.position;
    float dist = length(toFrag);

    if(dist >= L.radius)
        continue;

    vec2 dir = toFrag / dist;

    float shadow = 1.0;

    if(uShadowsEnabled)
    {
        float stepSize = pixelSize;

        vec2 samplePos = L.position;

        bool hit = false;
        float hitDistance = 0.0;

        for(float t = 0.0; t < dist; t += stepSize)
        {
            samplePos += dir * stepSize;

            vec2 uv = toUV(samplePos);

            float mask = texture(uShadowCaster, uv).r;

            if(mask > 0.1)
            {
                hit = true;
                hitDistance = t;
                break;
            }
        }

        // finite shadow length
        if(hit)
        {
            float shadowLength = 300.0;

            // how far fragment is behind blocker
            float behindDist = dist - hitDistance;

            if(behindDist < shadowLength)
            {
                // smooth fade
                float fade = 1.0 - (behindDist / shadowLength);

                shadow = 1.0 - fade;
            }
        }
    }

    float falloff = 1.0 - (dist / L.radius);

    vec3 lightContribution =
        L.color *
        falloff *
        L.intensity;

    lighting += lightContribution * shadow;
}

    // =========================================================
    // OCCLUSION
    // =========================================================

    float occlusion = 1.0;

    if(uOcclusionEnabled)
        occlusion = texture(uOcclusion, vUV).r;

    vec3 litScene = baseColor * lighting * occlusion;

    // =========================================================
    // EMISSIVE
    // =========================================================

    vec3 emissive = texture(uEmissive, vUV).rgb;
    litScene += emissive;

    FragColor = vec4(litScene, alpha);
}
