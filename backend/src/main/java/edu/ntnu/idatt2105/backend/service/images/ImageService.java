package edu.ntnu.idatt2105.backend.service.images;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * This service provides functionality surrounding images for the web application. The functionality includes uploading,
 * downloading, and deleting images.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
    private final Path STORAGE_DIRECTORY = Paths.get("backend/src/main/resources/images/");
    private final List<String> VALID_FILES = Arrays.asList("image/png", "image/jpeg", "image/PNG",
                                                                "image/JPG", "image/jpg");

    /**
     * This method retrieves an image from the server. The image is retrieved by its name.
     *
     * @param fileName The name of the image.
     * @return The image.
     * @throws MalformedURLException If the image could not be found.
     */
    public ResponseEntity<Resource> getFile(String fileName) throws MalformedURLException {
        try {
            log.info("Path to file: " +STORAGE_DIRECTORY+"/"+fileName);
            Path file = Paths.get(STORAGE_DIRECTORY+"/"+fileName).toAbsolutePath().normalize();
            log.info(file.toString());

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
            } else {
                throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,
                        "File not found " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,
                    "File not found " + fileName);
        }
    }

    /**
     * This method creates a subdirectory in the main storage directory for the given user.
     * @param userId        The id of the user.
     * @throws IOException  Potential exceptions with creating directory.
     */
    public void initializeUserDir(Long userId) throws IOException {
        LOGGER.info("Initializing user's image directory.");
        Path filePath = Paths.get(STORAGE_DIRECTORY.toString(), String.valueOf(userId));
        Files.createDirectories(filePath);
    }

    /**
     * This method saves a given image to the user's image directory.
     * @param file                  The image to be saved.
     * @param userId                The user saving the image.
     * @return                      The file path of the saved image.
     * @throws FileSystemException  An error with saving the image.
     */
    public String saveUserImage(MultipartFile file, Long userId) throws FileSystemException {
        String fileName = file.getOriginalFilename();

        if (!VALID_FILES.contains(file.getContentType())) {
            throw new InvalidPathException(Objects.requireNonNull(file.getContentType()),"Invalid Path Extension.");
        }

        Path newFilePath = Paths.get(STORAGE_DIRECTORY.toString(), String.valueOf(userId), fileName);

        try {
            file.transferTo(newFilePath);
        } catch (Exception e) {
            throw new FileSystemException("File could not be saved");
        }
        return newFilePath.getFileName().toString();
    }

    /**
     * This method recursively deletes all the images associated with a user
     * @param userId            The id of the user.
     * @throws IOException      An IO exception.
     */
    public void removeUserDir(Long userId) throws IOException {
        LOGGER.info("Starting removal of  user's image directory.");

        Path filePath = Paths.get(STORAGE_DIRECTORY.toString(), String.valueOf(userId));
        Files.walkFileTree(filePath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path childFile, BasicFileAttributes attributes) throws IOException {
                try{
                    Files.delete(childFile);
                } catch (Exception e) {
                    throw new FileSystemException("Something went wrong while deleting images in directory");
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path childDirectory, IOException ioException) throws IOException {
                try {
                    Files.delete(childDirectory);
                } catch (Exception e){
                    throw new DirectoryNotEmptyException("Something went wrong while sub-directories");

                }
                return FileVisitResult.CONTINUE;
            }
        });

        try {
            Files.delete(filePath);
        } catch (Exception e) {
            throw new DirectoryNotEmptyException("The image directory is not empty and could not be deleted.");
        }
    }


        /**
         * Saves an image to the server. The image has the same name as the quiz id with a prefix "q".
         *
         * @param file The image to be saved.
         * @param quizId The id of the quiz.
         * @throws IOException If the image could not be saved.
         */
    @Transactional
    public void saveQuizImage(MultipartFile file, long quizId) throws IOException {
        createFile(file, "q"+quizId);
    }

    /**
     * Saves an image to the server. The image has the same name as the user id.
     *
     * @param file     The image to be saved.
     * @param filename The name of the image.
     * @throws IOException If the image could not be saved.
     */
    public void saveImage(MultipartFile file, long filename) throws IOException {
        createFile(file, String.valueOf(filename));
    }

    /**
     * Saves a file to the server. The file has the same name as the user id.
     *
     * @param file The file to be saved.
     * @param filename The name of the file.
     * @throws IOException If the file could not be saved.
     */
    private void createFile(MultipartFile file, String filename) throws IOException {
        if (!VALID_FILES.contains(file.getContentType())) {
            throw new InvalidPathException(Objects.requireNonNull(file.getContentType()),"Invalid Path Extension.");
        }
        Path newFilePath = Paths.get(STORAGE_DIRECTORY+"/"+filename);
        log.info("qwerqwer" + newFilePath);
        try {
            Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new FileSystemException("File could not be saved");
        }
    }

    /**
     * Deletes an image from the server. The image is deleted by its name.
     *
     * @param userId The id of the user with an image to be deleted.
     */
    public void removeImage(Long userId) {
        LOGGER.info("Deleting image at " + userId);
        try {
            Files.deleteIfExists(Paths.get(STORAGE_DIRECTORY+"/"+userId));
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    /**
     * Sets the default profile picture for a user.
     *
     * @param userId The id of the user with an image to be loaded.
     */
    public void setDefaultProfilePic(Long userId) throws IOException {
            Random random = new Random();
            Files.copy(Paths.get(STORAGE_DIRECTORY+"/default-"+random.nextInt(1, 10)),
                    Paths.get(STORAGE_DIRECTORY+"/"+userId),
                    StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Retrieves the default profile pictures.
     *
     * @return The default profile picture ids.
     */
    public List<String> getDefaultProfilePicIds() {
        List<String> defaultPics = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            defaultPics.add("default-"+i);
        }
        return defaultPics;
    }
}
