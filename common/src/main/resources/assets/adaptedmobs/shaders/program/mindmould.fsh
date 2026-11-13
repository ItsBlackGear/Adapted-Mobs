#version 150
#define pi 3.14

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

uniform float GameTime;
uniform float DesaturateAmount;

out vec4 fragColor;

float Hash(in float p, in float scale) {
    p = mod(p, scale);
    return fract(sin(dot(vec2(p), vec2(27.16898, 38.90563))) * 5151.5473453);
}

vec2 drop(vec2 uv, vec2 pos, float r)
{
    pos.y = fract(pos.y);
    return (uv - pos) * exp(-pow(20.0 * length(uv - pos), 2.0));
}

float noise(in float p, in float scale ) {
    float f;
    p *= scale;

    f = fract(p);
    p = floor(p);

    f = f*f*(3.0-2.0*f);

    float res = mix(
        mix(Hash(p, scale), Hash(p + 1.0, scale), f),
        mix(Hash(p, scale), Hash(p + 1.0, scale), f), f
    );
    return res;
}

// https://www.shadertoy.com/view/lsdXDH
vec4 desaturate(vec4 color) {
    float bw = (min(color.r, min(color.g, color.b)) + max(color.r, max(color.g, color.b))) * 0.5;
    return vec4(bw, bw, bw, color.a);
}

// https://www.shadertoy.com/view/Xt2XDy
void main(){
    vec2 UV = vec2(texCoord) - vec2(.5);
    float intensity = 50.;
    float time = (GameTime * 1000.) * .75;

    vec2 res = vec2(textureSize(DiffuseSampler, 0));
    vec2 uv2 = vec2(texCoord.x * res.x / res.y, texCoord.y);

    vec2 d = vec2(0.0, 0.0);
    const int n = 100;
    for(int i = 0; i < n; i++)
    {
        float fi = float(i) * 4.0;
        vec4 r = vec4(
            Hash(fi, 1000.),
            Hash(fi + 1.0, 1000.),
            Hash(fi + 2.0, 1000.),
            Hash(fi + 3.0, 1000.)
        );
        vec2 pos = r.xy;
        pos.x *= res.x / res.y;
        pos.y += 10.0 * time * 0.02 * r.a;
        //pos.x += sin(t + r.z);
        d += 0.1 * drop(uv2.xy, pos, 0.03);
    }

//    UV.x /= .5 + sin(time) / 2.;
//    UV.y /= .5 + sin(time) / 2.;
//    UV += mix(radial(UV - .5, time), 0., sin(UV.x * pi));
//    time *= 1.75;

//    UV.y += sin((time) + UV.x * intensity) / intensity * .5;
    vec4 diffuseColor = texture(DiffuseSampler, texCoord + d);
    vec4 desaturatedColor = desaturate(diffuseColor);

    float scale = 2.5;
    float compositeSaturation = DesaturateAmount;
    compositeSaturation += sin(((texCoord + .5) / (d + .5)).x * scale)
        * cos(((texCoord + .5) / (d + .5)).y * scale) * DesaturateAmount;

    desaturatedColor.b = mix(desaturatedColor.b, 0., 1. - compositeSaturation);
    vec4 outColor = mix(diffuseColor, desaturatedColor, compositeSaturation);
    outColor = mix(outColor, desaturate(diffuseColor), DesaturateAmount * .8);

    fragColor = vec4(outColor.rgb, 1.0);
}