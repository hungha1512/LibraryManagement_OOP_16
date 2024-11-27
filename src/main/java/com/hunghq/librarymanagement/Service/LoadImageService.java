package com.hunghq.librarymanagement.Service;

import com.hunghq.librarymanagement.Model.Entity.Document;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoadImageService {

    private final String defaultImagePath = "com/hunghq/librarymanagement/Media/LogoUet.jpg";
    private final int maxRetry = 25;
    private final CallAPIService callAPIService;
    private final ImageCacheService imageCacheService = new ImageCacheService();

    public LoadImageService(CallAPIService callAPIService) {
        this.callAPIService = callAPIService;
    }

    public void loadImageWithEntity(Document document, ImageView imageView) {
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
                            Image image = new Image(coverImgUrl, 120, 120 , true, true);
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
            //imageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(defaultImagePath)));
        });

        new Thread(loadImageTask).start();
    }

    public void loadImage(String url, ImageView imageView) {
        Task<Image> loadImageTask = new Task<>() {
            private int retry = 0;

            @Override
            protected Image call() {
                // Check if the image is already cached
                Image cachedImage = imageCacheService.getImage(url);
                if (cachedImage != null) {
                    return cachedImage;
                }

                while (retry < maxRetry) {
                    try {
                        // Load the image from the URL
                        if (url != null && !url.isEmpty()) {
                            Image image = new Image(url,  120, 120, true, true);
                            imageCacheService.putImage(url, image); // Cache the image
                            return image;
                        }
                    } catch (Exception e) {
                        retry++;
                        System.err.println("Error loading image: " + e.getMessage());
                    }
                }

                // If loading fails, use the default image
                Image defaultImage = new Image(getClass().getClassLoader().getResourceAsStream(defaultImagePath));
                imageCacheService.putImage(url, defaultImage); // Cache the default image
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
        });

        new Thread(loadImageTask).start();
    }

}
