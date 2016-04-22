/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.server.core.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.LoggerFactory;

public final class HttpRequest {

    protected static final org.slf4j.Logger _log = LoggerFactory.getLogger(HttpRequest.class);

    /**
     * 进行 Http Post 操作
     *
     * @param httpUrl 网络地址
     * @param content 提交内容
     * @return 返回内容
     */
    public static final String post(String httpUrl, String content) {
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");//总共有其中方法：GET,POST,HEAD,OPTIONS,PUT,DELETE,TRACE,CONNECT
            connection.setRequestProperty("HOST", url.getHost());//设置主机和端口号
            connection.setRequestProperty("ACCEPT", "*/*");//设置可以接受的媒体（头部）
            //PUT有header"X-TT-PDMODE"，值可以是1（同'tcrdbputkeep'）,2(同'tcrdbputcat'),3(同'tcrdbput')
            //POST可以有header"X-TT-XNAME"或者"X-TT-MNAME"中的一个.
            //X-TT-XNAME等同于"tcrdbext"，用于指定函数名，header"X-TT-XOPTS"用于bitwise-or选项，1（记录锁）和2（全局锁），
            //每个请的URI将被作为URL编码过的key对待,而实体内容被作为value,结果表示在应答的实体正文中。
            //header"X-TT-MOPTS"用于bitwise-or选项,1(忽略更新日志)，请求参数用"application/x-www-form-urlencoded"格式表示在实体正文中，
            //名字被忽略而值被作为参数列表。结果在应答的实体正文中用"application/x-www.form-url-urlencoded"格式表示
            //PUT有header"X-TT-PDMODE"，值可以是1（同'tcrdbputkeep'）,2(同'tcrdbputcat'),3(同'tcrdbput')
//            connection.setRequestProperty("X-TT-PDMODE:", " 2 ");
            //设置连接主机超时（单位：毫秒）
            connection.setConnectTimeout(30000);
            //设置从主机读取数据超时（单位：毫秒）
            connection.setReadTimeout(30000);

            connection.connect();
            OutputStream outputStream = connection.getOutputStream();
            //想对象输出流写出数据，这些数据将存到内存缓冲区中
            //通过输出流对象构建对象输入流，以实现输出可序列化的对象
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                //想对象输出流写出数据，这些数据将存到内存缓冲区中
                bw.write(content);
                //刷新对象输出流，将任何字节都写入潜在的流中
                bw.flush();
                //关闭对象流，此时，不能再想对象输出流中写入任何数据，先前写入的数据存在于内存缓冲区中
                //在调用下面的getInputStream()函数时才把准备好的http请求正是发送到服务器
            }
            StringBuilder buffer = new StringBuilder();
            //上面的connection.getInputStream()方法已调用，本次http请求结束，下边向对象输出流的输出已无意义。
            //即使对象输出流没有调用close()方法，下面的操作也不会向对象输出流写入任何数据。
            //因此，要重新发送数据时需要重新创建连接、重新设参数、重新创建流对象、重新写数据，重新发送数据
            //connection.getInputStream()，将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端
            //注意，实际发送请求的代码段就在这里
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                //上面的connection.getInputStream()方法已调用，本次http请求结束，下边向对象输出流的输出已无意义。
                //即使对象输出流没有调用close()方法，下面的操作也不会向对象输出流写入任何数据。
                //因此，要重新发送数据时需要重新创建连接、重新设参数、重新创建流对象、重新写数据，重新发送数据
                buffer.append(content);
                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                    buffer.append("\r\n");
                }
            }
            return buffer.toString();
        } catch (IOException ex) {
            _log.error("Http Post Failed..", ex);
        }
        return null;
    }

//    public final String get(String key, String host, int port) throws IOException {
//        URL url = new URL(host + ":" + port + "/" + key);
//        //HttpURLConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection，
//        //故此处最好将其转化为HttpURLConnection类型的对象，以便用到HttpURLConnection更多的API
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        //设置是否向HttpURLConnection读入，默认情况下是false
//        connection.setDoInput(true);
//        //对象参数1:设置是否向HttpURLConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,默认情况下是false
//        connection.setDoOutput(true);
//        //请求不能使用缓存
//        connection.setUseCaches(false);
//        //设定传送的内容类型是可序列化的java对象（如果不设此项，在传送序列化对象时，当Web服务默认的不是这种类型时可能抛出java.io.EOFException）
//        connection.setRequestProperty("Content-type", "application/x-java-serialized-object");
//        //设置请求的方法是"GET",默认就是GET
//        connection.setRequestMethod("GET");
//
//        connection.setRequestProperty("HOST", host + ":" + port);
//        connection.setRequestProperty("ACCEPT", "*/*");
//        //上面的配置信息必须在connect之前完成
//        connection.connect();
//
//        //此处的getInputStream会隐含的进行connect（即：如同调用上面的connect()方法，所以在开发中不调用上述connect()也可以）
//        //通过输入流对象构建对象输入流，以实现输入可序列化的对象
//        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        StringBuffer buffer = new StringBuffer(100);
//        String line = null;
//        if (br == null) {
//            System.out.println("result is null!");
//        }
//        while ((line = br.readLine()) != null) {
//            buffer.append(line);
//            buffer.append("\r\n");
//        }
//        br.close();
//        return buffer.toString();
//    }
//
//    public final String delete(String key, String host, int port) throws IOException {
//        URL url = new URL(host + ":" + port + "/" + key);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setDoInput(true);
//        connection.setDoOutput(true);
//        connection.setRequestMethod("DELETE");
//        connection.setRequestProperty("HOST", host + ":" + port);
//        connection.setRequestProperty("ACCEPT", "*/*");
//        connection.connect();
//        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        StringBuffer buffer = new StringBuffer(100);
//        String line = null;
//        if (br == null) {
//            System.out.println("result is null!");
//        }
//        while ((line = br.readLine()) != null) {
//            buffer.append(line);
//            buffer.append("\r\n");
//        }
//        br.close();
//        return buffer.toString();
//
//    }
//
//    public final String options(String host, int port) throws IOException {
//        URL url = new URL(host + ":" + port + "/");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setDoInput(true);
//        connection.setDoOutput(true);
//        connection.setRequestMethod("OPTIONS");
//        connection.setRequestProperty("HOST", host + ":" + port);
//        connection.setRequestProperty("ACCEPT", "*/*");
//        connection.connect();
//        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        StringBuffer buffer = new StringBuffer(100);
//        String line = null;
//        if (br == null) {
//            System.out.println("result is null!");
//        }
//        while ((line = br.readLine()) != null) {
//            buffer.append(line);
//            buffer.append("\r\n");
//        }
//        br.close();
//        return buffer.toString();
//    }
//
//    public final String head(String key, String host, int port) throws IOException {
//        URL url = new URL(host + ":" + port + "/" + key);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setDoInput(true);
//        connection.setDoOutput(true);
//        connection.setRequestMethod("OPTIONS");
//        connection.setRequestProperty("HOST", host + ":" + port);
//        connection.setRequestProperty("ACCEPT", "*/*");
//        connection.connect();
//        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        StringBuffer buffer = new StringBuffer(100);
//        String line = null;
//        if (br == null) {
//            System.out.println("result is null!");
//        }
//        while ((line = br.readLine()) != null) {
//            buffer.append(line);
//            buffer.append("\r\n");
//        }
//        br.close();
//        return buffer.toString();
//    }
}
