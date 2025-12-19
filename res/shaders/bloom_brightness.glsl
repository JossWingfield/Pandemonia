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
uniform float uThreshold;

out vec4 FragColor;

void main() {
    vec3 color = texture(uScene, vTexCoord).rgb;
    float brightness = max(max(color.r, color.g), color.b);

    if (brightness > uThreshold)
        FragColor = vec4(color, 1.0);
    else
        FragColor = vec4(0.0);
}