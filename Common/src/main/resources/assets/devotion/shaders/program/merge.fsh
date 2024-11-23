#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D AuraSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
	fragColor = texture(DiffuseSampler, texCoord);
	vec4 auraColor = texture(AuraSampler, texCoord);

    fragColor = mix(fragColor, auraColor, auraColor.a);
}