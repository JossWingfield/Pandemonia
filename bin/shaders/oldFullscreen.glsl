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

uniform vec2 uScreenSize;

uniform vec3 uAmbientColor;
uniform float uAmbientIntensity;

uniform int uNumLights;

struct Light {
    vec2 position;   // SCREEN SPACE (pixels)
    vec3 color;
    float radius;
    float intensity;
};

uniform Light uLights[64];

void main() {
    vec4 scene = texture(uScene, vUV);
    vec3 baseColor = scene.rgb;
    float alpha = scene.a;

    // Early out for fully transparent pixels (optional optimization)
    if (alpha <= 0.001) {
        discard;
    }

    vec2 fragPos = vec2(
        vUV.x * uScreenSize.x,
        (1.0 - vUV.y) * uScreenSize.y
    );

    // Ambient light (NO alpha here)
    vec3 lighting = uAmbientColor * uAmbientIntensity;

    // Dynamic lights
    for (int i = 0; i < uNumLights; i++) {
        Light L = uLights[i];
        vec2 diff = L.position - fragPos;
        float dist = length(diff);

        if (dist < L.radius) {
            float falloff = 1.0 - (dist / L.radius);
            lighting += L.color * falloff * L.intensity;
        }
    }

    // Apply lighting to color
    vec3 litScene = baseColor * lighting;

    // Emissive adds light, NOT alpha-scaled
    vec3 emissive = texture(uEmissive, vUV).rgb;
    litScene += emissive;

    // Preserve original alpha
    FragColor = vec4(litScene, alpha);
}