package com.easycom;

import com.easycom.Utils.DefaultParam;
import com.easycom.config.AppConfig;
import com.easycom.entity.enums.ScoreBreakdownTypeEnum;
import com.easycom.redis.RedisUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@SpringBootTest
class EasyComApplicationTests {

	@Resource
	private AppConfig appConfig;
	@Test
	void contextLoads() {
		System.out.println();
		System.out.println();
		System.out.println();

		String userId = "U58474128947";
		String typeName = ScoreBreakdownTypeEnum.MORALITY.getTypeName();
		Integer fileTotal = (Integer) RedisUtils.get(DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + typeName + DefaultParam.REDIS_KEY_USER_TEMP_FILE_TOTAL);

		String folder = appConfig.getProjectFolder()+DefaultParam.FILE_FOLDER_FILE+"/test/";
		File fileFolder = new File(folder);
		if(!fileFolder.exists()){
			fileFolder.mkdir();
		}
		// 遍历所有文件并保存
		for (int i = 0; i < fileTotal; i++) {
			Object redisValue = RedisUtils.get(DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + typeName + ":" + i);

			if (redisValue == null) {
				System.out.println("文件 " + i + " 不存在！");
				continue;
			}

			byte[] files;

			// 判断是否为 byte[] 类型
			if (redisValue instanceof byte[]) {
				files = (byte[]) redisValue;
			} else if (redisValue instanceof String) {
				// 如果是 Base64 编码的字符串，则解码为字节数组
				String base64String = (String) redisValue;
				files = Base64.getDecoder().decode(base64String);
			} else {
				throw new IllegalArgumentException("Redis 中的数据类型不正确，预期为 byte[] 或 String，实际为：" + redisValue.getClass().getName());
			}

			// 将字节数组保存为文件
			File file = new File(fileFolder, i + ".png"); // 文件名格式为 0.png, 1.png 等

			try (FileOutputStream fos = new FileOutputStream(file)) {
				fos.write(files); // 将字节数组写入文件
				fos.flush(); // 刷新输出流以确保数据完全写入文件
			} catch (IOException e) {
				e.printStackTrace(); // 捕获并处理可能的 IO 异常
			}
		}


		System.out.println();
		System.out.println();
		System.out.println();
	}

}
