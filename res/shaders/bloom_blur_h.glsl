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

uniform sampler2D uTexture;
uniform float uTexelWidth;

out vec4 FragColor;

void main() {
    float offset = uTexelWidth;

    vec3 result = texture(uTexture, vTexCoord).rgb * 0.227027;
    result += texture(uTexture, vTexCoord + vec2(offset, 0.0)).rgb * 0.316216;
    result += texture(uTexture, vTexCoord - vec2(offset, 0.0)).rgb * 0.316216;
    result += texture(uTexture, vTexCoord + vec2(2.0 * offset, 0.0)).rgb * 0.070270;
    result += texture(uTexture, vTexCoord - vec2(2.0 * offset, 0.0)).rgb * 0.070270;

    FragColor = vec4(result, 1.0);
}