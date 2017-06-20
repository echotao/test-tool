package com.et.dubboConsumer.service.impl;

import com.et.dubboConsumer.dao.CityDao;
import com.et.dubboConsumer.domain.City;
import com.et.dubboConsumer.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shatao on 19/6/2017.
 */
//@Service
public class CityServiceDaoImpl implements CityService {
    @Autowired
    private CityDao cityDao;

    public List<City> findAllCity(){
        return cityDao.findAllCity();
    }

    public City findCityById(Long id) {
        return null;
    }

/*    public City findCityById(Long id) {
        return cityDao.findById(id);
    }*/

    public Long saveCity(City city) {
        return cityDao.saveCity(city);
    }

    public Long updateCity(City city) {
        return cityDao.updateCity(city);
    }

    public Long deleteCity(Long id) {
        return cityDao.deleteCity(id);
    }

}
