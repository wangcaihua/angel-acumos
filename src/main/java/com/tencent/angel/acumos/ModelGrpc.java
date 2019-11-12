package com.tencent.angel.acumos;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.17.1)",
    comments = "Source: default.proto")
public final class ModelGrpc {

  private ModelGrpc() {}

  public static final String SERVICE_NAME = "Model";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.tencent.angel.acumos.DatasetProto.DataFrame,
      com.tencent.angel.acumos.DatasetProto.Prediction> getClassifyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "classify",
      requestType = com.tencent.angel.acumos.DatasetProto.DataFrame.class,
      responseType = com.tencent.angel.acumos.DatasetProto.Prediction.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.tencent.angel.acumos.DatasetProto.DataFrame,
      com.tencent.angel.acumos.DatasetProto.Prediction> getClassifyMethod() {
    io.grpc.MethodDescriptor<com.tencent.angel.acumos.DatasetProto.DataFrame, com.tencent.angel.acumos.DatasetProto.Prediction> getClassifyMethod;
    if ((getClassifyMethod = ModelGrpc.getClassifyMethod) == null) {
      synchronized (ModelGrpc.class) {
        if ((getClassifyMethod = ModelGrpc.getClassifyMethod) == null) {
          ModelGrpc.getClassifyMethod = getClassifyMethod = 
              io.grpc.MethodDescriptor.<com.tencent.angel.acumos.DatasetProto.DataFrame, com.tencent.angel.acumos.DatasetProto.Prediction>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Model", "classify"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.angel.acumos.DatasetProto.DataFrame.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.tencent.angel.acumos.DatasetProto.Prediction.getDefaultInstance()))
                  .setSchemaDescriptor(new ModelMethodDescriptorSupplier("classify"))
                  .build();
          }
        }
     }
     return getClassifyMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ModelStub newStub(io.grpc.Channel channel) {
    return new ModelStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ModelBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ModelBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ModelFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ModelFutureStub(channel);
  }

  /**
   */
  public static abstract class ModelImplBase implements io.grpc.BindableService {

    /**
     */
    public void classify(com.tencent.angel.acumos.DatasetProto.DataFrame request,
        io.grpc.stub.StreamObserver<com.tencent.angel.acumos.DatasetProto.Prediction> responseObserver) {
      asyncUnimplementedUnaryCall(getClassifyMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getClassifyMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.tencent.angel.acumos.DatasetProto.DataFrame,
                com.tencent.angel.acumos.DatasetProto.Prediction>(
                  this, METHODID_CLASSIFY)))
          .build();
    }
  }

  /**
   */
  public static final class ModelStub extends io.grpc.stub.AbstractStub<ModelStub> {
    private ModelStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ModelStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ModelStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ModelStub(channel, callOptions);
    }

    /**
     */
    public void classify(com.tencent.angel.acumos.DatasetProto.DataFrame request,
        io.grpc.stub.StreamObserver<com.tencent.angel.acumos.DatasetProto.Prediction> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getClassifyMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ModelBlockingStub extends io.grpc.stub.AbstractStub<ModelBlockingStub> {
    private ModelBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ModelBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ModelBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ModelBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.tencent.angel.acumos.DatasetProto.Prediction classify(com.tencent.angel.acumos.DatasetProto.DataFrame request) {
      return blockingUnaryCall(
          getChannel(), getClassifyMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ModelFutureStub extends io.grpc.stub.AbstractStub<ModelFutureStub> {
    private ModelFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ModelFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ModelFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ModelFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.tencent.angel.acumos.DatasetProto.Prediction> classify(
        com.tencent.angel.acumos.DatasetProto.DataFrame request) {
      return futureUnaryCall(
          getChannel().newCall(getClassifyMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CLASSIFY = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ModelImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ModelImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CLASSIFY:
          serviceImpl.classify((com.tencent.angel.acumos.DatasetProto.DataFrame) request,
              (io.grpc.stub.StreamObserver<com.tencent.angel.acumos.DatasetProto.Prediction>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ModelBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ModelBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.tencent.angel.acumos.DatasetProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Model");
    }
  }

  private static final class ModelFileDescriptorSupplier
      extends ModelBaseDescriptorSupplier {
    ModelFileDescriptorSupplier() {}
  }

  private static final class ModelMethodDescriptorSupplier
      extends ModelBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ModelMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ModelGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ModelFileDescriptorSupplier())
              .addMethod(getClassifyMethod())
              .build();
        }
      }
    }
    return result;
  }
}
