# Ds project  MAster branch must be left untouch just working now on branch localtest2
(old version to update read me soon)

    
| Class         |   Variable Type            | Name          | What For                                                            |
|---------------|:-------------------------: |:-------------:| -------------------------------------------------------------------:|
| Router...     | HashMap<Integer, Port>     | portsConxs    | its to save eachport of the router with its own port class instance |
| Router...     | HashMap<Integer, Neighbor> | connections   | its to save each port with its directly connected neighbor          |
| Router        | InetAddress                | ip            | it represents the ip of the router                                  |
| RoutingTable  | HashMap<InetAddress,RoutingTableInfo>        | routingEntries| it represents the routingtable rows               |
| RoutingTableInfo | int                | nextHop            | it represents the port which is the nexthop in the cnx              |
| RoutingTableInfo | int                | cost            | it represents the nb of hops (nb routers in between)              |

**Class Router :**

1.Create Router assign its ip

2.Assign its Ports with *initializePort method* which creates new instance of Port class save he port with its instance to portsConxs hashmap then start the created thread

3.Assign connection for each port to its next directly connected neighbor with *initializeConnection method* which creates the given neighbor and then add it to the connections hashmap and then connect with this neighbor

4.Start the routing protocol use *initializeRoutingProtocol method* which starts the *RoutingService thread*
             
**Class Port Thread :**

1.Start *PortConnectionWait thread* immideatly which accepts a connection 

2.Or Could establish a connection to do that use *connect method*  which starts the *PortConnectionEstablish thread* which is used by *initializeConnection method* in **Router class**
           

**Class RoutingService Thread :**

1.Fills the RoutingTable using the *FillRoutingTable method* which adds all the directly connected neighbors to the routing table using *addEntry method* which adds a row to the table adding the destneighbor@ and the nexthop and cost=1

2.Starts *Reciever thread* for each port

3.Starts broadcasting by starting the *RoutingTableBroadcast thread*

4.It manages all operations of the routing protocol
                              
**Class Reciever Thread :** 

 1.Keeps reading objects at each port
 
 2.If the object is an instance of **RoutingTable** start *RoutingTableRecieve thread*
 
 3.If the object is an instance of **FailedNode** start *FailedNodeRecieve thread*
                        
**Class RoutingTableRecieve Thread :**

1.Recieves a RoutingTable ; if it is the 1st time ,resend it's own routing table 

2.Check if there should be an update to it's own routing table by starting *RoutingTableUpdate thread*
                                   
**Class RoutingTableUpdate Thread :** 

1.Checks if it's own routing table needs to be updated

2.Iterate through the neighbor's routing table

3.Checking if its routing table has an entry for each of its neighbor address

4.If it does compare the costs ; and update upon it the cost and the next hop of which the routing table was sent from

5.If it dosent add the whole Entry to its own table

6.If it's own routing table was updated in any case send it to all neighbors
                                  
**Class PortConnectionWait Thread**

1.Keeps waiting to recieve a socket connection at a certain port
if this port is not already assigned with a connection
the connectoin is establised successfuly and saved and send a true flag to inform that the connection succeded and waits to recieve the neighbor which requested the connection and save it 
if it was already reserved send false flag to inform that the port is already connected

**Class PortConnectionEstablish Thread**

1.Request a connection at a certain port with a neighbor
if it recieves a true flag thus the connection is available it saves the connection and send its self
                                   
