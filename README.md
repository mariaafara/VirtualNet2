# Ds project

**Class Router :**

1.Create Router assign its ip

2.Assign its Ports use *initializePort method* to do that with each port

3.Assign connection for each port with its next directly connected neighbor use *initializeConnection method* 

4.Start the routing protocol use *initializeRoutingProtocol method* which starts the *RoutingService thread*
             
**Class Port Thread :**

1.Start *PortConnectionWait thread* immideatly which accepts a connection 

2.Or Could establish a connection to do that use *connect method*  which starts the *PortConnectionEstablish thread*
           

**Class RoutingService Thread :**

1.Fills the RoutingTable 

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
                                  
                                 
                                   
