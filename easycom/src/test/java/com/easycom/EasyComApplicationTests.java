package com.easycom;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easycom.Mapper.SummaryMapper;
import com.easycom.Utils.DefaultParam;
import com.easycom.config.AppConfig;
import com.easycom.entity.PO.ScoreBreakdown;
import com.easycom.entity.PO.Summary;
import com.easycom.entity.VO.SummaryVo;
import com.easycom.entity.enums.ScoreBreakdownTypeEnum;
import com.easycom.entity.enums.SummaryStatusEnum;
import com.easycom.redis.RedisUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@SpringBootTest	
class EasyComApplicationTests {
	@Autowired
	private SummaryMapper summaryMapper;
	@Test
	void test(){
		QueryWrapper<Summary> summaryQueryWrapper = new QueryWrapper<>();
		summaryQueryWrapper.eq("status", 0);
		IPage<Summary> page = new Page<>(1, 15);
		List<Summary> summaryList = summaryMapper.selectList(page, summaryQueryWrapper);

		List<SummaryVo> summaryVos = new ArrayList<>();
		summaryList.forEach(summary -> summaryVos.add( BeanUtil.copyProperties(summary, SummaryVo.class)));
		summaryVos.forEach(System.out::println);
	}

	@Resource
	private AppConfig appConfig;
	@Test
	void contextLoads() {
		System.out.println();
		System.out.println();
		System.out.println();

		String userId = "U58474128947";
		String typeName = ScoreBreakdownTypeEnum.MORALITY.getTypeName();
		List<Object> filenameList = RedisUtils.lGet(DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + typeName + DefaultParam.REDIS_KEY_USER_TEMP_FILE_NAME_LIST,0,-1);
		int fileTotal = filenameList.size();

		String folder = appConfig.getProjectFolder()+DefaultParam.FILE_FOLDER_FILE+"/test/";
		File fileFolder = new File(folder);
		if(!fileFolder.exists()){
			fileFolder.mkdir();
		}
		// 遍历所有文件并保存
		for (int i = 0; i < fileTotal; i++) {
			String fileName = filenameList.get(i).toString();
			Object redisValue = RedisUtils.get(DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + typeName + ":" + fileName);
			
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
			File file = new File(fileFolder, fileName); // 文件名格式为 0.png, 1.png 等

			try (FileOutputStream fos = new FileOutputStream(file)) {
				fos.write(files); // 将字节数组写入文件
				fos.flush(); // 刷新输出流以确保数据完全写入文件
			} catch (IOException e) {
				System.out.println(e.getMessage()); // 捕获并处理可能的 IO 异常
			}
		}


		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	@Test
	void testRedis(){
		System.out.println();
		System.out.println();
		System.out.println();

		long l = RedisUtils.deleteKeysByPrefix("easycom:test1");
		System.out.println(l);

		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	@Test
	void testFileUtil(){
		System.out.println();
		System.out.println();
		System.out.println();
		
		String filePath = appConfig.getProjectFolder() + DefaultParam.FILE_FOLDER_FILE + "test";
		List<String> filenameList = FileUtil.listFileNames(filePath);
		filenameList.forEach(System.out::println);


		System.out.println();
		System.out.println();
		System.out.println();
		
	}

}
