package com.example.autoconfigure.brave;

import io.opentelemetry.proto.trace.v1.Span;
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
import zipkin2.reporter.otel.brave.TagToAttribute;
import zipkin2.reporter.otel.brave.TagToAttributes;

import static zipkin2.reporter.otel.brave.TagToAttribute.stringAttribute;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(ZipkinAutoConfiguration.class)
@EnableConfigurationProperties(OpenTelemetryProperties.class)
public class BraveOtelAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public OtlpProtoV1Encoder otlpProtoV1Encoder(OpenTelemetryProperties properties) {
		return OtlpProtoV1Encoder.newBuilder()
			.resourceAttributes(properties.getResourceAttributes())
			.instrumentationScope(new InstrumentationScope("org.springframework.boot", SpringBootVersion.getVersion()))
			.tagToAttributes(TagToAttributes.newBuilder()
				.withDefaults()
				.tagToAttribute("method", "http.request.method")
				.tagToAttribute("status", "http.response.status_code")
				.build())
			.build();
	}

	@Bean
	public Encoding otlpEncoding() {
		return Encoding.PROTO3;
	}

}
