import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.{ClassLoaderResourceAccessor, CompositeResourceAccessor, FileSystemResourceAccessor}
import liquibase.servicelocator.LiquibaseService
import org.squeryl.adapters.{H2Adapter, PostgreSqlAdapter}


package object db {
  private val configuration = ConfigFactory.load()
  private val config = new HikariConfig()

  val url = configuration.getString("db.default.url")
  val user = configuration.getString("db.default.user")
  val password = configuration.getString("db.default.password")
  config.setJdbcUrl(url)
  config.setUsername(user)
  config.setPassword(password)

  val hikariDataSource = new HikariDataSource(config)

  object SquerylConfig
  {
    lazy val dbDefaultAdapter = configuration.getString("db.default.driver") match
    {
      case "org.h2.Driver" => new H2Adapter
      case "org.postgresql.Driver" => new PostgreSqlAdapter
      case _ => sys.error("Database driver must be either org.h2.Driver or org.postgresql.Driver")
    }


    lazy val logSql = configuration.getBoolean("log-sql")
  }

  object Liqui{
    def mkLiquibase: Liquibase = {
      val fileAccessor = new FileSystemResourceAccessor()
      val classLoader  = classOf[LiquibaseService].getClassLoader
      val classLoaderAccessor = new ClassLoaderResourceAccessor(classLoader)
      val fileOpener = new CompositeResourceAccessor(fileAccessor, classLoaderAccessor)
      val dbConnection: JdbcConnection = new JdbcConnection(hikariDataSource.getConnection)
      val liqui = new Liquibase(configuration.getString("changelog"), fileOpener, dbConnection)
      liqui
    }
  }
}
