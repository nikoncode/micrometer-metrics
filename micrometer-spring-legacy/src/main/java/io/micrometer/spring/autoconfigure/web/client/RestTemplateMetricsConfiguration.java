/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.spring.autoconfigure.web.client;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.spring.autoconfigure.MetricsProperties;
import io.micrometer.spring.web.client.DefaultRestTemplateExchangeTagsProvider;
import io.micrometer.spring.web.client.MetricsRestTemplateCustomizer;
import io.micrometer.spring.web.client.RestTemplateExchangeTagsProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for {@link RestTemplate}-related metrics.
 *
 * @author Jon Schneider
 * @author Phillip Webb
 */
@Configuration
@ConditionalOnClass(name = {
    "org.springframework.web.client.RestTemplate",
    "org.springframework.boot.web.client.RestTemplateCustomizer"
})
public class RestTemplateMetricsConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplateExchangeTagsProvider.class)
    public DefaultRestTemplateExchangeTagsProvider restTemplateTagConfigurer() {
        return new DefaultRestTemplateExchangeTagsProvider();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.boot.web.client.RestTemplateCustomizer")
    public MetricsRestTemplateCustomizer metricsRestTemplateCustomizer(
        MeterRegistry meterRegistry,
        RestTemplateExchangeTagsProvider restTemplateTagConfigurer,
        MetricsProperties properties) {
        return new MetricsRestTemplateCustomizer(meterRegistry, restTemplateTagConfigurer,
            properties.getWeb().getClient().getRequestsMetricName());
    }

}
