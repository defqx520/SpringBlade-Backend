package org.springblade.modules.task.controller;

import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;

@ApiIgnore
@RestController
@PropertySource("classpath:application-dev.yml")//读取application.yml文件
public class DownloadController {

	@Value("${upload.dir}")
	private String fileDir;

	@GetMapping("/download")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, String url, String fileName){
		String charsetCode = "utf-8";
		try {
			fileName = new String(fileName.getBytes(charsetCode), "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (fileName != null) {
			//设置文件路径
			File file = new File(fileDir,url);
			if (file.exists()) {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(fileName)));
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						os.flush();
						i = bis.read(buffer);
					}


				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

}
