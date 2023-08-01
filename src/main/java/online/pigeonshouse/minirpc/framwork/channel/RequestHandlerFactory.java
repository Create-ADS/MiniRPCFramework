package online.pigeonshouse.minirpc.framwork.channel;

public class RequestHandlerFactory {
    public static RequestHandler createHandler() {
        DefaultRequestHandler handler = new DefaultRequestHandler();
        handler.setServiceLocator(new CacheServiceLocator());
        handler.setParameterResolver(new DefaultParameterResolver());
        return handler;
    }
}
