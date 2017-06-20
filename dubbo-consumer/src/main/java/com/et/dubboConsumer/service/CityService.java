package com.et.dubboConsumer.service;

import com.et.dubboConsumer.domain.City;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by shatao on 19/6/2017.
 */
public interface CityService {

    List<City> findAllCity() throws InterruptedException, ExecutionException;
    City findCityById(Long id);
    Long saveCity(City city);
    Long updateCity(City city);
    Long deleteCity(Long id);
}
