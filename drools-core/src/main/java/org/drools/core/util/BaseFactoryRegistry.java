package org.drools.core.util;

import org.drools.core.KieFactory;
import org.drools.core.KieFactoryInitContext;
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

public abstract class BaseFactoryRegistry<T extends KieFactory, S extends KieFactoryInitContext> {

    private static final Logger log = LoggerFactory.getLogger(BaseFactoryRegistry.class);

    public final String fileName; // = "kassembly.properties";
    public final String path; //      = "META-INF/" + ASSEMBLY_FILE_NAME;
    public final String type;

    //private static final BaseRegistry INSTANCE = new BaseRegistry();

    private Map<ResourceType, T> registry;

    //    public static BaseRegistry getInstance() {
    //        return INSTANCE;
    //    }

    public BaseFactoryRegistry(String fileName, String type) {
        this.fileName = fileName;
        this.path = "META-INF/" + fileName;
        this.type = type;
    }

    public void register(ResourceType resourceType, T factory) {
        log.info("Registering " + type + " name = {}, factory = {}", resourceType.getName(), factory.getClass().getName());
        getRegistry().put(resourceType, factory);
    }

    public T getFactory(ResourceType resourceType) {
        return getRegistry().get(resourceType);
    }

    private Map<ResourceType, T> getRegistry() {
        if (registry == null) {
            initFactories();
        }

        return registry;
    }

    public abstract S newKieFactoryInitContext(List<T> list);

    public synchronized void initFactories() {
        // ensures only one thread initializes he registry
        if ( registry != null ) {
            return;
        }

        this.registry = new HashMap<ResourceType, T>();

        List<T> list = discoverFactories();

        for ( T factory : list ) {
            register( factory.getResourceType(), factory );
        }

//        KieFactoryInitContext ctx = newKieFactoryInitContext(list);
//        for ( KieFactory factory : list ) {
//            factory.preInit(ctx);
//        }
//
//        for ( KieFactory factory : list ) {
//            factory.init(ctx);
//        }
//
//        for ( KieFactory factory : list ) {
//            factory.postInit(ctx);
//        }
    }

    public List<T> discoverFactories() {


        ClassLoader classLoader = getClassLoader();

        List<T> list = new ArrayList<T>();

        final Enumeration<URL> e;
        try {
            e = classLoader.getResources( path );
        } catch ( IOException exc ) {
            log.error( "Unable to find and build {} for {}\n {} ", type, path, exc.getMessage() );
            return list;
        }

        // iterate urls, then for each url split the factory key and attempt to register each factory
        while ( e.hasMoreElements() ) {
            URL url = e.nextElement();
            try {
                Properties properties = new Properties();

                java.io.InputStream is = url.openStream();
                properties.load( is );
                is.close();

                String value = (String) properties.get( "factory" );
                if ( value != null ) {
                    String[] factoryNames = value.split(",");
                    for ( String factoryName : factoryNames) {
                        factoryName = factoryName.trim();
                        if ( !StringUtils.isEmpty( factoryName ) )  {
                            T factory = (T) Class.forName(factoryName, true, classLoader).newInstance();
                            list.add( factory );
                        }
                    }
                }
            } catch ( Exception exc ) {
                log.error( "Unable to build {} url={}\n", type, url.toExternalForm(), exc.getMessage() );
            }
        }

        log.info( "{} {} discovered {}", list.size(), type, list.toString() );

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
