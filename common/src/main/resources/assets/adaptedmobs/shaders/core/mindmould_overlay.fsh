#version 150

uniform sampler2D Sampler0;

uniform float GameTime;
uniform vec4 ColorModulator;

in vec2 texCoord0;

out vec4 fragColor;

#define pi 3.14
void main() {
    vec2 UV = texCoord0;

    float intensity = 50.;
    float time = (GameTime * 1000.) * .75;
    UV.x += sin((time) + UV.y * intensity) / intensity * .5;
    time *= 1.75;
    UV.y += sin((time) + UV.x * intensity) / intensity * .5;

    vec4 color = vec4(0., 0., 0., 1.) * (1. - sin(texCoord0.x * pi) * sin(texCoord0.y * pi));
    color.a = mix(color.a / .5, 0., sin(texCoord0.x * pi) * sin(texCoord0.y * pi)) / 2.5;

    vec4 overlay = texture(Sampler0, UV);
    color += overlay * overlay.a;

//    UV.y += sin((time) + UV.x * intensity) / intensity;
//    color += texture(Sampler0, UV) / 2.;


//    if (color.a <= 0.1) discard;

    fragColor = color * ColorModulator;
}