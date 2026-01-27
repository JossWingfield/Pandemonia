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

void main() {
    vTexCoord = aTexCoord;
    vColor = aColor;
    vTexId = aTexId;
    gl_Position = u_MVP * vec4(aPos, 0.0, 1.0);
}
#type fragment
#version 330 core

in vec2 vTexCoord;
in vec4 vColor;

uniform sampler2D uTextures[1];

/*
    Base skin palette (hardcoded or set once)
    These MUST match the exact colors in your sprite sheet
*/
uniform vec3 u_SkinFrom[3]; // light, mid, dark

/*
    Player-selected skin palette
*/
uniform vec3 u_SkinTo[3];

out vec4 FragColor;

bool approxEqual(vec3 a, vec3 b) {
    return distance(a, b) < 0.01;
}

void main() {
    vec4 texColor = texture(uTextures[0], vTexCoord);

    // Palette swap ONLY on RGB, outline colors won't match so stay untouched
    vec3 rgb = texColor.rgb;

    for (int i = 0; i < 3; i++) {
        if (approxEqual(rgb, u_SkinFrom[i])) {
            rgb = u_SkinTo[i];
            break;
        }
    }

    texColor.rgb = rgb;

    // Premultiply alpha
    texColor.rgb *= texColor.a;

    // Vertex color multiply (batching safe)
    FragColor = texColor * vColor;
}