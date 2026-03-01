package com.viora.streamingandvideo.common.utils;

import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileHandlerUtils {

    private static final String FILE_DEFAULT_PATH =
            "/Users/volodymyr.dobrianskyi/IdeaProjects/microservices-viora/Viora/movies/uploads/Spidet-man-copy.mp4";
    private static final String RESOURCE_DEFAULT_PATH =
            "/Users/volodymyr.dobrianskyi/IdeaProjects/microservices-viora/Viora/movies/tests/";

    @SneakyThrows
    public static Resource createRangeResource(Path filePath, long rangeStart, long rangeLength) {
        RandomAccessFile fileReader = new RandomAccessFile(filePath.toFile(), "r");
        fileReader.seek(rangeStart);
        InputStream partialContentStream = new InputStream() {
            private long bytesRead = 0;

            @SneakyThrows
            @Override
            public int read() {
                if (bytesRead >= rangeLength) {
                    fileReader.close();
                    return -1;
                }
                bytesRead++;
                return fileReader.read();
            }

            @SneakyThrows
            @Override
            public int read(byte[] buffer, int offset, int length) {
                if (bytesRead >= rangeLength) {
                    fileReader.close();
                    return -1;
                }
                long remainingBytes = rangeLength - bytesRead;
                int bytesToRead = (int) Math.min(length, remainingBytes);
                int bytesRead = fileReader.read(buffer, offset, bytesToRead);
                if (bytesRead > 0) {
                    this.bytesRead += bytesRead;
                }
                if (this.bytesRead >= rangeLength) {
                    fileReader.close();
                }
                return bytesRead;
            }

            @SneakyThrows
            @Override
            public void close() {
                fileReader.close();
            }
        };
        return new InputStreamResource(partialContentStream) {
            @Override
            public long contentLength() {
                return rangeLength;
            }
        };
    }

    @SneakyThrows
    public static void main(String[] args) {
        Path filePath = Path.of(FILE_DEFAULT_PATH);
        long fileSize = Files.size(filePath) - 1000000000L;
        Resource partialMovie = createRangeResource(filePath, 0, fileSize);
        saveResource(partialMovie);
    }

    @SneakyThrows
    private static void saveResource(Resource resource) {
        String savedMoviePath = RESOURCE_DEFAULT_PATH + "2.mp4";
        Path path = Paths.get(savedMoviePath);

        // Ensure parent directory exists
        Files.createDirectories(path.getParent());

        try (InputStream is = resource.getInputStream()) {
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
