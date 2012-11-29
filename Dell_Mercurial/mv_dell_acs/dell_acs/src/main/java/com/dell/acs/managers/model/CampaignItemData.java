/*
 * Copyright (c) SourceN Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers.model;

import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 @author Samee K.S
 @author $: sameeks $
 @version : 0 $,  :: 2012-03-07 09:56:40#$ */

public class CampaignItemData implements FormData {

    private Long id;

    private Campaign campaign;

    private Long itemID;

    private String itemType;

    // Will be replaced by ProductBean once we have the Product bean in place
    private String itemName;

    private Date creationDate;

    private Date modifiedDate;

    private User createdBy;

    private User modifiedBy;

    private Integer priority;

    private CampaignItemCategoryData category;

    private Map<String, String> properties = new HashMap<String, String>();

    // TODO: Merge this with properties Map
    // Related to products
    private Integer reviews;

    private Float stars;

    private Float price;

    private Float listPrice;

    private String location;

    // image , video, link
    private String url;
    private String desc;

    // Video
    private String image;

    public Campaign getCampaign() {
        return campaign;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Long getItemID() {
        return itemID;
    }

    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public CampaignItemCategoryData getCategory() {
        return category;
    }

    public void setCategory(CampaignItemCategoryData category) {
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getListPrice() {
        return listPrice;
    }

    public void setListPrice(Float listPrice) {
        this.listPrice = listPrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
