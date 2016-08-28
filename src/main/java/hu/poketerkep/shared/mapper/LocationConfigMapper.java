package hu.poketerkep.shared.mapper;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import hu.poketerkep.shared.model.LocationConfig;

import java.util.Map;


public class LocationConfigMapper {
    public static LocationConfig mapFromDynamoDB(Map<String, AttributeValue> valueMap) {
        LocationConfig locationConfig = new LocationConfig();

        locationConfig.setLocationId(valueMap.get("locationId").getS());
        locationConfig.setLatitude(Double.valueOf(valueMap.get("latitude").getN()));
        locationConfig.setLongitude(Double.valueOf(valueMap.get("longitude").getN()));
        locationConfig.setSteps(Integer.valueOf(valueMap.get("steps").getN()));
        locationConfig.setLastUsed(Long.valueOf(valueMap.get("lastUsed").getN()));

        return locationConfig;
    }
}
