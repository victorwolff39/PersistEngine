package net.notfab.lib.entities;

import java.util.List;

public interface SQLMultiFilter extends SQLFilter {

    List<String> getValues();

    @Override
    default String getValue() {
        return null;
    }

}