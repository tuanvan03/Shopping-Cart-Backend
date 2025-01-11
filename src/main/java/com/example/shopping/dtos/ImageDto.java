package com.example.shopping.dtos;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class ImageDto {
    private Long imageId;
    private String imageName;
    private String downloadUrl;
}
