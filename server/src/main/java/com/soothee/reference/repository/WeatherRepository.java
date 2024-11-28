package com.soothee.reference.repository;

import com.soothee.reference.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    /**
     * 날씨 일련번호로 날씨 조회</hr>
     *
     * @param weatherId Long : 날씨 일련번호
     * @return Optional<Weather> : 날씨 조회 (null 가능)
     */
    Optional<Weather> findByWeatherId(Long weatherId);
}
