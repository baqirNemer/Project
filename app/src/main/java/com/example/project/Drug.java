package com.example.project;

import java.util.ArrayList;

public class Drug {
    private String product_ndc;
    private String generic_name;
    private String labeler_name;
    private String brand_name;
    private String dosage_form;
    private String product_type;

    public Drug() {

    }

    public Drug(String product_ndc, String generic_name, String labeler_name, String brand_name,
                String dosage_form, String product_type) {
        this.product_ndc = product_ndc;
        this.generic_name = generic_name;
        this.labeler_name = labeler_name;
        this.brand_name = brand_name;
        this.dosage_form = dosage_form;
        this.product_type = product_type;
    }

    public String getProduct_ndc() {
        return product_ndc;
    }

    public void setProduct_ndc(String product_ndc) {
        this.product_ndc = product_ndc;
    }

    public String getGeneric_name() {
        return generic_name;
    }

    public void setGeneric_name(String generic_name) {
        this.generic_name = generic_name;
    }

    public String getLabeler_name() {
        return labeler_name;
    }

    public void setLabeler_name(String labeler_name) {
        this.labeler_name = labeler_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getDosage_form() {
        return dosage_form;
    }

    public void setDosage_form(String dosage_form) {
        this.dosage_form = dosage_form;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }
}
