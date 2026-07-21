import com.google.inject.AbstractModule
import repository.{CouchbaseDispatchingRepository, DispatchingRepository}
import services.{Dispatcher, DispatcherImpl}

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[Dispatcher]).to(classOf[DispatcherImpl])
    bind(classOf[DispatchingRepository]).to(classOf[CouchbaseDispatchingRepository])
  }

}
