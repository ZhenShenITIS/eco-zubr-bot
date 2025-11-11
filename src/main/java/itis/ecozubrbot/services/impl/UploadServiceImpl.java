package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.services.UploadService;
import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

@Component
@AllArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final String accessToken;

    private final String uploadsEndpoint;

    @Override
    public String getImageToken(File file) {
        System.out.println(file.getAbsolutePath());
        RestTemplate restTemplate = new RestTemplate();

        URI initUri = UriComponentsBuilder.fromHttpUrl(uploadsEndpoint)
                .queryParam("type", "image")
                .build(true)
                .toUri();

        HttpHeaders initHeaders = new HttpHeaders();
        initHeaders.set(HttpHeaders.AUTHORIZATION, accessToken);
        initHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> initResp =
                restTemplate.postForEntity(initUri, new HttpEntity<>(Map.of(), initHeaders), Map.class);
        Map<String, Object> initPayload = initResp.getBody();

        if (initPayload == null || initPayload.get("url") == null) {
            throw new IllegalStateException("Upload URL not returned");
        }
        String uploadUrl = initPayload.get("url").toString();

        URI uploadUri =
                UriComponentsBuilder.fromUriString(uploadUrl).build(true).toUri();

        String rawPhotoId = UriComponentsBuilder.fromUri(uploadUri)
                .build(true)
                .getQueryParams()
                .getFirst("photoIds");
        if (rawPhotoId == null || rawPhotoId.isBlank()) {
            throw new IllegalStateException("photoIds is missing in uploadUrl");
        }
        String photoId = UriUtils.decode(rawPhotoId, StandardCharsets.UTF_8);

        HttpHeaders partHeaders = new HttpHeaders();
        ContentDisposition disposition = ContentDisposition.builder("form-data")
                .name(photoId)
                .filename(file.getName())
                .build();
        partHeaders.setContentDisposition(disposition);
        MediaType fileType = MediaTypeFactory.getMediaType(file.getName()).orElse(MediaType.APPLICATION_OCTET_STREAM);
        partHeaders.setContentType(fileType);

        HttpEntity<Resource> filePart = new HttpEntity<>(new FileSystemResource(file), partHeaders);

        MultiValueMap<String, Object> multipart = new LinkedMultiValueMap<>();
        multipart.add(photoId, filePart);

        HttpHeaders uploadHeaders = new HttpHeaders();
        uploadHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        ResponseEntity<Map> uploadResp =
                restTemplate.postForEntity(uploadUri, new HttpEntity<>(multipart, uploadHeaders), Map.class);

        Map<String, Object> uploadPayload = uploadResp.getBody();

        if (uploadPayload == null) {
            throw new IllegalStateException("Token not found in upload response");
        }

        Object token = uploadPayload.get("token");
        if (token == null) {
            Object photos = uploadPayload.get("photos");
            if (photos instanceof Map<?, ?> p && p.get(photoId) instanceof Map<?, ?> pm) {
                token = pm.get("token");
            }
        }
        if (token == null) {
            Object retval = uploadPayload.get("retval");
            if (retval instanceof Map<?, ?> rv && rv.get("token") != null) {
                token = rv.get("token");
            } else if (retval != null) {
                token = retval;
            }
        }
        if (token == null) {
            throw new IllegalStateException("Token not found in upload response");
        }

        return token.toString();
    }
}
