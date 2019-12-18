package com.kingdom.system.util.excel;

import lombok.Data;

import java.util.List;

@Data
public class FieldList extends FieldParent {

    private String name;

    private String title;

    private List<FieldParent> fields;
}
