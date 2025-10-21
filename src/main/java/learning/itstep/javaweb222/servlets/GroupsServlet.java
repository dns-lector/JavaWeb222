package learning.itstep.javaweb222.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import learning.itstep.javaweb222.data.DataAccessor;
import learning.itstep.javaweb222.data.dto.Product;
import learning.itstep.javaweb222.data.dto.ProductGroup;
import learning.itstep.javaweb222.rest.RestMeta;
import learning.itstep.javaweb222.rest.RestResponse;
import learning.itstep.javaweb222.rest.RestStatus;

@Singleton
public class GroupsServlet extends HttpServlet {
    private final DataAccessor dataAccessor;
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private RestResponse restResponse;
    
    @Inject
    public GroupsServlet(DataAccessor dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // викликається з кожним запитом до того, як буде передано doXxxx методам
        this.restResponse = new RestResponse();
        restResponse.setMeta(
                new RestMeta()
                .setServiceName("Shop API 'Product groups'")
                .setCacheSeconds(1000)
                .setManipulations(new String[] {"GET"})
                .setLinks(Map.ofEntries(
                    Map.entry("groups", "/groups"),
                    Map.entry("group", "/groups/{id}")
                ) )
        );
        
        super.service(req, resp); 
        
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().print(
                gson.toJson(restResponse)
        );
    }
    
    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() ;
        if(path == null) {
            getAllGroups(req, resp);
        }
        else {
            getGroup(req, resp);
        }
    }
    
    private void getGroup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        String path = req.getPathInfo().substring(1) ;   // прибираємо перший /
        ProductGroup pg = dataAccessor.getProductGroupBySlug(path);
        if(pg != null) {
            String fileUrl = getFileUrl(req);
            pg.setImageUrl( fileUrl + pg.getImageUrl() );
            List<Product> products = pg.getProducts();
            if(products != null) {
                for(Product p : products) {
                    p.setImageUrl( fileUrl + p.getImageUrl() );
                }
            }
            restResponse.getMeta().setDataType("json.object");
        }
        else {
            restResponse.setStatus(RestStatus.status404);
            restResponse.getMeta().setDataType("null");
        }
        this.restResponse.setData(pg);
    }
    
    private void getAllGroups(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
        List<ProductGroup> groups = dataAccessor.getProductGroups();
        String fileUrl = getFileUrl(req);
        for(ProductGroup group : groups) {
            group.setImageUrl( fileUrl + group.getImageUrl() );
        }
        
        restResponse.getMeta().setDataType("json.array");
        restResponse.setData(groups);        
    }
    
    private String getFileUrl(HttpServletRequest req) {
        return String.format("%s://%s:%d%s/file/", 
                req.getScheme(),
                req.getServerName(),
                req.getServerPort(),
                req.getContextPath());
    // req.getServletPath()  /groups
    // req.getContextPath()  /JavaWeb222
    // req.getRequestURI()   /JavaWeb222/groups
    // req.getServerName()   localhost
    // req.getScheme()       http
    // req.getServerPort()   8080
    }
}
/*
REST Representational State Transfer - архітектурний стиль програмного забезпечення
Зазвичай, мова іде про дотримання переліку вимог до інформаційної системи.
Client/Server – для веб - очевидно
Stateless – заборона сесій, сервер не повинен зберігати даних про попередні
  взаємодії - не мати стану. -> всі дані про "пам'ять" потрібно вкладати до 
  запитів. Зазвичай це включається до токенів
Cache – Сервер повинен повідомляти клієнта про можливість кешування даних.
  Зазвичай зазначається час, протягом якого дані не будуть змінюватись.
  Зі свого боку клієнт повинен кешувати дані, не надсилаючи повторні запити.
Layered system – можливість проксі, проміжних вузлів між клієнтом і сервером
  client ---------> server
         <---400---
                                  
  client ---------> proxy -------> server
         <--500----      <---400---
          time=12347        time=12345
          server=nginx      server=Tomcat
 -> Є потреба розділити дані про сутність та про шлях сутності
    Статус запитів НТТР засвідчують успішність проходження запитів
    Статус роботи - включається у пакет додатково

  client ---------> proxy -------> server
         <--200----      <---200---
        {status: 400}    {status: 400}
           

Code on demand (optional) – Дозволяється код, зокрема, HTML / SVG

Uniform interface -
 - Resource identification in requests: назва ресурсу включається у запит
 - Resource manipulation through representations: до відповіді додається 
    метаінформація про можливості маніпуляції ресурсом (CRUD)
 - Self-descriptive messages: відповідь повинна містити дані про тип контента
    чи способи його оброблення
 - Hypermedia as the engine of application state (HATEOAS) – включення до 
    відповіді відомостей про "зміст" - додаткові посилання в межах даного
    сервісу (або всіх посилань для "головної" сторінки)

---------------- зразок REST формату ----------------------
GET /api/groups -->

{
    "status": {
        "code": 200,
        "isOk": true,
        "phrase": "OK"
    },
    "meta": {
        "service": "Shop API 'Product groups'",
        "cacheSeconds": 1000,
        "pagination": {
            "page": 2,
            "perPage": 10,
            "lastPage": 3,
            "totalCount": 25
        },
        "dataType": "json.array",
        "links": {
            "groups": "/api/groups",
            "group": "/api/groups/{id}",
        },
        "manpulations": ["GET", "POST"],
        "pathParams": ["glass"],
Д.З. REST - додати до метаданих відповіді сервера параметр
"pathParams": ["glass"],
який буде відповідати за варіативні частини запиту,
(/groups/glass)
на базі побудована відповідь

    },
    "data": [
        {
          "id": "0b227043-a994-11f0-9062-62517600596c",
          "parentId": null,
          "name": "Скло",
          "description": "Вироби з кольорового та прозорого скла",
          "slug": "glass",
          "imageUrl": "http://localhost:8080/JavaWeb222/file/76d2e198-d8e0-463d-bc86-482c87ce73b6.jpg",
          "deletedAt": null,
          "products": null
        },
        {...
        }
    ]
}
*/
