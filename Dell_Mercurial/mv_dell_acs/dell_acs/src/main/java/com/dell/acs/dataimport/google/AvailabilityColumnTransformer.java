package com.dell.acs.dataimport.google;

import com.dell.acs.persistence.domain.Product;
import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.transformer.ColumnTransformer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: Adarsh Date: 6/8/12 Time: 10:55 AM To change this template use File | Settings |
 * File Templates.
 */
public class AvailabilityColumnTransformer implements ColumnTransformer {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilityColumnTransformer.class);

    @Override
    public Object transform(Map<String, Object> record, ColumnDefinition columnDefinition, Object value) throws
            SQLException {

        if (value == null || StringUtils.isBlank(value.toString())) {
            logger.debug("availability was null returning default as OUT_OF_STOCK");
            return Product.Availability.IN_STOCK.getValue();
        }

        String strValue = value.toString().trim();

        if (strValue.equalsIgnoreCase("In Stock") || strValue.startsWith("in") || strValue.equalsIgnoreCase("YES") || strValue
                .equalsIgnoreCase("instock")) {
            return Product.Availability.IN_STOCK.getValue();
        } else if (strValue.equalsIgnoreCase("out of stock") || strValue.startsWith("out") || strValue.equalsIgnoreCase("NO") || strValue
                .equalsIgnoreCase("0") || strValue.equalsIgnoreCase("out of stock") || strValue.equalsIgnoreCase("outofstock")) {
            return Product.Availability.OUT_OF_STOCK.getValue();
        }
        logger.debug("unable to find Availability:=" + strValue + " returning default as OUT_OF_STOCK");
        return Product.Availability.OUT_OF_STOCK.getValue();

    }

    @Override
    public void setColumnDefinition(ColumnDefinition columnDefinition) {
    }

    @Override
    public void initialize() {
    }
}
