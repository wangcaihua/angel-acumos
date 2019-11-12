#! /bin/bash

java -jar $ANGEL_ACUMOS/acumos-1.0-SNAPSHOT-assembly.jar  --grpc_port $GRPC_PORT --http_port $HTTP_PORT --modelLoadPath $MODEL_BASE_PATH

