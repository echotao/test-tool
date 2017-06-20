package com.et.dubboConsumer.dao;

import com.et.dubboConsumer.domain.City;

import java.util.List;

/**
 * Created by shatao on 19/6/2017.
 */
public interface CityDao {

    List<City> findAllCity();

    City findById(Long id);

    Long saveCity(City city);

    Long updateCity(City city);

    Long deleteCity(Long id);
}
