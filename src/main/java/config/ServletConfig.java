package config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import model.Logic;

/**
 * Created by sergeybp on 20.07.17.
 */
public class ServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule(){
            @Override
            protected void configureServlets() {
                bind(Logic.class);
                serve("*").with(GuiceContainer.class);
            }
        });
    }
}