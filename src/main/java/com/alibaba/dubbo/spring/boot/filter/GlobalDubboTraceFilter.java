package com.alibaba.dubbo.spring.boot.filter;

import org.springframework.util.StringUtils;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

public class GlobalDubboTraceFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment("traceId");
        if (!StringUtils.isEmpty(traceId) ) {
            // *) 从RpcContext里获取traceId并保存
            TraceIdUtil.setTraceId(traceId);
        } else {
            // *) 交互前重新设置traceId, 避免信息丢失
            RpcContext.getContext().setAttachment("traceId", TraceIdUtil.getTraceId());
        }
        // *) 实际的rpc调用
        return invoker.invoke(invocation);
	}

}
