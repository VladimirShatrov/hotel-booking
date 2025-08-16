package t1internship.visualizationservice.api.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import t1internship.visualizationservice.api.config.MinioConfig;
import t1internship.visualizationservice.api.dto.FloorResponseDto;
import t1internship.visualizationservice.api.dto.SpaceDto;
import t1internship.visualizationservice.api.exception.NotFoundFloorException;
import t1internship.visualizationservice.api.exception.NotFoundSpaceException;
import t1internship.visualizationservice.api.mapper.FloorVisualizationMapper;
import t1internship.visualizationservice.data.entity.FloorVisualization;
import t1internship.visualizationservice.data.repository.FloorVisualizationRepository;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FloorVisualizationService {
    private final MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucketName;
    private final FloorVisualizationRepository floorVisualizationRepository;
    private final PlacesServiceClient placesServiceClient;
    private final MinioConfig minioConfig;

    @Transactional
    public String uploadFloor(Long floorId, MultipartFile file, List<SpaceDto> spacesDtoList ) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if(!placesServiceClient.checkFloorExists(floorId)){
            throw new NotFoundFloorException("Not found floor with id: "+ floorId);
        }
        for (SpaceDto space : spacesDtoList) {
            if (!placesServiceClient.checkPlaceExists(space.getPlaceId())) {
                throw new NotFoundSpaceException("Not found space with id: "+ space.getPlaceId());
            }
        }
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        FloorVisualization floorVisualization = FloorVisualizationMapper.toFloorVisualization(floorId,fileName,spacesDtoList);
        floorVisualizationRepository.save(floorVisualization);
        return fileName;
    }

    @Transactional
    public FloorResponseDto getFloor(Long floorId) throws Exception {
        FloorVisualization floorImage = (FloorVisualization) floorVisualizationRepository.findByFloorId(floorId)
                .orElseThrow(() -> new IllegalArgumentException("Floor not found"));
        String presignedUrl = minioConfig.getPresignedUrl(minioClient, bucketName, floorImage.getImageUrl());
        List<SpaceDto> places = floorImage.getSpaceCoordinates().stream().map(p ->
                new SpaceDto(p.getSpaceId(), p.getX(), p.getY())
        ).collect(Collectors.toList());
        return new FloorResponseDto(presignedUrl, places);
    }

}
