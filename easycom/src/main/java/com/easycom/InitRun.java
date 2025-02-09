package com.easycom;

import com.easycom.redis.RedisUtils;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@Component
public class InitRun implements ApplicationRunner {

    @Resource
    private DataSource dataSource;
    @Resource
    private Integer serverPort;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try{

            dataSource.getConnection();
            RedisUtils.get("test");
            if(System.getProperty("server.port")!=null){
                serverPort = Integer.parseInt(System.getProperty("server.port"));
            }
            log.info("服务端启动成功！端口号: "+serverPort);
        }catch (SQLException e){
            log.error("数据库启动错误！");
        }catch (RedisConnectionException e){
            log.error("redis启动错误！");
        }catch (Exception e){
            log.error("服务启动失败，{}",e.getMessage());
        }

    }
}
