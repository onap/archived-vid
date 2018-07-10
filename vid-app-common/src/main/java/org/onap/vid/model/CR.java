package org.onap.vid.model;

import java.util.HashMap;
import java.util.Map;

public class CR extends Node{

    private String category;

    private String subcategory;

    private String resourceVendor;

    private String resourceVendorRelease;

    private String resourceVendorModelNumber;

    private String customizationUUID;

    private Map<String, NetworkCollection> networksCollection = new HashMap<String, NetworkCollection>();



    public Map<String, NetworkCollection> getNetworksCollection() {
        return networksCollection;
    }

    public void setNetworksCollection(Map<String, NetworkCollection> networksCollection) {
        this.networksCollection = networksCollection;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getResourceVendor() {
        return resourceVendor;
    }

    public void setResourceVendor(String resourceVendor) {
        this.resourceVendor = resourceVendor;
    }

    public String getResourceVendorRelease() {
        return resourceVendorRelease;
    }

    public void setResourceVendorRelease(String resourceVendorRelease) {
        this.resourceVendorRelease = resourceVendorRelease;
    }

    public String getResourceVendorModelNumber() {
        return resourceVendorModelNumber;
    }

    public void setResourceVendorModelNumber(String resourceVendorModelNumber) {
        this.resourceVendorModelNumber = resourceVendorModelNumber;
    }

    public String getCustomizationUUID() {
        return customizationUUID;
    }

    public void setCustomizationUUID(String customizationUUID) {
        this.customizationUUID = customizationUUID;
    }

}
