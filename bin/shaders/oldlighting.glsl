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
uniform bool uLightingEnabled;
uniform bool uBloomEnabled;

uniform int uNumLights;
struct Light {
    vec2 position;
    vec3 color;
    float radius;
    float radiusSquared;
    float intensity;
};
uniform Light uLights[64];

uniform vec3 uAmbientColor;
uniform float uAmbientIntensity;

uniform float uBloomThreshold;   // e.g., 0.8
uniform float uBloomIntensity;   // e.g., 1.0

void main() {
    vec4 baseCol = vColor * texture(uTextures[0], vTexCoord);
    vec4 emisCol = texture(uEmissive, vTexCoord);
    
    if (!uLightingEnabled) {
    	color = baseCol;
    	return;
	}

    bool isEmissive = emisCol.a > 0.0;

    vec3 result;

    // --- PRECOMPUTED AMBIENT ---
    if (isEmissive) {
        // emissive ignores ambient
        result = baseCol.rgb;
    } else {
        vec3 ambientMul = uAmbientColor * uAmbientIntensity;
        result = baseCol.rgb * ambientMul;
    }

    // Pixel snapping
    vec2 fragScreen = vec2(gl_FragCoord.x, uScreenSize.y - gl_FragCoord.y);
    fragScreen = floor(fragScreen / uPixelScale) * uPixelScale + uPixelScale * 0.5;

    // Lighting loop
    for (int i = 0; i < uNumLights; i++) {
        Light L = uLights[i];
        vec2 diff = L.position - fragScreen;
        float dist2 = dot(diff, diff);
        if (dist2 > L.radiusSquared)
        	continue;
        float attenuation = clamp(1.0 - (dist2 / L.radiusSquared), 0.0, 1.0);
        vec3 add = baseCol.rgb * (L.color * L.intensity) * attenuation;
        result += add;
    }

    // --- BLOOM ---
    if (uBloomEnabled) {
    	// bright-pass threshold
	    float brightness = max(max(result.r, result.g), result.b);
	    vec3 bloomCol = (brightness > uBloomThreshold || isEmissive) ? result : vec3(0.0);
	
	    // simple 3x3 box blur (cheap approximation)
	    vec2 texel = 1.0 / uScreenSize;
	    vec3 blur = bloomCol;
	    for (int x = -1; x <= 1; x++) {
	        for (int y = -1; y <= 1; y++) {
	            if (x == 0 && y == 0) continue;
	            vec2 offset = vec2(x, y) * texel;
	            vec3 sample = texture(uTextures[0], vTexCoord + offset).rgb;
	            blur += (max(max(sample.r, sample.g), sample.b) > uBloomThreshold) ? sample : vec3(0.0);
	        }
	    }
	    blur /= 9.0;
		result += blur * uBloomIntensity;
	}

    color = vec4(clamp(result, 0.0, 1.0), baseCol.a);
}