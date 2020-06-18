package com.patro.SpringBootProject.dao;

import com.patro.SpringBootProject.model.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<Image,String> {

    @Query("SELECT image FROM Image image WHERE image.bookId=:bookId")
    public List<Image> getImagesByBookId(@Param("bookId") String bookId);

    @Query("DELETE FROM Image image WHERE image.imageId=:imageId")
    public void deleteImageByImageId(String imageId);

    @Query("SELECT image FROM Image image WHERE image.imageId=:imageId")
    public Image getImageByImageId(String imageId);
}
