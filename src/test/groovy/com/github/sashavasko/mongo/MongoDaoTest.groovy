package com.github.sashavasko.mongo

import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

public class MongoDaoTest {

    MongoDBConnection mockMongo = new MongoDBConnection()
    def actualConnectionParams = [:]

    @Before
    public void setUp() {
        mockMongo.metaClass.connectDb = { ServerAddress serverAddress, List<MongoCredential> credList, MongoClientOptions options, String dbName ->
            actualConnectionParams = [serverAddress:serverAddress,credList:credList, options:options, dbName:dbName]
        }
        MongoDBConnection.metaClass.constructor = {String host, int port, String dbName, String user, String password, MongoClientOptions options->
            actualConnectionParams = [host:host,port:port, dbName:dbName, user:user, options:options]
            mockMongo
        }
    }

    @After
    public void cleanup(){
        MongoDBConnection.metaClass = null
    }

    @Test
    public void testConnectBuiDB() throws Exception {
        MongoDao dao = new MongoDao()

        dao.connectDB()

        assert 'dbName' == actualConnectionParams['dbName']
    }

    @Ignore
    @Test
    public void integrationTestConnectBuiDB() throws Exception {
        MongoDao dao = new MongoDao()

        assert dao.connectDB().isOpen()
    }

    @Ignore
    @Test
    public void integrationTestEachDealerId() throws Exception {
        MongoDao dao = new MongoDao()
        dao.eachId('SOMETHING'){record->
            println record
        }
    }
}