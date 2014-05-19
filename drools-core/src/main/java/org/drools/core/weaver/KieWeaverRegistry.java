package org.drools.core.weaver;

import org.kie.api.io.ResourceType;
import org.kie.internal.utils.ClassLoaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KieWeaverRegistry {

    private static final Logger log = LoggerFactory.getLogger(KieWeaverRegistry.class);

    public static String WEAVER_FILE_NAME = "kweaver.properties";
    public static String WEAVER_PATH      = "META-INF/" + WEAVER_FILE_NAME;

    private static final KieWeaverRegistry INSTANCE = new KieWeaverRegistry();

    private Map<ResourceType, KieWeaverFactory> registry;

    public static KieWeaverRegistry getInstance() {
        return INSTANCE;
    }

    private KieWeaverRegistry() {

    }

    public void register(ResourceType resourceType, KieWeaverFactory factory) {
        log.info("Registering Kie Weaver name = {}, factory = {}", resourceType.getName(), factory.getClass().getName());
        getRegistry().put(resourceType, factory);
    }

    public KieWeaverFactory getKieWeaverFactory(ResourceType resourceType) {
        return getRegistry().get(resourceType);
    }

    private Map<ResourceType, KieWeaverFactory> getRegistry() {
        if (registry == null) {
            initWeaver();
        }

        return registry;
    }

    public synchronized void initWeaver() {
        // ensures only one thread initializes he registry
        if ( registry != null ) {
            return;
        }

        this.registry = new HashMap<ResourceType, KieWeaverFactory>();

        List<KieWeaverFactory> list = discoverWeavers();

        KieWeaverContext ctx = new KieWeaverContextImpl( list );
        for ( KieWeaverFactory factory : list ) {
            factory.preInit(ctx);
        }

        for ( KieWeaverFactory factory : list ) {
            factory.init(ctx);
        }

        for ( KieWeaverFactory factory : list ) {
            factory.postInit(ctx);
        }
    }

    public List<KieWeaverFactory> discoverWeavers() {


        ClassLoader classLoader = getClassLoader();

        List<KieWeaverFactory> list = new ArrayList<KieWeaverFactory>();

        final Enumeration<URL> e;
        try {
            e = classLoader.getResources(WEAVER_PATH);
        } catch ( IOException exc ) {
            log.error( "Unable to find and build Kie Weaver of {}\n {} ", WEAVER_PATH, exc.getMessage() );
            return list;
        }

        // Map of kmodule urls
        while ( e.hasMoreElements() ) {
            URL url = e.nextElement();
            try {
                Properties properties = new Properties();

                java.io.InputStream is = url.openStream();
                properties.load( is );
                is.close();

                String factoryName = (String) properties.get( "factory" );
                KieWeaverFactory factory = (KieWeaverFactory) Class.forName(factoryName, true, classLoader).newInstance();
                list.add( factory );
                register(factory.getResourceType(), factory);
            } catch ( Exception exc ) {
                log.error( "Unable to build Kie Weaver url={}\n", url.toExternalForm(), exc.getMessage() );
            }
        }

        log.info( "{} Kie Weaver discovered {}", list.size(), list.toString() );

        return list;

    }

    public ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        if (cl == null) {
            cl = ClassLoaderUtil.class.getClassLoader();
        }
        return cl;
    }
}
