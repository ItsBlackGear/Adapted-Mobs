#version 150

#moj_import <light.glsl>
#moj_import <fog.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler1;
uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 ChunkOffset;
uniform int FogShape;

out float vertexDistance;
out vec4 overlayColor;
out vec2 vesselTexCoord;
out vec2 facadeTexCoord;
out vec3 vesselLighting;
out vec3 facadeLighting;

float Hash(in float p, in float scale) {
    p = mod(p, scale);
    return fract(sin(dot(vec2(p), vec2(27.16898, 38.90563))) * 5151.5473453);
}

float noise(in float p, in float scale ) {
    float f;
    p *= scale;

    f = fract(p);
    p = floor(p);

    f = f*f*(3.0-2.0*f);

    float res = mix(mix(Hash(p, scale),
    Hash(p + 1.0, scale), f),
    mix(Hash(p, scale),
    Hash(p + 1.0, scale), f), f);
    return res;
}

vec3 sampleJitter(in float timeStep) {
    return vec3(
    noise(timeStep * 0.37, 64.0), 0.0,
    noise(timeStep * 0.71 + 43.0, 64.0)
    ) * 2.0 - 1.0;
}

void main() {
    vec3 pos = Position + ChunkOffset;
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    vertexDistance = fog_distance(pos, FogShape);
    overlayColor = texelFetch(Sampler1, UV1, 0);
    vec3 lightmap = minecraft_sample_lightmap(Sampler2, UV2).rgb;

    vesselTexCoord = Normal.xy * 0.5 + 0.5;
    facadeTexCoord = UV0;
    vesselLighting = vec3(Color.a) * lightmap;
    facadeLighting = Color.rgb * lightmap;
}
