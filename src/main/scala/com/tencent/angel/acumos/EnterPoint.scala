package com.tencent.angel.acumos

import scopt.OptionParser

object EnterPoint {

  def main(args: Array[String]): Unit = {
    // --grpc_port 9876 --http_port 9875 --modelLoadPath E:\github\fitzwang\angel-acumos\models\fm
    val parser = new OptionParser[Options]("EnterPoint") {
      opt[Int]("grpc_port")
        .text("Port to listen on for gRPC API")
        .required()
        .action((x, c) => c.copy(grpc_port = x))
      opt[Int]("http_port")
        .text("Port to listen on for HTTP API")
        .required()
        .action((x, c) => c.copy(http_port = x))
//      opt[String]("modelLoadPath")
//        .text("model Load Path")
//        .required()
//        .action((x, c) => c.copy(modelLoadPath = x))
    }
    //val defaultOptions = Options(9876, 9875, "models/fm")
    val defaultOptions = Options(9876, 9875)

    parser.parse(args, defaultOptions).foreach { options =>
      ServerModel.init(options)
      val grpc = new GrpcServer(options)
      val http = new HttpServer(options)

      grpc.start()
      http.start()

      grpc.blockUntilShutdown()
      http.blockUntilShutdown()
    }
  }
}
