package com.hunghq.librarymanagement.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import javafx.scene.image.Image;

import java.util.concurrent.TimeUnit;

public class ImageCacheService {
    // Cấu hình cache không giới hạn số lượng
    private static final Cache<String, Image> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS) // Xóa mục sau 1 giờ không được truy cập
            .build();

    /**
     * Lấy hình ảnh từ cache.
     *
     * @param key Key của hình ảnh.
     * @return Hình ảnh nếu tồn tại trong cache, null nếu không có.
     */
    public static Image getImage(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * Thêm hình ảnh vào cache.
     *
     * @param key   Key của hình ảnh.
     * @param image Hình ảnh cần lưu vào cache.
     */
    public static void putImage(String key, Image image) {
        cache.put(key, image);
    }

    /**
     * Xóa toàn bộ cache.
     */
    public static void clearCache() {
        cache.invalidateAll();
        System.out.println("Cache đã được xóa.");
    }

    /**
     * Kiểm tra số lượng mục trong cache.
     *
     * @return Số lượng mục hiện tại trong cache.
     */
    public static long getCacheSize() {
        return cache.size();
    }
}
