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
    // Read buffers
    vec4 scene = texture(uScene, vUV);
    vec4 emissive = texture(uEmissive, vUV);

    // Convert UV -> screen pixel coordinates (Y-down)
    vec2 fragPos = vec2(vUV.x * uScreenSize.x, (1.0 - vUV.y) * uScreenSize.y);

    float alpha = scene.a;

    // Start with ambient light, scaled by alpha
    vec3 lighting = uAmbientColor * uAmbientIntensity * alpha;

    // Apply dynamic lights (alpha-weighted)
    for (int i = 0; i < uNumLights; i++) {
        Light L = uLights[i];
        vec2 diff = L.position - fragPos;
        float dist = length(diff);
        if (dist < L.radius) {
            float falloff = 1.0 - (dist / L.radius);
            lighting += L.color * falloff * L.intensity * alpha;
        }
    }

    // Multiply base color by lighting
    vec3 litScene = scene.rgb * lighting;

    // Add emissive contribution (scaled by sprite alpha)
    vec3 litEmissive = emissive.rgb * alpha;

    // Output final color with original alpha
    FragColor = vec4(litScene + litEmissive, alpha);
}