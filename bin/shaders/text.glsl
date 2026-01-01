#type vertex
#version 330 core

layout(location = 0) in vec2 aPos;       // pixel space
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in float aTexId;
layout(location = 3) in vec4 aColor;

uniform mat4 u_Ortho;   // screen-space ortho

out vec2 vTexCoord;
out vec4 vColor;

void main() {
    // Flip V for font atlas
    vTexCoord = vec2(aTexCoord.x, 1.0 - aTexCoord.y);
    vColor = aColor;

    gl_Position = u_Ortho * vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 330 core

in vec4 vColor;
in vec2 vTexCoord;
in float vTexId;

uniform sampler2D uTextures[1];

out vec4 FragColor;

void main() {
    float alpha = texture(uTextures[0], vTexCoord).r;
    FragColor = vec4(vColor.rgb, vColor.a * alpha);
}