package com.example.ilovezappos;

/**
 * Created by Rama Vamshi Krishna on 1/26/2017.
 */
public class ProductDetails {
    String brandName, thumbnailImageUrl, productId, originalPrice, styleId, colorId, price, percentOff, productUrl, productName;

    public ProductDetails(String brandName, String colorId, String originalPrice, String percentOff, String price, String productId, String productName, String productUrl, String styleId, String thumbnailImageUrl) {
        this.brandName = brandName;
        this.colorId = colorId;
        this.originalPrice = originalPrice;
        this.percentOff = percentOff;
        this.price = price;
        this.productId = productId;
        this.productName = productName;
        this.productUrl = productUrl;
        this.styleId = styleId;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPercentOff() {
        return percentOff;
    }

    public void setPercentOff(String percentOff) {
        this.percentOff = percentOff;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        if(productName.contains(";")){
            productName=productName.substring(productName.indexOf(";")+2,productName.length());
        }
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public ProductDetails() {

    }
}