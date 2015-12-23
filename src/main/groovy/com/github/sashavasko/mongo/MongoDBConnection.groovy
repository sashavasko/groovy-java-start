package com.github.sashavasko.mongo
import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import groovy.util.logging.Log4j

@Log4j
class MongoDBConnection {
    MongoClient mongoClient
    MongoDatabase dataBase

    MongoDBConnection(){
    }

    MongoDBConnection(String host, int port, String dbName, String user, String password, MongoClientOptions options) {
        List<MongoCredential> credList = null
        if (user && password) {
            credList = new ArrayList<MongoCredential>(1)
            MongoCredential cred = MongoCredential.createScramSha1Credential(user, dbName, password.toCharArray());
            credList.add(cred)
        }
        connectDb(new ServerAddress("$host:$port"), credList, options, dbName)
    }

    public void connectDb(ServerAddress serverAddress, List<MongoCredential> credList, MongoClientOptions options, String dbName){
        log.info("Connecting to $dbName :  Striking ${serverAddress.toString()}.")
        if (credList){
            mongoClient = new MongoClient(serverAddress, credList, options)
        }else
            mongoClient = new MongoClient(serverAddress, options)
        dataBase = mongoClient.getDatabase(dbName)
        log.info(isOpen() ? 'Connected.' : 'Connection failed.')
    }

    boolean isOpen() {
        mongoClient != null && dataBase != null
    }

    def getCollection(String collectionName) {
        isOpen() ? dataBase.getCollection(collectionName) : null
    }

    void close() {
        if (null != mongoClient){
            mongoClient.close()
            mongoClient = null
        }
    }
}
