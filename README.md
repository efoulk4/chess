# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
Link to Sequence Diagrams: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afJCIJqTsXzQo8wHiVQSIwAgQnihignCQSRJgKSb6GLuNL7vUVAmkgWiTmyykoKp+h-Dsc5eXeS7CmKEpujKcplu8SqYCqwYagAchAMCqD4ID6m6mg6D6w78rU4q+cg67BTeUUOkmvrOl2MCVX5HBupyUYxnGhTuSB1T+m11WdUq3UoLGCnofCybIKmMDppmoxjDmqh5vM0FFiW9QAJJoFV-kbilnx0U2ZWLpZ1kJVu6XqjAe0HeuTRhepPSHhw5hICaGj9Y1HY1PUw3+c9Ww7ONk3xlpf2iWmGYEat60FmMW3QPUIPhQCrUCuKIDQCi4ANvR24eVSt7lQq2ABcGl7Tpu8iRby0WVMukqU3IhhujIW6-R+5n+o54oZHlCCAXzM1NRJYGEfp8wkah3wUVR9Zy7R02YXN2EwLh+HLcFNFkWMiuIcrpGE02njeH4-heCg6AxHEiQ23bjm+FgomCqB9QNNIEb8RG7QRt0PRyaoCnDEbSFq7zsK6Ybl5K8hKtoTp8IS1ZLpGCg3Doue8fG2gbnNYK52jjZQmu7n8H5wzC4CjForiuu13yLK8oR+gaWqllEDMMAMAAGrtQGl4wAndWMw1l0Z5VCAnBwz6UfnxMDZLgP7e1C8J+DvVR7NJRgLDACM8P8ojm3FqjMDZcwtn2Iy67imbDElyv6ctT2fbL55E-0rfrtTu3Au49a73mFCiGYEAaAUUrovJChdOzthTvUF2p4hYAUwCnV+-oP79ihnvea2sT65nzNBRy98YAtFPE-RilsAgonXP4bA4oNT8TRDAAA4kqDQ7tLKSXYf7IO9glThzzpHKGlQkEwHGIA-WDZMFTxaoyMAnCcwwITvAt+xcyb0hgI9dmC81HV2AfuUBtRB7VXZsaURHdbrd17gPIeC9R5Lxfn9dya8qCz38pvJePM05DXXtVHxSFt5TTwZUGGC0nDH2WgjEhhYL6lmvmXO+KAH7IVOs-bRdcFH1AsKkL+pN6r0iUSotQhi4HGKZkKWorCcgcK4V-aOX4GRcIAPrIByBgsWWDSxCJzB0tEu8IkawPlrPCRC1rxLGGUgUGQLCoBoLsTJNDmL+A4AAdjcE4FATgYgRmCHALiAA2eAE5DBlJgEUUZHtBqlikh0QRwjpjWMUqMfpKBMpKk0nc7SYsXhxyrtRJOqwxgfK+cRU2ZkY6p3+tZY4dZMooE2GUip6A1jgqVBowcRSf6lz0ceGmgCa4mPruY-yljAGdwylfHuxoHHVWHnqZxkdXG5NavtLx89XlNP8aWIG3KgWRmjBNHe4T4CjKPpMs+CTtq0pvkJchj8VlsrTtZfJYhfq4trmOc5qLAEYqVBCrkVSGrM2FHAc5V9kUNLmNBTFnxeUPBha0uYHp5G8L6UqV8vzoaSvGTrbMp9plkHyiOAAZr2BA8pjgfRgB811nzIIrItmsywWdbKbHtkgBIYB019ggFmgAUhAcUtrDD+GSKANU1z963NXo0JozIZI9A+TAERQq3kjGwLPdNUA4AQFslAQ1cwdrSB+ZLP5LqpGAtgSbeWKwfi9soAOodXwQQfLHbI6FX561v3qAAK1LWgfVrzVLLv7YO6AI6UBjuxTubV3ldHtX0US15JLqnLnJVTKxnbqV3WSX3b9TL9Rj1VXC6enK57BI7n4-6ATHowbQKEyGvr8Ga3TDEoNxCNqysvsk2+SqMmNiycUnJaqM4fJ9Qg7+Oqj3ilPZ289wA+2ruvfGpUd7TWLnNbUHtLGMI6mZcyZkY7f1zrQDAejaABSbukE6yRVGtwer5Ym6jiZ0NjMIbE4NuGxihv3JGvs8ppNpI43a8zSaTokdWVbLwLHs25vs-KRAwZYDAGwD2wgeQChXJ4apxoPs-YByDsYUwUcJH-JnSGGY4odqAnljuiyFGWogG4HgaQAAhc8HBYtoC41qrmZGfIvuPLl+yMW4vSA-Wamp37LGrjy-+ux9LgONbLWO7j5GIMtRntB8raAx5wc9hyxDA2x0ob6mhkZ+8pU6Zw0jFGpZ2uSe6ikUONAKHYEoD54jRNwPuNHlAAphWS5jnS+6bLNWeM1PaMdtAIBLG6krLgouf1JE4O6TC3p3Yo3DIlbNgN0qQ1hv5EZ6NEq3Nmay0-IAA