package com.hunghq.librarymanagement.Service;

import com.hunghq.librarymanagement.Model.Entity.Document;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoadImageService {

    private final String defaultImagePath = "com/hunghq/librarymanagement/Media/LogoUet.jpg";
    private final int maxRetry = 20;
    private final CallAPIService callAPIService;
    private final ImageCacheService imageCacheService = new ImageCacheService();

    public LoadImageService(CallAPIService callAPIService) {
        this.callAPIService = callAPIService;
    }

    public void loadImage(Document document, ImageView imageView) {
        Task<Image> loadImageTask = new Task<>() {
            private int retry = 0;

            @Override
            protected Image call() {

                Image cachedImage = imageCacheService.getImage(document.getTitle());
                if (cachedImage != null) {
                    return cachedImage;
                }

                String coverImgUrl = callAPIService.getImageUrlFromTitle(document.getTitle());

                while (retry < maxRetry) {
                    try {
                        String coverImgDb = document.getCoverImg();
                        if (coverImgUrl != null) {
                            Image image = new Image(coverImgUrl, true);
                            imageCacheService.putImage(document.getTitle(), image);
                            return image;
                        }
                        else if (coverImgDb != null) {
                            Image image = new Image(coverImgDb, true);
                            imageCacheService.putImage(document.getTitle(), image);
                            return image;
                        }
                        //Using API is better but CPU is used more


                    } catch (Exception e) {
                        retry++;
                        System.err.println("Error loading image: " + e.getMessage());
                    }
                }

                Image defaultImage = new Image(getClass().getClassLoader().getResourceAsStream(defaultImagePath));
                ImageCacheService.putImage(document.getTitle(), defaultImage);
                return defaultImage;
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            if (image != null) {
                imageView.setImage(image);
            }
        });

        loadImageTask.setOnFailed(event -> {
            System.err.println("Failed to load image after " + maxRetry + " retries.");
            imageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(defaultImagePath)));
        });

        new Thread(loadImageTask).start();
    }
}
