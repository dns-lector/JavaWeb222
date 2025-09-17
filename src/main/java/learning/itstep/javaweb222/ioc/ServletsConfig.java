package learning.itstep.javaweb222.ioc;

import com.google.inject.servlet.ServletModule;
import learning.itstep.javaweb222.servlets.HomeServlet;

public class ServletsConfig extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/").with(HomeServlet.class);
    }
    
}
/*
Конфігурація сервлетів та фільтрів (middleware)
Замінює собою стандартну маршрутизацію (анотації @WebServlet, @WebFilter)
!! На всіх класах сервлетів та фільтрів зняти @Web-анотації, додати
!! анотацію @Singleton
*/