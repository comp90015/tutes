# COMP90015 Project Optional JAR Starter


- Uses gradle to manage dependencies and build scripts
- Automatically includes json library jackson

Stores the output

```sh
server/target/lib/server.jar
client/target/lib/client.jar
```

The Uber jar is a fat jar that packages dependencies as well.


## Commands

```
#identitychange
#join
#who
#list
#createroom
#deleteroom
#quit
```

## Wire

### Types

```json
identity: [a-zA-Z0-9]{3,16}
string: char[]
i32: int
```

### C2S

#### identitychange

```json
{
    "type": "identitychange",
    "identity": identity
}
```

#### join

```json
{
    "type":"join",
    "roomid": string
}
```


#### who

```json
{
    "type":"who",
    "roomid": string
}
```

#### list

```json
{
    "type":"list"
}
```

#### createroom

```json
{
    "type":"createroom",
    "roomid": string
}
```

#### delete

```json
{
    "type":"delete",
    "roomid": string
}
```

#### quit

```json
{
    "type":"quit"
}
```

#### message


```json
{
    "type":"message",
    "content": string
}
```

### S2C


#### newidentity

```json
{
    "type":"newidentity",
    "former": identity,
    "identity": identity
}
````

#### roomchange

```json
{
    "type":"roomchange",
    "identity": identity,
    "former": identity,
    "roomid": string
}
```

#### roomcontents

```json
{
    "type":"roomcontents",
    "roomid": string,
    "identities": [identity],
    "owner": identity
}
```

#### roomlist

```json
{
    "type":"roomlist",
    "rooms": [{"roomid": string, "count": i32}]
}
```

#### message

```json
{
    "type":"message",
    "identity": identity,
    "content": string
}
```

