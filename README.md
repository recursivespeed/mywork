# searchapp
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
