# searchapp

(I am assuming that JAVA_HOME has been set and maven is availble to build the app.)

1. This application uses Elastic search in backend to store and index files for faster search. So please do following to start elastic search on local. 

```
curl -O https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.5.2-linux-x86_64.tar.gz
tar -xzf elasticsearch-7.5.2-linux-x86_64.tar.gz
cd elasticsearch-7.5.2/bin
./elasticsearch 
```

2. clone this repository and run sprong boot app.

``` mvn clean install ``` 
(elastic search  must be started for the above if not build with out tests. ``` mvn clean install -DskipTests=true```)

```cd search-app```

 ```mvn spring-boot:run ```
 
3. I have configured swagger for this spring boot app so that user can use apis to search. 

go to following url : [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) 

4. Swagger has self describing documentation for the apis I have provided for the search. search fields can be found for any model (users, tickets or organizations by looking at Model section on the swagger page.)

5. curl also can be used for using the search apis. following is example for searching users 

```curl -X GET "http://localhost:8080/api/users/generic-search?field=email&val=coffeyrasmussen" -H "accept: */*" |json_pp```

6. Above curl will result in a user friendly json format. 

7. The search will work for exact match as well as partial string match on any field of the provided jsons (users,organizations and tickets.)

8. Provided jsons are being loaded at application start up time. 

9. Search results would contain all the related entities as well. (user results will include created/assigned ticket list as well as organization data, similarly ticket search result would contain created user info as well assigned user info.)
