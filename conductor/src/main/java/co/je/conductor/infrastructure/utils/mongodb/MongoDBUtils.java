package co.je.conductor.infrastructure.utils.mongodb;

import org.bson.types.ObjectId;

import com.mongodb.DBObject;

public class MongoDBUtils {

    public static ObjectId getMongoObjectId(DBObject dbo) {

        ObjectId objectId = (ObjectId) dbo.get("_id");
        return objectId;
    }
}