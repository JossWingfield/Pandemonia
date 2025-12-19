#type vertex
#version 330 core
layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aTexCoord;

out vec2 vTexCoord;

void main() {
    vTexCoord = aTexCoord;
    gl_Position = vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 330 core
in vec2 vTexCoord;

uniform sampler2D uScene;
uniform sampler2D uBloom;
uniform float uBloomIntensity; // NEW: controls bloom strength

out vec4 FragColor;

void main() {
    vec3 sceneColor = texture(uScene, vTexCoord).rgb;
    vec3 bloomColor = texture(uBloom, vTexCoord).rgb;

    vec3 result = sceneColor + bloomColor * uBloomIntensity; // scale bloom
    FragColor = vec4(result, 1.0);
}