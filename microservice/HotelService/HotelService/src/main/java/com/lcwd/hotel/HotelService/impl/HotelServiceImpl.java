package com.lcwd.hotel.HotelService.impl;


import com.lcwd.hotel.HotelService.entities.Hotel;
import com.lcwd.hotel.HotelService.exceptions.ResourceNotFoundException;
import com.lcwd.hotel.HotelService.repositories.HotelRepository;
import com.lcwd.hotel.HotelService.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service // Add the Service annotation to declare it as a Spring service
public class HotelServiceImpl implements HotelService {


    @Autowired
    private HotelRepository hotelRepository;
    @Override
    public Hotel create(Hotel hotel) {
        String hotelId = UUID.randomUUID().toString();
        hotel.setId(hotelId);
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel get(String id) {
        return hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel with given id not found"));
    }
}
