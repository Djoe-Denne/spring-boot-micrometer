package io.github.djoe.micrometer.springboot;

import io.github.djoe.micrometer.springboot.configuration.MicrometerAspectConfiguration;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MicrometerAspectConfiguration.class)
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .bannerMode(Mode.OFF)
                .run(args);
    }
}
