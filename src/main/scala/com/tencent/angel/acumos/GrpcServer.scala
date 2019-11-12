package com.tencent.angel.acumos


import com.tencent.angel.acumos.DatasetProto.{DataFrame, Prediction}
import com.tencent.angel.mlcore.PredictResult
import io.grpc.stub.StreamObserver
import io.grpc.{Server, ServerBuilder}


class GrpcServer(options: Options) extends ModelGrpc.ModelImplBase {
  private var server: Server = _
  private val model = ServerModel.get()

  @throws[Exception]
  def start(): Unit = {
    server = ServerBuilder.forPort(options.grpc_port)
      .addService(this)
      .build()
      .start()

    println("gRPC service start...")

    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        println("*** shutting down gRPC server since JVM is shutting down")
        if (server != null) server.shutdown
        println("*** server shut down")
      }
    })
  }

  @throws[InterruptedException]
  def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  override def classify(request: DataFrame, responseObserver: StreamObserver[Prediction]): Unit = {
    val rowsCount = request.getRowsCount
    if (rowsCount == 1) {
      val row = request.getRows(0)
      val responseBuilder = Prediction.newBuilder()
      val labeledData = Utils.row2LabeledData(row)
      val result: PredictResult = model.predict(labeledData)
      responseBuilder.setThreshold(0.5f)
      responseBuilder.putId2Prediction(labeledData.getAttach, result.pred.toFloat)
      val predictResponse = responseBuilder.build()
      responseObserver.onNext(predictResponse)
      responseObserver.onCompleted()
    } else if (rowsCount > 1) {
      throw new Exception("")
    } else {
      throw new Exception("")
    }
  }
}