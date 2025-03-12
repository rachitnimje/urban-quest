## Challenges Faced
- JSON Serialization through Jackson when using LAZY fetch.
  - When you use *FetchType.LAZY* in JPA, Hibernate doesn't load the actual entity immediately. Instead, it creates a proxy object that will load the real data only when accessed.
  - SOLUTION: The *@JsonIgnoreProperties* annotation tells Jackson to skip these specific properties during serialization, which prevents it from trying to access and serialize the internals of the Hibernate proxy.

