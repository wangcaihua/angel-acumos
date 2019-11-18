package com.tencent.angel.acumos
import java.net.URL

object ReadModel {
  def main(args: Array[String]): Unit = {
    val dirPath = "fm"
    val url = this.getClass.getClassLoader.getResource("fm")
    print(url)
  }
}
