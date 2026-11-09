package br.com.trackflow.order.service.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TrackingCodeGeneratorTest {

    private final TrackingCodeGenerator generator = new TrackingCodeGenerator();

    @Test
    void shouldGenerateTrackingCodeWithExpectedFormat() {

        String trackingCode = generator.generate();

        assertEquals(15, trackingCode.length());
        assertTrue(trackingCode.matches("TF-[A-Z0-9]{12}"));
    }

    @Test
    void shouldGenerateDifferentTrackingCodes() {

        String firstCode = generator.generate();
        String secondCode = generator.generate();

        assertTrue(!firstCode.equals(secondCode));
    }
}