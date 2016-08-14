package com.pop.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Communication {
	

    public String communication02(String urlString,Map<String, Object> params,String  imageuri ,String img){  
        String result="";  
          
        String end = "\r\n";          
        String MULTIPART_FORM_DATA = "multipart/form-data";
        String BOUNDARY = "---------7d4a6d158c9"; //
        String imguri ="";  
        if (!imageuri.equals("")) {  
            imguri = imageuri.substring(imageuri.lastIndexOf("/") + 1);//���ͼƬ���ļ�����  
            //System.out.println(imguri);
        }  
          
          
          
          
        if (!urlString.equals("")) {  
            //uploadUrl = uploadUrl+urlString;  
        	//return "faile";
          
        	try {  
        		URL url = new URL(urlString);    
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();    
                conn.setDoInput(true);
                conn.setDoOutput(true);//�������    
                conn.setUseCaches(false);//��ʹ��Cache    
                conn.setConnectTimeout(6000);// 6�������ӳ�ʱ  
                conn.setReadTimeout(6000);// 6���Ӷ����ݳ�ʱ  
                conn.setRequestMethod("POST");              
                conn.setRequestProperty("Connection", "Keep-Alive");    
                conn.setRequestProperty("Charset", "UTF-8");    
                conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);  
              
                StringBuilder sb = new StringBuilder();    
                
                //�ϴ��ı��������֣���ʽ��ο�����    
                for (Map.Entry<String, Object> entry : params.entrySet()) {//�������ֶ�����    
                	sb.append("--");    
                    sb.append(BOUNDARY);    
                    sb.append("\r\n");    
                    sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");    
                    sb.append(entry.getValue());    
                    sb.append("\r\n");    
                }    
  
                sb.append("--");    
                sb.append(BOUNDARY);    
                sb.append("\r\n");    
  
                //System.out.println(sb);
                
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());  
                dos.write(sb.toString().getBytes());  
              
                if (!imageuri.equals("")&&!imageuri.equals(null)) {  
                	dos.writeBytes("Content-Disposition: form-data; name=\""+img+"\"; filename=\"" + imguri + "\"" + "\r\n"+"Content-Type: image/jpeg\r\n\r\n");  
                    FileInputStream fis = new FileInputStream(imageuri);  
                    byte[] buffer = new byte[1024]; // 8k  
                    int count = 0;  
                    while ((count = fis.read(buffer)) != -1)  
                    {  
                      dos.write(buffer, 0, count);  
                    }  
                    dos.writeBytes(end);  
                    fis.close();  
                }  
                System.out.println(dos.toString().getBytes());
                dos.writeBytes("--" + BOUNDARY + "--\r\n");  
                dos.flush();                
                
                int res = conn.getResponseCode();
                System.out.println(res);
                InputStream is = conn.getInputStream();  
                InputStreamReader isr = new InputStreamReader(is, "utf-8");  
                BufferedReader br = new BufferedReader(isr);  
                result = br.readLine();  
              
        	}catch (Exception e) {  
        		e.printStackTrace();
        		result = "{\"ret\":\"898\"}";  
        	}  
        }  
        return result;  
          
    }  
    
    /**  
     * ͨ��ƴ�ӵķ�ʽ�����������ݣ�ʵ�ֲ��������Լ��ļ�����  
     * @param params
     * @param files  
     * @return  
     * @throws IOException  
     */  
    public static String post(String actionUrl ,  
            Map <String , String> params , Map <String , File> files)  
            throws IOException  {  

            String BOUNDARY = java.util.UUID.randomUUID ( ).toString ( ) ;  
            String PREFIX = "--" , LINEND = "\r\n" ;  
            String MULTIPART_FROM_DATA = "multipart/form-data" ;  
            String CHARSET = "UTF-8" ;  

            URL uri = new URL ( actionUrl ) ;  
            HttpURLConnection conn = ( HttpURLConnection ) uri  
                    .openConnection ( ) ;  
            conn.setReadTimeout ( 5 * 1000 ) ; // ������ʱ��  
            conn.setDoInput ( true ) ;// ��������  
            conn.setDoOutput ( true ) ;// �������  
            conn.setUseCaches ( false ) ; // ������ʹ�û���  
            conn.setRequestMethod ( "POST" ) ;  
            conn.setRequestProperty ( "connection" , "keep-alive" ) ;  
            conn.setRequestProperty ( "Charsert" , "UTF-8" ) ;  
            conn.setRequestProperty ( "Content-Type" , MULTIPART_FROM_DATA  
                    + ";boundary=" + BOUNDARY ) ;  

            // ������ƴ�ı����͵Ĳ���  
            StringBuilder sb = new StringBuilder ( ) ;  
            for ( Map.Entry < String , String > entry : params.entrySet ( ) )  
                {  
                    sb.append ( PREFIX ) ;  
                    sb.append ( BOUNDARY ) ;  
                    sb.append ( LINEND ) ;  
                    sb.append ( "Content-Disposition: form-data; name=\""  
                            + entry.getKey ( ) + "\"" + LINEND ) ;  
                    sb.append ( "Content-Type: text/plain; charset="  
                            + CHARSET + LINEND ) ;  
                    sb.append ( "Content-Transfer-Encoding: 8bit" + LINEND ) ;  
                    sb.append ( LINEND ) ;  
                    sb.append ( entry.getValue ( ) ) ;  
                    sb.append ( LINEND ) ;  
                }  

            DataOutputStream outStream = new DataOutputStream (  
                    conn.getOutputStream ( ) ) ;  
            outStream.write ( sb.toString ( ).getBytes ( ) ) ;  
            // �����ļ�����  
            if ( files != null )  
                for ( Map.Entry < String , File > file : files.entrySet ( ) )  
                    {  
                        StringBuilder sb1 = new StringBuilder ( ) ;  
                        sb1.append ( PREFIX ) ;  
                        sb1.append ( BOUNDARY ) ;  
                        sb1.append ( LINEND ) ;  
                        sb1.append ( "Content-Disposition: form-data; name=\"file\"; filename=\""  
                                + file.getKey ( ) + "\"" + LINEND ) ;  
                        sb1.append ( "Content-Type: application/octet-stream; charset="  
                                + CHARSET + LINEND ) ;  
                        sb1.append ( LINEND ) ;  
                        outStream.write ( sb1.toString ( ).getBytes ( ) ) ;  

                        InputStream is = new FileInputStream (  
                                file.getValue ( ) ) ;  
                        byte [ ] buffer = new byte [ 1024 ] ;  
                        int len = 0 ;  
                        while ( ( len = is.read ( buffer ) ) != - 1 )  
                            {  
                                outStream.write ( buffer , 0 , len ) ;  
                            }  

                        is.close ( ) ;  
                        outStream.write ( LINEND.getBytes ( ) ) ;  
                    }  

            // ���������־  
            byte [ ] end_data = ( PREFIX + BOUNDARY + PREFIX + LINEND )  
                    .getBytes ( ) ;  
            outStream.write ( end_data ) ;  
            outStream.flush ( ) ;  
            // �õ���Ӧ��  
            int res = conn.getResponseCode ( ) ;  
            InputStream in = conn.getInputStream ( ) ;  
            if ( res == 200 )  
                {  
                    int ch ;  
                    StringBuilder sb2 = new StringBuilder ( ) ;  
                    while ( ( ch = in.read ( ) ) != - 1 )  
                        {  
                            sb2.append ( ( char ) ch ) ;  
                        }  
                }  
            outStream.close ( ) ;  
            conn.disconnect ( ) ;  
            return in.toString ( ) ;  
              
        }
}
