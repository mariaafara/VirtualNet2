# Ds project

Class Router :
             1-Create Router assign its ip
             2-Assign its Ports use *initializePort method* to do that with each port
             3-Assign connection for each port with its next directly connected neighbor use *initializeConnection method* 
             4-Start the routing protocol use *initializeRoutingProtocol method* which starts the *RoutingService thread*
             
Class Port *Thread* : 
                    1-Start *PortConnectionWait thread* immideatly which accepts a connection 
                    2-Or Could establish a connection to do that use *connect method*  which starts the *PortConnectionEstablish thread*
           

Class RoutingService *Thread* : 
                              1-Fills the RoutingTable 
                              2-Starts *RoutingTableRecieve thread* for each port
                              3-Starts broadcasting by starting the *RoutingTableBroadcast thread*
                              4-It manages all operations of the routing protocol
                              
