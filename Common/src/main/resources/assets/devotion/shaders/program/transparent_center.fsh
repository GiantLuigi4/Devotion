#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

in vec2 texCoord;
in vec2 oneTexel;
in vec4 vPosition;

uniform float Radius;
uniform float DevotionTransStepGranularity;
uniform vec3 CameraPosition;
uniform vec3 Center;
uniform mat4 InverseTransformMatrix;
uniform ivec4 ViewPort;

out vec4 fragColor;

vec4 CalcEyeFromWindow(in float depth) {
    vec3 ndcPos;
    ndcPos.xy = ((2.0 * gl_FragCoord.xy) - (2.0 * ViewPort.xy)) / (ViewPort.zw) - 1;
    ndcPos.z = (2.0 * depth - gl_DepthRange.near - gl_DepthRange.far) / (gl_DepthRange.far - gl_DepthRange.near);
    vec4 clipPos = vec4(ndcPos, 1.);
    vec4 homogeneous = InverseTransformMatrix * clipPos;
    vec4 eyePos = vec4(homogeneous.xyz / homogeneous.w, homogeneous.w);
    return eyePos;
}

// TODO I cannot, for the life of me, remember what i did last time to accidentally change the thickness of the bands
void main(){
    vec4 center = texture(DiffuseSampler, texCoord);
    float distanceToTransparency = 1.0;

    if(center.a == 0) {
        fragColor = vec4(0);
        return;
    }

    // TODO figure out how to implement all this correctly
    vec3 ndc = vPosition.xyz / vPosition.w;
    vec2 viewportCoord = ndc.xy * 0.5 + 0.5;
    float sceneDepth = texture(DepthSampler, viewportCoord).x;
    vec3 pixelPosition = CalcEyeFromWindow(sceneDepth).xyz + CameraPosition;
    float d = distance(pixelPosition, Center);
    float radius = Radius; // Radius / d;
    float step = max(1.0, ceil(radius / DevotionTransStepGranularity));

    for(float u = 0.0; u <= radius; u += step) {
        for(float v = 0.0; v <= radius; v += step) {
            float distance = sqrt(u * u + v * v) / radius;

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
