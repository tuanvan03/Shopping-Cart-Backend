package com.example.shopping.services.image;

import com.example.shopping.dtos.ImageDto;
import com.example.shopping.exceptions.ImageNotFoundException;
import com.example.shopping.models.Image;
import com.example.shopping.models.Product;
import com.example.shopping.repositories.ImageRepository;
import com.example.shopping.services.product.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final IProductService iProductService;

    public ImageService(ImageRepository imageRepository, IProductService iProductService) {
        this.imageRepository = imageRepository;
        this.iProductService = iProductService;
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image not found" + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ImageNotFoundException("Image not found" + id);
        });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product product = iProductService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String downloadUrl = "/image/image/download/" + image.getId();
                image.setDownloadUrl(downloadUrl);

                /// when you save the first time, image does not have id, so downloadUrl provides a incorrect link
                Image savedImage = imageRepository.save(image); /// ???????
                savedImage.setDownloadUrl("/image/image/download/" + savedImage.getId()); /// update the correct link
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                image.setId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch (IOException | SQLException exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);

        try {
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}
