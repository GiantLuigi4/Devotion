#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform float Time;
uniform vec2 Frequency;
uniform vec2 WobbleAmount;

out vec4 fragColor;

void main() {
    float speed = Time * 3.1415926535 * 2.0;
    float xOffset = sin(texCoord.y * Frequency.x + speed) * WobbleAmount.x;
    float yOffset = cos(texCoord.x * Frequency.y + speed) * WobbleAmount.y;
    vec2 offset = vec2(xOffset, yOffset);

    fragColor = texture(DiffuseSampler, texCoord + offset);
}