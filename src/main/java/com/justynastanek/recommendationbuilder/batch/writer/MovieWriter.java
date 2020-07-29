package com.justynastanek.recommendationbuilder.batch.writer;

import com.justynastanek.recommendationbuilder.model.Item;
import com.justynastanek.recommendationbuilder.repository.ItemRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieWriter implements ItemWriter<Item> {

    private ItemRepo itemRepository;

    @Autowired
    public MovieWriter(ItemRepo itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void write(List<? extends Item> items) throws Exception {

        System.out.println("Data Saved for Movies: " + items);
        itemRepository.saveAll(items);
    }
}
