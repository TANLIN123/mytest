package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.DubboService;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = DubboService.class )
@Transactional
public class DubboServiceImpl implements DubboService {

    @Override
    public String sayHello(String name) {
        return "hello ...."+name;
    }
}
