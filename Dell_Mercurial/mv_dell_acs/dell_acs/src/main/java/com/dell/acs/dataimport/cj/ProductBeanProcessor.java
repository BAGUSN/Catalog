/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.dataimport.cj;

import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductImage;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 3883 $, $Date:: 2012-07-09 03:31:30#$
 */
public class ProductBeanProcessor extends com.dell.acs.dataimport.ficstar.ProductBeanProcessor {

    @Override
    public boolean supportsBean(final Class clazz) {
        return clazz.equals(Product.class);
    }

    @Override
    public Map<String, Object> preProcessBeanValues(final Map<String, Object> row) {
        return row;
    }

    @Override
    public Object preProcessBeforePersist(final Object bean, final Map<String, Object> row) {
        Product product = (Product) bean;

        product.setSku(row.get("productId").toString());

        if (row.get("price") == null) {
            product.setPrice(Float.valueOf(row.get("listPrice").toString()));
        }

        Assert.notNull(row.get("image.srcImageURL"), "image is null for product :=" + product.getProductId());
        Assert.notNull(row.get("image.srcImageURL").toString().trim().length() == 0 ? null :
                row.get("image.srcImageURL"), "image is null for product :=" + product.getProductId());
        String baseImageName = FilenameUtils.getName(row.get("image.srcImageURL").toString());

        if (product.getId() != null && product.getImages().size() != 0) {
            logger.info("Product Image is found for Updating :=> " + product.getProductId());
            Collection<ProductImage> productImages = product.getImages();
            Iterator<ProductImage> iterator = productImages.iterator();
            while (iterator.hasNext()) {
                ProductImage productImage = iterator.next();
                if (productImage.getImageName().equalsIgnoreCase(baseImageName)) {
                    String fileExtension = baseImageName.substring(baseImageName.lastIndexOf(".") + 1, baseImageName.length());
                    if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
                        productImage.setImageType("image/jpeg");
                    }
                    productImage.setSrcImageURL(row.get("image.srcImageURL").toString());
                    productImage.setModifiedDate(new Date());
                    productImage.setProduct(product);
                    productImage.setCached(0);
                }
            }
        } else {

            logger.info("Product Image is found for Insert :=> " + product.getProductId());
            Collection<ProductImage> images = new LinkedList<ProductImage>();
            ProductImage image = new ProductImage();

            image.setImageName(baseImageName);
            image.setSrcImageURL(row.get("image.srcImageURL").toString());
            image.setCached(0);

            String fileExtension = baseImageName.substring(baseImageName.lastIndexOf(".") + 1, baseImageName.length());
            if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
                image.setImageType("image/jpeg");
            }
            image.setModifiedDate(new Date());
            image.setProduct(product);
            images.add(image);
            product.setImages(images);
        }
        return super.preProcessBeforePersist(bean, row);
    }
}
