package com.wy.common;
//package com.wy.springjava.base;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Iterator;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//
//import sun.net.www.http.HttpClient;
//
//public class BaseUpload extends BaseController {
//	public String upLoad(@RequestParam("file") MultipartFile multi) {
//	FileOutputStream fos = null;
//    InputStream in = null;
//    String fileUrl = null;
//    try {
//        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(this.session.getServletContext());
//        //检查form中是否有enctype="multipart/form-data"
//        if (multipartResolver.isMultipart(this.getRequest())) {
//            //将request变成多部分request
//            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) this.getRequest();
//            //获取multiRequest 中所有的文件名
//            Iterator<String> iterator = multiRequest.getFileNames();
//            while (iterator.hasNext()) {
//                MultipartFile multipartFile = multiRequest.getFile(iterator.next().toString());
//                in = multipartFile.getInputStream();
//                File file = new File(multipartFile.getOriginalFilename());
//                fos = new FileOutputStream(file);
//                byte[] buff = new byte[1024];
//                int len = 0;
//                while ((len = in.read(buff)) > 0) {
//                    fos.write(buff, 0, len);
//                }
//                String uploadUrl = System.getProperty("user.dir") + "/UploadFile";
//                HttpPost post = new HttpPost(uploadUrl);
//                HttpClient httpclient = new DefaultHttpClient();
//                MultipartEntity reqEntity = new MultipartEntity();
//                reqEntity.addPart("file", new FileBody(file));
//                post.setEntity(reqEntity);
//                HttpResponse response = httpclient.execute(post);
//                int statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode == HttpStatus.SC_OK) {
//                    fileUrl = EntityUtils.toString(response.getEntity());
//                }
//                file.deleteOnExit();
//            }
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    } finally {
//        if (fos != null) {
//            try {
//                fos.close();
//            } catch (IOException e) {
//            }
//        }
//        if (in != null) {
//            try {
//                in.close();
//            } catch (IOException e) {
//            }
//        }
//    }
//}
