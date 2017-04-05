# Pre-Requsite
    
    => Scalla 2.11.x
    => Java 1.8.x
    => Docker 1.11.x
    
# Installation
    
    => `Run setup.sh to run all required container` 
    => `Load init.cql in cassandra`
    
# Running
    
**core**

     => Run /video-analytics/core/src/main/scala/com/assign/core/SparkStreamCassandraSync.scala
     
**gen**

     => Run  /video-analytics/gen/src/main/scala/com/assign/gen/Main.scala

Using Generator

**Start Command** 

                START USERS FREQ DURATIION VIDEO ID
        EX =>   START 100 5 45 Video1


**Adding users**

                ADD USERS
        EX =>   ADD 10


**Droping users**

                DROP USERS
        EX =>   DROP 10
        
**client**
    
    Run /video-analytics/client/src/main/scala/com/assign/client/Main.scala
    
Using Client
    
    SHOW Video1 5 10 
    it will show count of user who viewed between 5 to 10 seconds 
