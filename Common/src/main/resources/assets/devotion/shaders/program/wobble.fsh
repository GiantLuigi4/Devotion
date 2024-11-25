#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform float STime;
uniform vec2 Frequency;
uniform vec2 WobbleAmount;

out vec4 fragColor;

void main() {
    float xOffset = sin(texCoord.y * Frequency.x + STime * 3.1415926535 * 2.0) * WobbleAmount.x;
    float yOffset = cos(texCoord.x * Frequency.y + STime * 3.1415926535 * 2.0) * WobbleAmount.y;
    vec2 offset = vec2(xOffset, yOffset);

    fragColor = texture(DiffuseSampler, texCoord + offset);
}