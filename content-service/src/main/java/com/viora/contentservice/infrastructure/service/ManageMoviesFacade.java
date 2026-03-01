package com.viora.contentservice.infrastructure.service;

import com.viora.contentservice.api.request.AddMovieRequest;
import com.viora.contentservice.domain.domain.MovieDetails;
import org.springframework.web.multipart.MultipartFile;

public interface ManageMoviesFacade {

    MovieDetails addMovie(AddMovieRequest request, MultipartFile multipartFile);

}
