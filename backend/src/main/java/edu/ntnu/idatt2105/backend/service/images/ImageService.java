package edu.ntnu.idatt2105.backend.service.images;

import edu.ntnu.idatt2105.backend.dto.images.ImageLoadDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This service provides functionality surrounding images for the web application. The functionality includes uploading,
 * downloading, and deleting images.
 */
@Service
public class ImageService {

    private final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
    private final Path STORAGE_DIRECTORY = Paths.get("src/main/resources/images");
    private final List<String> VALID_FILES = Arrays.asList("image/png", "image/jpeg", "image/PNG",
                                                                "image/JPG", "image/jpg");

    //TODO: add initialization of main storage directory, could be in the shell script.

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
     * This method saves a given image to the user's image directory.
     * @param file                  The image to be saved.
     * @param userId                The user saving the image.
     * @return                      The file path of the saved image.
     * @throws FileSystemException  An error with saving the image.
     */
    public String saveImage(MultipartFile file, Long userId) throws FileSystemException {
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
     * This method loads a given image from the user's image directory.
     * @param path                      Path of the image.
     */
    public ImageLoadDTO loadImage(String path) throws IOException {
        LOGGER.info("Attempting to retrieve the image.");
        Path pathObject = Paths.get(STORAGE_DIRECTORY.toString(), path);
        byte[] image = Files.readAllBytes(pathObject);


        String contentType = switch (path.split("\\.")[0]) {
            case "jpg", "jpeg":
                yield "image/jpeg";
            case "png":
                yield "image/png";
            case "gif":
                yield  "image/gif";
            default:
                yield "application/octet-stream";
        };
        LOGGER.info("Image resource retrieved.");
        return ImageLoadDTO.builder().image(image).contentType(contentType).build();
    }

    /**
     * This method deletes an image given its path.
     * @param filePath  Path of the image.
     * @return          Whether the action was successful.
     */
    public boolean deleteImage(String filePath) throws IOException {
        LOGGER.info("Deleting image at " + filePath);
        return Files.deleteIfExists(Path.of(filePath));
    }



}
