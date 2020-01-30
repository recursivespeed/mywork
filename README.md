# searchapp
1. This application uses Elastic search in backend to store and index files for faster search. So please do following to start elastic search on local.

'''
curl -O https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.5.2-linux-x86_64.tar.gz
tar -xzf elasticsearch-7.5.2-linux-x86_64.tar.gz
cd elasticsearch-7.5.2/bin
./elasticsearch '''

2. clone this repository. 
3. mvn clean install (elastic search  must be started as i added integration tests)
4. cd search-app
5. ''' mvn spring-boot:run '''
