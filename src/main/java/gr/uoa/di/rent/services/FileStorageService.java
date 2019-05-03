package gr.uoa.di.rent.services;

import gr.uoa.di.rent.exceptions.FileStorageException;
import gr.uoa.di.rent.exceptions.FileNotFoundException;
import gr.uoa.di.rent.properties.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Path getFileStorageLocation() { return fileStorageLocation; }

    public String storeFile(MultipartFile file, String fileName, String innerDir) {

        String file_name;

        if ( fileName != null ) {
            file_name = StringUtils.cleanPath(fileName);
        } else {
            file_name = StringUtils.cleanPath(file.getOriginalFilename());
        }

        Path path;

        try {
            if ( innerDir != null ) {
                path = Paths.get(this.fileStorageLocation.toString() + File.separator + innerDir);
                Files.createDirectories(path);
            }
            else
                path = this.fileStorageLocation;

            // Check if the file's name contains invalid characters
            if ( file_name.contains("..") ) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence: " + file_name);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = path.resolve(file_name);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return file_name;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file: " + file_name + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}
