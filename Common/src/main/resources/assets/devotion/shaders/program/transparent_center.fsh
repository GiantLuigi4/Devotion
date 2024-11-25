#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float Radius;
uniform float DevotionTransStepGranularity;

out vec4 fragColor;

void main(){
    vec4 center = texture(DiffuseSampler, texCoord);
    float distanceToTransparency = 1.0;

    if(center.a == 0) {
        fragColor = vec4(0);
        return;
    }

    float step = max(1.0, ceil(Radius / DevotionTransStepGranularity));

    for(float u = 0.0; u <= Radius; u += step) {
        for(float v = 0.0; v <= Radius; v += step) {
            float distance = sqrt(u * u + v * v) / (Radius);

            if(distance < distanceToTransparency) {
                float s0 = texture(DiffuseSampler, texCoord + vec2(-u * oneTexel.x, -v * oneTexel.y)).a;
                float s1 = texture(DiffuseSampler, texCoord + vec2(u * oneTexel.x, v * oneTexel.y)).a;
                float s2 = texture(DiffuseSampler, texCoord + vec2(-u * oneTexel.x, v * oneTexel.y)).a;
                float s3 = texture(DiffuseSampler, texCoord + vec2(u * oneTexel.x, -v * oneTexel.y)).a;

                if(s0 <= 0 || s1 <= 0 || s2 <= 0 || s3 <= 0) {
                    distanceToTransparency = distance;
                }
            }
        }
    }

    distanceToTransparency = min(abs(distanceToTransparency), 0.9);

    fragColor = vec4(vec3(center), center.a * (1 - distanceToTransparency));
}