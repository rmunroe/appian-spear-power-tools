package org.appiansc.plugins.spt;

import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.type.TypeService;
import org.apache.log4j.Logger;

public class AppianTypeHelper {
    private static final Logger LOG = Logger.getLogger(AppianTypeHelper.class);

    private static AppianTypeFactory typeFactory;


    /**
     * Static singleton AppianTypeFactory
     *
     * @param ts an injected TypeService instance
     * @return a AppianTypeFactory instance
     */
    public static AppianTypeFactory getTypeFactory(TypeService ts) {
        if (typeFactory == null) typeFactory = AppianTypeFactory.newInstance(ts);
        return typeFactory;
    }
}

