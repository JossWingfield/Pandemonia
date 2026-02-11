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
uniform vec2 uShadowCasterPos[MAX_SHADOW_CASTERS];
uniform vec2 uShadowCasterSize[MAX_SHADOW_CASTERS];
uniform float uShadowCasterStrength[MAX_SHADOW_CASTERS];

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

    // -------- Dynamic Lights --------
    for (int i = 0; i < uNumLights; i++) {

    Light L = uLights[i];

    vec2 lightToFrag = fragPos - L.position;
    float fragDist = length(lightToFrag);

    if (fragDist < L.radius) {

        float falloff = 1.0 - (fragDist / L.radius);
        vec3 lightContribution = L.color * falloff * L.intensity;

        // --- Add Light ---
        lighting += lightContribution;

		if (uShadowsEnabled) {
	        for (int s = 0; s < uNumShadowCasters; s++) {
	
			    vec2 casterPos = uShadowCasterPos[s];
			    vec2 casterSize = uShadowCasterSize[s];
			    float casterStrength = uShadowCasterStrength[s];
			
			    float casterDist = length(casterPos - L.position);
			
			    // Only cast if inside light
			    if (casterDist < L.radius * 3.0) {
			
			        vec2 shadowDir = normalize(casterPos - L.position);
			        vec2 fragToCaster = fragPos - casterPos;
			
			        float angle = atan(shadowDir.y, shadowDir.x);
			        float cosA = cos(angle);
			        float sinA = sin(angle);
			
			        vec2 shadowSpace = vec2(
			            fragToCaster.x * cosA + fragToCaster.y * sinA,
			            -fragToCaster.x * sinA + fragToCaster.y * cosA
			        );
			
			        if (shadowSpace.x > 0.0) {
			
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
    }
}
    
    

    // -------- Occlusion with smooth edges --------
    float occlusion = 1.0;
    if (uOcclusionEnabled) {
        // first sample the occlusion texture
        float occ = texture(uOcclusion, vUV).r;


        // you can tweak these numbers for softer/harder edges
        occlusion = smoothstep(0.0, 1.0, occ);
    }

    vec3 litScene = baseColor * lighting * occlusion;

    // Emissive
    vec3 emissive = texture(uEmissive, vUV).rgb;
    litScene += emissive;

    FragColor = vec4(litScene, alpha);
}