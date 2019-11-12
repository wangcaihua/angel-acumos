package com.tencent.angel.acumos


import java.net.InetAddress

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletContextHandler, ServletHolder}
import org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS
import com.sun.jersey.spi.container.servlet.ServletContainer
import org.slf4j.{Logger, LoggerFactory}


class HttpServer(options: Options) {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[HttpServer])

  private val httpServer = new Server(options.http_port)
  private val servletContextHandler = new ServletContextHandler(NO_SESSIONS)
  servletContextHandler.setContextPath("/")
  httpServer.setHandler(servletContextHandler)
  httpServer.setHandler(servletContextHandler)
  private val servletHolder: ServletHolder = servletContextHandler.addServlet(classOf[ServletContainer], "/*")
  servletHolder.setInitOrder(0)
  servletHolder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig")
  servletHolder.setInitParameter("com.sun.jersey.config.property.packages", "com.tencent.angel.acumos.jersey")
  servletHolder.setInitParameter("jersey.config.server.provider.packages", "com.tencent.angel.acumos.jersey.resources")
  // 自动将对象映射成json返回
  servletHolder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true")

  def start(): Unit = {
    val addr: InetAddress = InetAddress.getLocalHost
    if(httpServer != null) {
      try {
        httpServer.start()
        LOG.info("Exporting HTTP/REST API at: " + addr.getHostAddress + ":" + options.http_port)
        httpServer.join()
      }
      catch {
        case ex: Exception =>
          ex.printStackTrace()
          LOG.info("Error occurred while starting Jetty")
          System.exit(-1)
      }
    } else {
      LOG.info("Failed to start HTTP Server at " + addr.getHostAddress + ":" + options.http_port)
    }
  }


  @throws[InterruptedException]
  def blockUntilShutdown(): Unit = {
    if(httpServer != null) httpServer.wait()
  }
}
