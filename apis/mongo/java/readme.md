# readme for apis/mongo/java 

https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/time-to-live#set-time-to-live-value-for-a-document
globaldb:PRIMARY> db.coll.createIndex({"_ts":1}, {expireAfterSeconds: 10})

db.getCollection("vehicle_activity").find({"_id" : ObjectId("640e03cc74f91c0cf7885eda")})

db.getCollection("vehicle_activity").createIndex({"_ts":1}, {expireAfterSeconds: 3600})
