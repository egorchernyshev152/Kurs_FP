error id: x1u5NwK8dyCiS4yVF4czXw==
### Bloop error:

Unexpected error when copying <WORKSPACE>\src\main\resources\topics.csv to <WORKSPACE>\.bloop\v1\bloop-bsp-clients-classes\classes-Metals-yGIddrGDRXyPV2gG6RhALw==\topics.csv, you might need to restart the build server.
java.nio.file.FileSystemException: <WORKSPACE>\.bloop\v1\bloop-bsp-clients-classes\classes-Metals-yGIddrGDRXyPV2gG6RhALw==\topics.csv: Процесс не может получить доступ к файлу, так как этот файл занят другим процессом
	at java.base/sun.nio.fs.WindowsException.translateToIOException(WindowsException.java:92)
	at java.base/sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:103)
	at java.base/sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:108)
	at java.base/sun.nio.fs.WindowsFileCopy.copy(WindowsFileCopy.java:171)
	at java.base/sun.nio.fs.WindowsFileSystemProvider.copy(WindowsFileSystemProvider.java:284)
	at java.base/java.nio.file.Files.copy(Files.java:1305)
	at bloop.io.ParallelOps$.copy$1(ParallelOps.scala:215)
	at bloop.io.ParallelOps$.$anonfun$copyDirectories$10(ParallelOps.scala:243)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:23)
	at monix.eval.internal.TaskRunLoop$.startFuture(TaskRunLoop.scala:494)
	at monix.eval.Task.runToFutureOpt(Task.scala:586)
	at monix.eval.internal.TaskDeprecated$Extensions.runSyncMaybeOptPrv(TaskDeprecated.scala:128)
	at monix.eval.internal.TaskDeprecated$Extensions.$anonfun$coeval$1(TaskDeprecated.scala:303)
	at monix.eval.Coeval$Always.apply(Coeval.scala:1451)
	at monix.eval.Coeval.value(Coeval.scala:258)
	at bloop.io.ParallelOps$.$anonfun$copyDirectories$9(ParallelOps.scala:283)
	at monix.reactive.internal.consumers.ForeachAsyncConsumer$$anon$1.onNext(ForeachAsyncConsumer.scala:44)
	at monix.reactive.internal.consumers.LoadBalanceConsumer$$anon$1.$anonfun$signalNext$1(LoadBalanceConsumer.scala:218)
	at monix.execution.internal.InterceptRunnable.run(InterceptRunnable.scala:27)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:840)
#### Short summary: 

Unexpected error when copying <WORKSPACE>\src\main\resources\topics.csv to <WORKSPACE>\.bloop\v1\blo...