package com.example.autoconfigure.brave;

import java.util.Set;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.actuate.autoconfigure.opentelemetry.OpenTelemetryProperties;
import org.springframework.boot.actuate.autoconfigure.tracing.zipkin.ZipkinAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.Encoding;
import zipkin2.reporter.otel.brave.InstrumentationScope;
import zipkin2.reporter.otel.brave.OtlpProtoV1Encoder;
import zipkin2.reporter.otel.brave.TagToAttributes;

import static zipkin2.reporter.otel.brave.TagToAttribute.stringAttribute;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(ZipkinAutoConfiguration.class)
@EnableConfigurationProperties(OpenTelemetryProperties.class)
public class BraveOtelAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public OtlpProtoV1Encoder otlpProtoV1Encoder(OpenTelemetryProperties properties) {
		Set<String> httpMethod = Set.of("GET", "HEAD", "POST", "PUT", "DELETE", "CONNECT", "TRACE", "OPTIONS", "PATCH");
		return OtlpProtoV1Encoder.newBuilder()
			.resourceAttributes(properties.getResourceAttributes())
			.instrumentationScope(new InstrumentationScope("org.springframework.boot", SpringBootVersion.getVersion()))
			.tagToAttributes(TagToAttributes.newBuilder().withDefaults().tagToAttribute("method", (builder, value) -> {
				if (httpMethod.contains(value)) {
					builder.addAttributes(stringAttribute("http.request.method", value));
				}
				else {
					builder.addAttributes(stringAttribute("method", value));
				}
			}).tagToAttribute("status", "http.response.status_code").build())
			.build();
	}

	@Bean
	public Encoding otlpEncoding() {
		return Encoding.PROTO3;
	}

}
