package com.cravecart;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

@Component
public class FrontendProcessRunner {

    private final String frontendDir;
    private final String frontendCommand;
    private final ExecutorService outputExecutor = Executors.newCachedThreadPool();
    private Process frontendProcess;

    public FrontendProcessRunner(
            @Value("${frontend.dir:frontend}") String frontendDir,
            @Value("${frontend.command:set PORT=3000 && npm run dev}") String frontendCommand
    ) {
        this.frontendDir = frontendDir;
        this.frontendCommand = frontendCommand;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startFrontend() {
        try {
            if (frontendProcess != null && frontendProcess.isAlive()) {
                return;
            }

            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", frontendCommand);
            builder.directory(new File(frontendDir));
            builder.redirectErrorStream(true);

            frontendProcess = builder.start();
            consumeStream(frontendProcess.getInputStream());
        } catch (IOException e) {
            System.err.println("Failed to start frontend process: " + e.getMessage());
        }
    }

    private void consumeStream(InputStream stream) {
        outputExecutor.submit(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[frontend] " + line);
                }
            } catch (IOException e) {
                System.err.println("Error reading frontend output: " + e.getMessage());
            }
        });
    }

    @PreDestroy
    public void stopFrontend() {
        if (frontendProcess != null && frontendProcess.isAlive()) {
            frontendProcess.destroy();
            try {
                frontendProcess.waitFor(5, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            if (frontendProcess.isAlive()) {
                frontendProcess.destroyForcibly();
            }
        }
        outputExecutor.shutdownNow();
    }
}
