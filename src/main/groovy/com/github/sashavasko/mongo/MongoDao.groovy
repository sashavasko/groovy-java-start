package com.github.sashavasko.mongo

import com.mongodb.BasicDBObject
import com.mongodb.MongoClientOptions
import com.mongodb.ReadPreference
import groovy.util.logging.Log4j
import org.bson.conversions.Bson

import static com.mongodb.client.model.Projections.*


@Log4j
class MongoDao {

    static final def HOSTS_ALPHA = ['host-test1', 'host-test2']
    //TODO Add prod connections when we get them
    static final def HOSTS_PROD = ['host-prod1', 'host-prod2']

    static final int PORT = 27017
    static final String DB_NAME = 'dbName'
    static final String COLLECTION_NAME = 'collectionName'
    static final String USER = 'userName'
    static final String PASSWORD_ALPHA = 'password_test'
    static final String PASSWORD_PROD = 'password_prod'

    MongoDBConnection connection

    Bson projection

    MongoDao(){
        projection = fields(excludeId(), include('field1','structure.field'))
    }

    MongoDBConnection connectDB(){
        if (!connection){
            MongoClientOptions.Builder builder = new MongoClientOptions.Builder()
            builder.connectionsPerHost(20).readPreference(ReadPreference.secondaryPreferred())
            def password = PASSWORD_ALPHA
            def hosts = HOSTS_ALPHA
            String source = System.properties.get('mongoDb')
            if (source && source.contains('PROD')) {
                log.info('Using PRODUCTION data.')
                hosts = HOSTS_PROD
                password = PASSWORD_PROD
            }
            for (String host : hosts) {
                connection = new MongoDBConnection(host, PORT, DB_NAME, USER, password, builder.build())
                if (connection.isOpen())
                    break
            }
        }
        connection
    }

    def eachId(String groupName, Closure closure) {
        BasicDBObject query = new BasicDBObject('id', groupName)
        def cursor = connectDB().getCollection(COLLECTION_NAME).find(query).projection(projection)

        int foundCount = cursor.size()
        log.info("found $foundCount records for group $groupName")

        if (foundCount > 0) {
            for (def record : cursor) {
                closure (record)
            }
        }
    }
}
