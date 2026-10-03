#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler3;

uniform vec4 ColorModulator;

uniform float GameTime;
uniform vec2 FacadeTopLeft;
uniform vec2 FacadeBottomRight;

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

float radial(vec2 pos, float radius)
{
    float result = length(pos)-radius;
    result = fract(result * 1.0);
    float result2 = 1.0 - result;
    float fresult = result * result2;
    fresult = pow((fresult*5.5),10.0);
//     fresult = clamp(0.0, 1.0, fresult);
    return fresult;
}

void main() {
    vec4 vesselColor = texture(Sampler0, vesselTexCoord) * vec4(vesselLighting, 1.0);
    float time = GameTime * 2000.;

    // distortion
    vec2 shiftedBottomRight = (FacadeBottomRight - FacadeTopLeft);
    vec2 normalizedTexCoord = (facadeTexCoord - FacadeTopLeft) / shiftedBottomRight;

    float pixelScaleConsistent = 16.;
    vec2 pixelScale = shiftedBottomRight / pixelScaleConsistent;

    vec2 effectUV = normalizedTexCoord;

    // pixel snapping
//    vec2 pixelCenter = floor(effectUV * pixelScaleConsistent) + 0.5;
//    effectUV = pixelCenter / pixelScaleConsistent;
    vec2 originalUV = effectUV;

    vec2 centeredUV = originalUV - vec2(.5, .5);
    float intensity = (0.5 + sin(time) * 0.5) * cos(time / 100.);
    effectUV.x += (radial(centeredUV + vec2(0., 5.), time / 10.) * pixelScale.x) * intensity;
    effectUV.y -= (radial(centeredUV + vec2(0., 5.), time / 10.) * pixelScale.y) * intensity;

    // facade transform
    float facadeTime = FacadeAmount;
    effectUV.x -= (sin((originalUV.y * pixelScaleConsistent * 2.) + time * 10.) / (pixelScaleConsistent / 2.)) * (1. - facadeTime);

//    effectUV.y += cos((originalUV.x * 16.) + time)
//        * (pixelScale.y * pixelScaleConsistent * 1.);

//    effectUV.x -= sin(time + (originalUV.x - .5) / (1. + sin(time) / 2.) * 4.) / (32.);
//    effectUV.y += radial(originalUV - vec2(.5, 0.), (time / 25.)) * pixelScale.y;
//    effectUV.x += radial(originalUV - vec2(.5, 0.), cos(time / 25.)) * pixelScale.x;

    // clamp
//    effectUV = (effectUV * shiftedBottomRight) + FacadeTopLeft;
//    effectUV = clamp(effectUV, FacadeTopLeft, FacadeBottomRight - pixelScale);

    // wrap
    effectUV = mod(effectUV, 1.) * shiftedBottomRight + FacadeTopLeft;

    vec4 facadeColor = texture(Sampler3, effectUV) * vec4(facadeLighting, 1.0);
    vec4 color = mix(vesselColor, facadeColor, facadeTime);

    if (color.a < 0.1) discard;

    color *= ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
