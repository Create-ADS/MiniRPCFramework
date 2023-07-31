package online.pigeonshouse.minirpc.framwork.channel;

import io.netty.channel.ChannelHandlerContext;
import online.pigeonshouse.minirpc.api.framwork.request.RemoteCallRequest;

import java.util.ArrayList;
import java.util.List;

public class FilterChain {

    private List<Filter> filters = new ArrayList<>();

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void doFilter(RemoteCallRequest request, ChannelHandlerContext ctx) {
        for (Filter filter : filters) {
            filter.doFilter(request, ctx);
        }
    }
}