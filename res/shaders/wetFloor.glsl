#type vertex
#version 330 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in float aTexId;
layout(location = 3) in vec4 aColor;

uniform mat4 u_MVP;

out vec2 vTexCoord;
out vec4 vColor;

void main()
{
    vTexCoord = aTexCoord;
    vColor = aColor;

    gl_Position = u_MVP * vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 330 core

in vec2 vTexCoord;
in vec4 vColor;

uniform sampler2D uTextures[1];

uniform float uTime;
uniform vec2 uWorldPos;

out vec4 FragColor;

void main()
{
    // =====================================================
    // BASE TEXTURE
    // =====================================================

    vec4 texColor =
        texture(uTextures[0], vTexCoord);

    texColor.rgb *= texColor.a;

    // =====================================================
    // WORLD UV
    // =====================================================

    vec2 worldUV =
        (uWorldPos + vTexCoord * 16.0) / 64.0;

    // =====================================================
    // WET DARKENING
    // =====================================================

    texColor.rgb *= 0.84;

    // =====================================================
    // LARGE SCALE SHEEN
    // =====================================================

    float sheen =
        sin(worldUV.y * 8.0 - uTime * 1.2);

    sheen *=
        sin(worldUV.x * 5.0);

    sheen = sheen * 0.5 + 0.5;

    // thinner highlights
    sheen = pow(sheen, 7.0);

    // reflective brightness
    texColor.rgb += sheen * 0.12;

    // =====================================================
    // SOFT WET PATCH VARIATION
    // =====================================================

    float patches =
        sin(worldUV.x * 2.5) *
        sin(worldUV.y * 2.0);

    patches = patches * 0.5 + 0.5;

    patches = smoothstep(
        0.4,
        0.9,
        patches
    );

    // slightly glossier areas
    texColor.rgb += patches * 0.04;

    // =====================================================
    // FINAL
    // =====================================================

    FragColor = texColor * vColor;
}