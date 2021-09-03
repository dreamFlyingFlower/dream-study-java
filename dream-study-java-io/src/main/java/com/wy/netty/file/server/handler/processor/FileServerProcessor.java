package com.wy.netty.file.server.handler.processor;

import com.wy.netty.file.Result;
import com.wy.netty.file.server.parse.RequestParam;

public interface FileServerProcessor {

	Result process(RequestParam reqParams);
}