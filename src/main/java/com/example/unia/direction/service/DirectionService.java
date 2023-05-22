package com.example.unia.direction.service;

import com.example.unia.api.dto.DocumentDto;
import com.example.unia.api.service.KakaoCategorySearchService;
import com.example.unia.direction.entity.Direction;
import com.example.unia.direction.repository.DirectionRepository;
import com.example.unia.restaurant.service.RestaurantSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT = 100;
    private static final double RADIUS_KM = 10.0;
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    private final RestaurantSearchService restaurantSearchService;
    private final DirectionRepository directionRepository;
    private final KakaoCategorySearchService kakaoCategorySearchService;

    @Transactional
    public List<Direction> saveAll(List<Direction> directionList) {
        if (CollectionUtils.isEmpty(directionList)) return Collections.emptyList();
        return directionRepository.saveAll(directionList);
    }

    public List<Direction> buildDirectionList(DocumentDto documentDto){

        if(Objects.isNull(documentDto)) return Collections.emptyList();

        return restaurantSearchService.searchRestaurantDtoList()
                .stream().map(restaurantDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLatitude(documentDto.getLatitude())
                                .inputLongitude(documentDto.getLongitude())
                                .targetRestaurantName(restaurantDto.getRestaurantName())
                                .targetAddress(restaurantDto.getRestaurantAddress())
                                .targetLatitude(restaurantDto.getLatitude())
                                .targetLongitude(restaurantDto.getLongitude())
                                .distance(
                                        calculateDistance(37.282669, 127.041801,
                                                restaurantDto.getLatitude(), restaurantDto.getLongitude())
                                )
                                .build())
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());

    }
    public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto){
        if(Objects.isNull(inputDocumentDto)) return Collections.emptyList();

        return kakaoCategorySearchService
                .requestRestaurantCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
                .getDocumentDtoList()
                .stream().map(resultDocumentDto ->
                        Direction.builder()
                                        .inputAddress(inputDocumentDto.getAddressName())
                                        .inputLatitude(inputDocumentDto.getLatitude())
                                        .inputLongitude(inputDocumentDto.getLongitude())
                                        .targetRestaurantName(resultDocumentDto.getPlaceName())
                                        .targetAddress(resultDocumentDto.getAddressName())
                                        .targetLatitude(resultDocumentDto.getLatitude())
                                        .targetLongitude(resultDocumentDto.getLongitude())
                                        .distance(
                                                calculateDistance(37.282669, 127.041801,
                                                        resultDocumentDto.getLatitude(), resultDocumentDto.getLongitude())
                                        )
                                .build())
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371;
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }
}
