package fileserver.controllers;

import fileserver.dtos.UploadFileResponse;
import fileserver.models.ImageDetails;
import fileserver.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping(FileController.ENDPOINT)
public class FileController {

    public static final String ENDPOINT = "/files";

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public UploadFileResponse upload(@Valid @NotNull @RequestParam("file") MultipartFile file,
                                     @Valid @NotBlank @RequestParam("description") String description) throws Exception {
        return fileService.uploadFile(file, description);
    }


    @GetMapping("/search")
    public Page<ImageDetails> search(@RequestParam("page") Integer page,
                                     @RequestParam("pageSize") Integer pageSize,
                                     @RequestParam(name = "description", required = false) String description,
                                     @RequestParam(name = "type", required = false) String type,
                                     @RequestParam(name = "size", required = false) Long size) {
        return fileService.getImageDetails(description, type, size, PageRequest.of(page, pageSize));
    }
}
