#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform float DevotionTime;
uniform vec2 DevotionFrequency;
uniform vec2 DevotionWobbleAmount;

out vec4 fragColor;

void main() {
    float xOffset = sin(texCoord.y * DevotionFrequency.x + DevotionTime * 3.1415926535 * 2.0) * DevotionWobbleAmount.x;
    float yOffset = cos(texCoord.x * DevotionFrequency.y + DevotionTime * 3.1415926535 * 2.0) * DevotionWobbleAmount.y;
    vec2 offset = vec2(xOffset, yOffset);

    fragColor = texture(DiffuseSampler, texCoord + offset);
}