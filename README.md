# microservices
## Registry-Service con Eureka Server
### Eureka Server
- To register and find out Microservices Instances
- Each eureka-client(MS) notifies heartbeats each 30 seconds
- to verify the health of the services
- Each MS has a cached copy of the Eureka's registry
- It works in cluster mode
- It has self-preservation mechanism

### Gateway
- Provides a single point to access the microservices ecosystem to all types of clients
- Dynamic Routing, monitoring and security
- load filters in hot

