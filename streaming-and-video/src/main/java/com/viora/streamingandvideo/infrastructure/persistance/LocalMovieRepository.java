package com.viora.streamingandvideo.infrastructure.persistance;

import com.viora.streamingandvideo.domain.model.Movie;
import com.viora.streamingandvideo.domain.model.MovieDetails;
import com.viora.streamingandvideo.domain.ports.out.MovieRepository;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Repository
public class LocalMovieRepository implements MovieRepository {

    private static final String ROOT_UPLOADS_FOLDER_PATH = "../movies/uploads/";
    private static final String ROOT_SEGMENTS_FOLDER_PATH = "../movies/segments/";
    private static final int MINIMAL_SEGMENT_ID_LENGTH = 3;

    @SneakyThrows
    @Override
    public Movie saveMovie(MovieDetails movieDetails, MultipartFile movie) {
        String imdbId = movieDetails.imdbId();
        String filename = movie.getOriginalFilename() == null ? "movie.mp4" : movie.getOriginalFilename();
        Path uploadDir = Files.createDirectories(Paths.get(ROOT_UPLOADS_FOLDER_PATH, imdbId));
        Path uploadPath = uploadDir.resolve(filename);
        Files.copy(movie.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        Path outputSegmentPath = Path.of(ROOT_SEGMENTS_FOLDER_PATH, imdbId);
        segmentVideo(uploadPath, outputSegmentPath);

        Resource movieResource = getMoviePlayback(imdbId);
        return Movie.createMovie(imdbId, movieResource);
    }

    @Override
    @SneakyThrows
    public Resource getMoviePlayback(String movieId) {
        Path playlistPath = Paths.get(ROOT_SEGMENTS_FOLDER_PATH, movieId, "playlist.m3u8");
        if (!Files.exists(playlistPath)) {
            throw new IllegalArgumentException("Playlist not found for movie " + movieId);
        }
        return new FileSystemResource(playlistPath);
    }

    @Override
    @SneakyThrows
    public Resource getMovieSegment(String movieId, Long segment) {
        String segmentStringId = String.format("segment_%s.ts", getSegmentStringId(segment));
        Path segmentPath = Paths.get(ROOT_SEGMENTS_FOLDER_PATH, movieId, segmentStringId);
        if (!Files.exists(segmentPath)) {
            throw new IllegalArgumentException("Segment " + segment + " not found for movie " + movieId);
        }
        return new FileSystemResource(segmentPath);
    }

    private String getSegmentStringId(Long segment) {
        if (segment >= 100) {
            return segment.toString();
        }

        StringBuilder resultBuilder = new StringBuilder();
        long currentNumber = segment;
        while (currentNumber > 0) {
            long floatingNumber = currentNumber % 10;
            currentNumber /= 10;
            resultBuilder.append(floatingNumber);
        }

        int resultLength = resultBuilder.length();
        if (resultLength < MINIMAL_SEGMENT_ID_LENGTH) {
            resultBuilder.append("0".repeat(MINIMAL_SEGMENT_ID_LENGTH - resultLength));
        }

        resultBuilder.reverse();
        return resultBuilder.toString();
    }

    @SneakyThrows
    public void segmentVideo(Path videoPath, Path outputDir) {
        Files.createDirectories(outputDir);

        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i", videoPath.toAbsolutePath()
                .toString(),
                "-c:v", "libx264",
                "-c:a", "aac",
                "-preset", "fast",
                "-crf", "23",
                "-f", "hls",
                "-hls_time", "6",
                "-hls_playlist_type", "vod",
                "-hls_segment_filename", outputDir.resolve("segment_%03d.ts")
                .toString(),
                outputDir.resolve("playlist.m3u8")
                        .toString()
        );

        pb.inheritIO();

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg failed with exit code " + exitCode);
        }
    }

}
