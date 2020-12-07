package com.example.ecommerce.configs;

import com.example.ecommerce.entity.Country;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductCategory;
import com.example.ecommerce.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Autowired
    private EntityManager entityManager;
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod [] theUnsporttedActions = {HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};

        disableHttpMethods(Product.class,config, theUnsporttedActions);
        disableHttpMethods(ProductCategory.class,config,theUnsporttedActions);
        disableHttpMethods(Country.class,config,theUnsporttedActions);
        disableHttpMethods(State.class,config,theUnsporttedActions);



      exposeIds(config);
    }

    private void disableHttpMethods(Class theClass,RepositoryRestConfiguration config, HttpMethod[] theUnsporttedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsporttedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsporttedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {

       Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
       List<Class> entityClasses = new ArrayList();
       for(EntityType tempEntityType:entities){
           entityClasses.add(tempEntityType.getJavaType());
       }
       Class[] domainTypes = entityClasses.toArray(new Class[0]);
       config.exposeIdsFor(domainTypes);
    }


}
