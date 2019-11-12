package com.tencent.angel.acumos

import java.util.concurrent.TimeUnit

import com.tencent.angel.acumos.DatasetProto.DataFrame
import io.grpc.ManagedChannelBuilder

import scala.reflect.ClassTag

class Client(host: String, port: Int) {
  private val channelBuilder = ManagedChannelBuilder.forAddress(host, port)
    .usePlaintext(true)
  private val channel = channelBuilder.build()

  private val blockingStub: ModelGrpc.ModelBlockingStub = ModelGrpc.newBlockingStub(channel)

  @throws[InterruptedException]
  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  def classify[T: ClassTag](id: String, data: Array[T]): Float = {
    val df = DataFrame.newBuilder().addRows(Utils.dataFrameRow(id, data)).build()
    val pred = blockingStub.classify(df)
    pred.getId2PredictionMap.get(id)
  }

  def classify[K: ClassTag, V: ClassTag](id: String, dim: K, dataMap: Map[K, V]): Float = {
    val df = DataFrame.newBuilder().addRows(
      Utils.dataFrameRow(id, dim, dataMap)
    ).build()
    val pred = blockingStub.classify(df)
    pred.getId2PredictionMap.get(id)
  }
}

object Client{
  def main(args: Array[String]): Unit = {
    val client = new Client("localhost", 9876)

    val dataMap = Map[Int, Double](
      (0, 1.3), (5, 3.6), (39, 0.9), (67, 2.5), (88, 7.0), (99, 0.9), (111, 7.3), (140, 2.9)
    )
    val res = client.classify("123", 148, dataMap)

    print(res)
    client.shutdown()
  }
}
