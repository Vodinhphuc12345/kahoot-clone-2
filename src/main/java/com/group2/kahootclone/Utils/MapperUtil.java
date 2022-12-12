package com.group2.kahootclone.Utils;

import org.modelmapper.ModelMapper;

public class MapperUtil {
    public final static ModelMapper INSTANCE = new ModelMapper();
    static {
        INSTANCE.getConfiguration().setSkipNullEnabled(true);
        INSTANCE.getConfiguration().setAmbiguityIgnored(true);
    }

}
