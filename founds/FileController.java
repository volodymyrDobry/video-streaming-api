package com.viora.streamingandvideo.api.controller;

import com.viora.streamingandvideo.common.utils.FileHandlerUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private static final String FILE_DEFAULT_PATH = "movies/uploads/Spidet-man-copy.mp4";
    private static final String RESOURCE_DEFAULT_PATH = "movies/test/";
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @CrossOrigin(origins = "http://localhost:63342",
            allowedHeaders = {"Authorization", "Range", "Content-Type"},
            methods = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD})
    @SneakyThrows
    @GetMapping("/streamFile")
    public ResponseEntity<Resource> streamFile(
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader) {
        Path filePath = Path.of(FILE_DEFAULT_PATH);
        long fileSize = Files.size(filePath);
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        if (rangeHeader == null || rangeHeader.isEmpty()) {
            Resource resource = FileHandlerUtils.createRangeResource(filePath, 0, fileSize);
            log.info("Class name of the video: {}", resource.getClass()
                    .getName());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(fileSize)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .body(resource);
        }
        String[] ranges = rangeHeader.replace("bytes=", "")
                .split("-");
        long rangeStart = Long.parseLong(ranges[0]);
        long rangeEnd = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : fileSize - 1;
        if (rangeStart < 0 || rangeEnd < 0 || rangeStart > rangeEnd || rangeStart > fileSize - 1 || rangeStart > rangeEnd) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                    .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                    .body(null);
        }

        long rangeLength = rangeEnd - rangeStart + 1;
        Resource resource = FileHandlerUtils.createRangeResource(filePath, rangeStart, rangeLength);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(rangeLength)
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd)
                .header(HttpHeaders.CONTENT_RANGE, "bytes")
                .body(resource);
    }


    @SneakyThrows
    private void saveResource(Resource resource) {
        InputStreamSource inputStreamSource = new InputStreamResource(resource);
        String savedMoviePath = RESOURCE_DEFAULT_PATH + "2" + ".mp4";
        Path uploadPath =
                Files.createDirectories(
                        Paths.get(savedMoviePath));
        Files.copy(inputStreamSource.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
    }
}
