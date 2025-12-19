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

void main() {
    // Read buffers
    vec4 scene = texture(uScene, vUV);
    vec4 emissive = texture(uEmissive, vUV);

    // Add emissive on top of scene
    vec3 color = scene.rgb;

    // Preserve scene alpha
    FragColor = vec4(color, scene.a);
}