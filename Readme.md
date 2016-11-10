This project reproduces the bug when the AsyncAppender is used.

To generate the logs, invoke the following after `sbt run`:

```
curl localhost:8080/build -v
```

When the AsyncAppender is used, the following log lines are emitted:
```
[info] {sourceThread=kamon-akka.actor.default-dispatcher-4, akkaSource=BuildInfoController(akka://kamon), sourceActorSystem=kamon, traceName=UnnamedTrace, traceToken=ubuntu-2, akkaTimestamp=06:06:33.458UTC}
[info] 2016-11-09 22:06:33.459 INFO  io.kamon.sample.BuildInfoController 06:06:33.458UTC [undefined] - getting build information!
```
The first line is a debug printout of the MDC map.

Notice `undefined` which never gets resolved. When stepping into the code,
the conversionRule used in logback.xml refers to `LogbackTraceTokenConverter`. 
Inside the method, the `Tracer.currentContext` is empty. However, the MDC 
values are indeed present. Why does the context turn out to be empty?
                   
When I do remove the AsyncAppender in the logback config (i.e., switching the appender 
from `ASYNC_CONSOLE` to `CONSOLE`), the `Tracer.currentContext` 
is not empty as follows:

```
[info] {traceName=UnnamedTrace, traceToken=ubuntu-1, sourceThread=kamon-akka.actor.default-dispatcher-4, akkaTimestamp=06:12:23.565UTC, akkaSource=BuildInfoController(akka://kamon), sourceActorSystem=kamon}
[info] 2016-11-09 22:12:23.575 INFO  io.kamon.sample.BuildInfoController 06:12:23.565UTC [ubuntu-1] - getting build information!
```
