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

uniform sampler2D uLightingTexture;
uniform sampler2D uOcclusion;

uniform vec2 uScreenSize;

uniform float uTime;

uniform vec3 uAmbientColor;
uniform float uAmbientIntensity;

uniform bool uOcclusionEnabled;

uniform int uNumLights;

struct Light
{
    vec2 position;
    vec3 color;
    float radius;
    float intensity;
};

uniform Light uLights[64];

// ----------------------------------------------------
// HASH
// ----------------------------------------------------

float hash21(vec2 p)
{
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);

    return fract(p.x * p.y);
}

// ----------------------------------------------------
// DUST
// ----------------------------------------------------

float dustMote(vec2 fragPos, float time)
{
    float dust = 0.0;

    const int PARTICLE_COUNT = 50;

    float scale = 3.0;

    for(int i = 0; i < PARTICLE_COUNT; i++)
    {
        float particleOffset =
            hash21(vec2(i, 9.0)) * 5.0;

        float localTime =
            time + particleOffset;

        float minY =
            uScreenSize.y * 0.2;

        float maxY =
            uScreenSize.y * 0.8;

        vec2 spawn = vec2(
            hash21(vec2(i, 1.0)) * uScreenSize.x,

            mix(
                minY,
                maxY,
                hash21(vec2(i, 2.0))
            )
        );

        // ---------------- OCCLUSION ----------------

        if(uOcclusionEnabled)
        {
            float occ =
                texture(
                    uOcclusion,
                    spawn / uScreenSize
                ).r;

            if(occ < 0.1)
                continue;
        }

        // ---------------- MOVEMENT ----------------

        float speedY =
            3.0 +
            hash21(vec2(i, 3.0)) * 8.0;

        spawn.y =
            mod(
                spawn.y -
                localTime * speedY -
                minY,

                maxY - minY
            ) + minY;

        float sideSpeed =
            (hash21(vec2(i, 4.0)) - 0.5)
            * 40.0;

        spawn.x +=
            sin(
                localTime * 0.8 +
                hash21(vec2(i, 5.0)) * 20.0
            ) * sideSpeed;

        // ---------------- PIXEL SNAP ----------------

        spawn =
            floor(spawn / scale) * scale;

        // ---------------- PARTICLE ----------------

        vec2 diff =
            fragPos - spawn;

        if(abs(diff.x) < scale &&
           abs(diff.y) < scale)
        {
            float life =
                sin(
                    localTime *
                    (1.5 +
                    hash21(vec2(i, 6.0)))

                    +

                    hash21(vec2(i, 7.0))
                    * 10.0
                ) * 0.5 + 0.5;

            life =
                smoothstep(0.2, 0.8, life);

            dust += life;
        }
    }

    return clamp(dust, 0.0, 1.0);
}

// ----------------------------------------------------
// MAIN
// ----------------------------------------------------

void main()
{
    vec2 fragPos = vec2(
        vUV.x * uScreenSize.x,
        (1.0 - vUV.y) * uScreenSize.y
    );

    // ------------------------------------------------
    // LIGHT DETECTION
    // ------------------------------------------------

    float directLight = 0.0;

    for(int i = 0; i < uNumLights; i++)
    {
        Light L = uLights[i];

        float dist =
            length(fragPos - L.position);

        if(dist < L.radius)
        {
            float falloff =
                1.0 - (dist / L.radius);

            directLight +=
                falloff * L.intensity;
        }
    }

    directLight =
        clamp(directLight, 0.0, 1.0);

    // ------------------------------------------------
    // LIGHTING TEXTURE BRIGHTNESS
    // ------------------------------------------------

    vec3 lighting =
        texture(uLightingTexture, vUV).rgb;

    float brightness =
        max(
            lighting.r,
            max(lighting.g, lighting.b)
        );

    brightness =
        clamp(
            brightness - uAmbientIntensity,
            0.0,
            1.0
        );

    // ------------------------------------------------
    // DUST
    // ------------------------------------------------

    float dust =
        dustMote(fragPos, uTime);

    float lightMask =
        smoothstep(0.05, 0.2, brightness);

    dust *= lightMask;

    // ------------------------------------------------
    // OUTPUT
    // ------------------------------------------------

    vec3 dustColor =
        vec3(1.0, 0.95, 0.85);

    vec3 finalDust =
        dustColor *
        dust *
        0.5;

    FragColor =
        vec4(finalDust, dust);
}