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
out vec2 vWorldPos;

void main() {
    vTexCoord = aTexCoord;
    vColor = aColor;
    vTexId = aTexId;

    vec4 worldPos = vec4(aPos, 0.0, 1.0);
    vWorldPos = worldPos.xy;

    gl_Position = u_MVP * worldPos;
}
#type fragment
#version 330 core

in vec4 vColor;
in vec2 vTexCoord;
in float vTexId;
in vec2 vWorldPos;

out vec4 color;

uniform sampler2D uTextures[1];
uniform sampler2D uEmissive;

uniform vec2 uScreenSize;
uniform float uPixelScale;

uniform int uNumLights;
struct Light {
    vec2 position;
    vec2 positionWorld;
    vec3 color;
    float radius;
    float intensity;
};
uniform Light uLights[64];

uniform vec3 uAmbientColor;
uniform float uAmbientIntensity;

uniform float uBloomThreshold;
uniform float uBloomIntensity;

uniform vec2 uTileSize;        // size of a tile in world units (you upload 48,48)
uniform vec2 uMapSize;         // map width/height in tiles (you upload mapW,mapH)
uniform vec2 uPlayerPos;       // player world position
uniform int uLightSteps;       // e.g. 64

// ======= uniform flat tile passability array =======
// must be >= mapW * mapH. Increase if your map is bigger than 128x128
#define MAX_MAP_TILES 384
uniform float uLightPassFlat[MAX_MAP_TILES];

//
// helper: read passability for a world-space position (via tile coords)
//
// returns 1.0 for passable, 0.0 for blocking
//
float readPassableFromTileCoord(vec2 tileCoord) {
    // floor to get tile index (tileCoord is in tiles; e.g. tile 0..mapW-1)
    int tx = int(floor(tileCoord.x));
    int ty = int(floor(tileCoord.y));

    // clamp to valid tile range (outside map -> treated as blocking)
    int mapW = int(uMapSize.x);
    int mapH = int(uMapSize.y);

    if (tx < 0 || ty < 0 || tx >= mapW || ty >= mapH) return 0.0;

    int idx = ty * mapW + tx;
    // safety: ensure inside the compiled array bounds
    if (idx < 0 || idx >= MAX_MAP_TILES) return 0.0;
    return uLightPassFlat[idx];
}

void main() {
    vec4 baseCol = vColor * texture(uTextures[0], vTexCoord);
    vec4 emisCol = texture(uEmissive, vTexCoord);
    bool isEmissive = emisCol.a > 0.0;

    // --- Ambient base ---
    vec3 result = isEmissive ? baseCol.rgb : baseCol.rgb * uAmbientColor * uAmbientIntensity;

    // --- Pixel-snapping for screen-space effects ---
    // fragScreen is in screen pixels (origin top-left because we invert y)
    vec2 fragScreen = vec2(gl_FragCoord.x, uScreenSize.y - gl_FragCoord.y);
    fragScreen = floor(fragScreen / uPixelScale) * uPixelScale + uPixelScale * 0.5;

    // --- Player LOS (ray march between fragment world pos -> player world pos) ---
    float playerVisibility = 1.0;
    vec2 startPos = vWorldPos;        // fragment world pos
    vec2 endPos   = uPlayerPos;       // player world pos

    vec2 rayDir = endPos - startPos;
    float rayLen = length(rayDir);

    if (rayLen > 0.0) {
        rayDir /= rayLen; // normalize
        
        float stepSize = rayLen / float(uLightSteps);
        float t = 0.0;

        for (int s = 0; s < uLightSteps; s++) {
            vec2 samplePos = startPos + rayDir * t;

            // convert world → tile space
            vec2 tileCoord = samplePos / uTileSize;

            // read tile passability
            float passable = readPassableFromTileCoord(tileCoord);

            if (passable < 0.5) {
                playerVisibility = 0.0;
                break;
            }

            t += stepSize;
        }
    }


    // --- Lighting loop (no per-light LOS; only player visibility gates contribution) ---
    for (int i = 0; i < uNumLights; i++) {
    Light L = uLights[i];

    // --- Compute LOS to this light ---
    float lightVisibility = 1.0;
    vec2 startPos = vWorldPos; // fragment world pos
    vec2 endPos   = L.positionWorld; // light world pos

    vec2 rayDir = endPos - startPos;
    float rayLen = length(rayDir);

    if (rayLen > 0.0) {
        rayDir /= rayLen; // normalize
        float stepSize = rayLen / float(uLightSteps);
        float t = 0.0;

        for (int s = 0; s < uLightSteps; s++) {
            vec2 samplePos = startPos + rayDir * t;

            // convert world → tile space
            vec2 tileCoord = samplePos / uTileSize;

            // read tile passability
            float passable = readPassableFromTileCoord(tileCoord);

            if (passable < 0.5) {
                lightVisibility = 0.0;
                break;
            }

            t += stepSize;
        }
    }

    // --- Lighting calculation (same as working shader) ---
    vec2 diff = L.position - fragScreen; // use screen-snapped position
    float dist2 = dot(diff, diff);
    float r2 = L.radius * L.radius;
    float attenuation = clamp(1.0 - (dist2 / r2), 0.0, 1.0);

    // modulate by LOS
    vec3 add = L.color * L.intensity * attenuation * lightVisibility;
    result += add * baseCol.rgb;
}

    // --- Bloom (cheap bright-pass + 3x3 box blur approximate) ---
    float brightness = max(max(result.r, result.g), result.b);
    vec3 bloomCol = (brightness > uBloomThreshold || isEmissive) ? result : vec3(0.0);

    vec2 texel = 1.0 / uScreenSize;
    vec3 blur = bloomCol;
    for (int sx = -1; sx <= 1; sx++) {
        for (int sy = -1; sy <= 1; sy++) {
            if (sx == 0 && sy == 0) continue;
            vec2 off = vec2(float(sx), float(sy)) * texel;
            vec3 sample = texture(uTextures[0], vTexCoord + off).rgb;
            blur += (max(max(sample.r, sample.g), sample.b) > uBloomThreshold) ? sample : vec3(0.0);
        }
    }
    blur /= 9.0;

    result += blur * uBloomIntensity;

    color = vec4(clamp(result, 0.0, 1.0), baseCol.a);
}