package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.AttachDTO;
import jahongir.kun_uz.entity.AttachEntity;
import jahongir.kun_uz.exp.AppBadException;
import jahongir.kun_uz.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;


@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    @Value("${attache.folder}")
    private String attacheFolder;

    @Value("${server.url}")
    private String attachUrl;


    public String saveToSystem(MultipartFile file) {
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get("attaches/" + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<Resource> openSimple(String fileName) {
        Path filePath = Paths.get("attaches/" + fileName).normalize();
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + fileName);
            }
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Fallback content type
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public AttachDTO upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppBadException("File not found");
        }

        try {
            String pathFolder = getYmDString(); // 2026/05/21
            String key = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
            String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename())); // .jpg, .png, .mp4

            // create folder if not exists
            File folder = new File(attacheFolder + "/" + pathFolder); // attaches/2026/05/21
            if (!folder.exists()) {
                boolean t = folder.mkdirs();
            }

            // save to system
            byte[] bytes = file.getBytes();
            Path path = Paths.get(attacheFolder + "/" + pathFolder + "/" + key + "." + extension);
            // attaches/2026/05/21/dasdasd-dasdasda-asdasda-asdasd.jpg
            Files.write(path, bytes); // save to system

            // save to db
            AttachEntity entity = new AttachEntity();
            entity.setId(key + "." + extension);
            entity.setPath(pathFolder);
            entity.setSize(file.getSize());
            entity.setOrigenName(file.getOriginalFilename());
            entity.setExtension(extension);
            entity.setVisible(true);
            attachRepository.save(entity);

            return toDTO(entity);
        } catch (IOException e) {
            throw new AppBadException("Upload went wrong");
        }
    }

    private AttachDTO toDTO(AttachEntity entity) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(entity.getId());
        attachDTO.setOriginName(entity.getOrigenName());
        attachDTO.setSize(entity.getSize());
        attachDTO.setExtension(entity.getExtension());
        attachDTO.setCreatedData(entity.getCreatedDate());
        attachDTO.setUrl(openURL(entity.getId()));
        return attachDTO;
    }

    private String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    private String getExtension(String fileName) { // so.methi.ng.jpg
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public String openURL(String fileName) {
        return attachUrl + "/api/v1/attach/open/" + fileName;
    }

    public ResponseEntity<Resource> open(String id) { // d5ab71b2-39a8-4ad2-80b3-729c91c932be.jpg
        AttachEntity entity = getEntity(id);
        Path filePath = Paths.get(attacheFolder + "/" + entity.getPath() + "/" + entity.getId()).normalize();
        // attaches/2026/05/21/d5ab71b2-39a8-4ad2-80b3-729c91c932be.jpg
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + filePath);
            }
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Fallback content type
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public AttachEntity getEntity(String id) {
        return attachRepository.findById(id).orElseThrow(() -> new AppBadException("File not found"));
    }

    public ResponseEntity<Resource> download(String id){
        try {
            AttachEntity entity = getEntity(id);
            Path file = Paths.get(attacheFolder + "/" + entity.getPath() + "/" + entity.getId()).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() && resource.isReadable()){
                throw new AppBadException("File is not readable");
            }
            String contentType = Files.probeContentType(file);
            if (contentType == null){
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + entity.getOrigenName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new AppBadException(e.toString());
        }
    }
}
