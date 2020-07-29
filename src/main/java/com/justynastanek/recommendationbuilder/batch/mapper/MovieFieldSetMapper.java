package com.justynastanek.recommendationbuilder.batch.mapper;

import com.justynastanek.recommendationbuilder.model.Item;
import com.justynastanek.recommendationbuilder.repository.ItemRepo;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Component
public class MovieFieldSetMapper implements FieldSetMapper {

    private ItemRepo itemRepository;

    @Autowired
    public MovieFieldSetMapper(ItemRepo itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Object mapFieldSet(FieldSet fieldSet) throws BindException {
        Item item = new Item();
        item.setMovieId(fieldSet.readString("movieId"));
        String titleWithReleaseYear = fieldSet.readString("title");

        Pattern pattern = Pattern.compile("\\((\\d{4})\\)");
        Matcher matcher = pattern.matcher(titleWithReleaseYear);
        if(matcher.find()) {
            item.setYear(matcher.group(1));
        }

        item.setTitle(titleWithReleaseYear
                .substring(0, titleWithReleaseYear.length() - 7));

        item.setGenre(fieldSet.readString("genre"));

        return item;
    }
}
