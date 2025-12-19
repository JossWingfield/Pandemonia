#type vertex
#version 330 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in float aTexId;
layout(location = 3) in vec4 aColor;

uniform mat4 u_MVP;

out vec2 vTexCoord;
out vec4 vColor;

void main() {
    vTexCoord = aTexCoord;
    vColor = aColor;

    gl_Position = u_MVP * vec4(aPos, 0.0, 1.0);
}
#type fragment
#version 330 core

in vec2 vTexCoord;
in vec4 vColor;

uniform sampler2D uTextures[1];

out vec4 FragColor;

void main() {
    vec4 texColor = texture(uTextures[0], vTexCoord);

    // Premultiply RGB by alpha
    vec3 rgb = texColor.rgb * texColor.a * vColor.rgb;
    float alpha = texColor.a * vColor.a;

    FragColor = vec4(rgb, alpha);
}