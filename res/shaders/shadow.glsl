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

uniform sampler2D uTextures[1];

out vec4 FragColor;

void main() 
{
    vec4 texColor = texture(uTextures[0], vTexCoord);

    // discard fully transparent pixels (important for clean silhouettes)
    if (texColor.a < 0.1)
        discard;

    // convert ANY sprite to solid mask
    // (ignore original colour completely)
    float mask = texColor.a;

    FragColor = vec4(vec3(mask), 1.0);
}