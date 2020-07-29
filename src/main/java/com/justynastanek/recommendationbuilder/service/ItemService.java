package com.justynastanek.recommendationbuilder.service;

import com.justynastanek.recommendationbuilder.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private ItemRepo itemRepository;

    @Autowired
    public ItemService(ItemRepo itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<String> getAllMovieIds() {
        return itemRepository.findAllMovieId();
    }

    public int updateDetails(String id, String lang, String post, String descr, String title) {
        return itemRepository.updateDetails(id, lang, post, descr, title);
    }
}
