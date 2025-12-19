#type vertex
#version 330 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in float aTexId;
layout(location = 3) in vec4 aColor;

uniform mat4 u_MVP;

out vec2 vTexCoord;
out vec4 vColor;
out float vTexId;
out vec2 vWorldPos;

void main() {
    vTexCoord = aTexCoord;
    vColor = aColor;
    vTexId = aTexId;

    vec4 worldPos = vec4(aPos, 0.0, 1.0);
    vWorldPos = worldPos.xy;

    gl_Position = u_MVP * worldPos;
}
#type fragment
#version 330 core

in vec4 vColor;
in vec2 vTexCoord;
in float vTexId;

out vec4 color;

uniform sampler2D uTextures[1];
uniform sampler2D uEmissive;
uniform vec2 uScreenSize;
uniform float uPixelScale;

uniform int uNumLights;
struct Light {
    vec2 position;
    vec3 color;
    float radius;
    float intensity;
};
uniform Light uLights[64];

uniform vec3 uAmbientColor;
uniform float uAmbientIntensity;

void main() {
    vec4 baseCol = vColor * texture(uTextures[0], vTexCoord);
    vec4 emisCol = texture(uEmissive, vTexCoord);

    bool isEmissive = emisCol.a > 0.0;

    vec3 result;

    // --- PRECOMPUTED AMBIENT ---
    if (isEmissive) {
        // emissive ignores ambient
        result = baseCol.rgb;
    } else {
        // ambient applied to base
        vec3 ambientMul = uAmbientColor * uAmbientIntensity;
        result = baseCol.rgb * ambientMul;
    }

    // Pixel snapping
    vec2 fragScreen = vec2(gl_FragCoord.x, uScreenSize.y - gl_FragCoord.y);
    fragScreen = floor(fragScreen / uPixelScale) * uPixelScale + uPixelScale * 0.5;

    // Lighting loop (unchanged)
    for (int i = 0; i < uNumLights; i++) {
        Light L = uLights[i];
        vec2 diff = L.position - fragScreen;
        float dist2 = dot(diff, diff);
        float scaledRadius = L.radius;
		float r2 = scaledRadius * scaledRadius;

		// Use the old CPU formula
		float attenuation = 1.0 - (dist2 / r2);
		attenuation = clamp(attenuation, 0.0, 1.0);

		vec3 add = baseCol.rgb * (L.color * L.intensity) * attenuation;
		result += add;
    }

    color = vec4(clamp(result, 0.0, 1.0), baseCol.a);
}