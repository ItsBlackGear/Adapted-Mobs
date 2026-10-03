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

uniform float GameTime;
uniform vec3 FacadeOrigin;
uniform mat3 FacadeInverseTransform;

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

float Hash(in vec3 p) {
//    p = mod(p, scale);
    return fract(sin(dot(p, vec3(127.1, 311.7, 74.7))) * 43758.5453);
//    return fract(sin(dot(vec2(p), vec2(27.16898, 38.90563))) * 5151.5473453);
}

vec2 vertexJitter(vec3 p, float time) {
    float phaseX = Hash(p);
    float phaseZ = Hash(p + vec3(19.19, 73.13, 41.71));

    float freqX = 0.7 + Hash(p + 13.7) * 0.5;
    float freqZ = 0.7 + Hash(p + 31.2) * 0.5;

    return vec2(
        sin(time * freqX + phaseX * 6.28318),
        sin(time * freqZ + phaseZ * 6.28318)
    );
}

void main() {
    vec3 pos = Position + ChunkOffset;
    // TODO: apply some variance to this so that it isnt the same for every mindmould
    vec3 RawPosition = FacadeInverseTransform * (Position - FacadeOrigin);
    RawPosition = round(RawPosition * 1024.0) / 1024.0; // reduces jitter with floating point imprecision
    pos.xz += vertexJitter(RawPosition, GameTime * 1000.) * 0.025;
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    vertexDistance = fog_distance(pos, FogShape);
    overlayColor = texelFetch(Sampler1, UV1, 0);
    vec3 lightmap = minecraft_sample_lightmap(Sampler2, UV2).rgb;

    vesselTexCoord = Normal.xy * 0.5 + 0.5;
    facadeTexCoord = UV0;

    vesselLighting = vec3(Color.a) * lightmap;
    facadeLighting = Color.rgb * lightmap;
}
