#type vertex
#version 330 core

layout(location = 0) in vec2 aPos;       // pixel space
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in float aTexId;
layout(location = 3) in vec4 aColor;

uniform mat4 u_Ortho;   // GUI orthographic projection

out vec2 vTexCoord;
out vec4 vColor;
out float vTexId;

void main() {
    vTexCoord = aTexCoord;
    vColor = aColor;
    vTexId = aTexId;

    gl_Position = u_Ortho * vec4(aPos, 0.0, 1.0);
}
#type fragment
#version 330 core

in vec4 vColor;
in vec2 vTexCoord;
in float vTexId;

out vec4 outColor;

uniform sampler2D uTextures[1];
uniform sampler2D uEmissive;

void main() {
    vec4 texCol = texture(uTextures[0], vTexCoord);
vec3 rgb = texCol.rgb * texCol.a * vColor.rgb; // account for tex alpha
float a = texCol.a * vColor.a;
outColor = vec4(rgb, a);
}