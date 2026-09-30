#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler3;

uniform vec4 ColorModulator;
uniform float FacadeAmount;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 overlayColor;
in vec2 vesselTexCoord;
in vec2 facadeTexCoord;
in vec3 vesselLighting;
in vec3 facadeLighting;

out vec4 fragColor;

void main() {
    vec4 vesselColor = texture(Sampler0, vesselTexCoord) * vec4(vesselLighting, 1.0);
    vec4 facadeColor = texture(Sampler3, facadeTexCoord) * vec4(facadeLighting, 1.0);
    vec4 color = mix(vesselColor, facadeColor, FacadeAmount);
    if (color.a < 0.1) discard;

    color *= ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
