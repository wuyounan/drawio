<%@ page contentType="text/html;charset=utf-8"%>
<%@ page language="java" import="java.io.*,java.util.*" %>
<%@ page language="java" import="org.apache.commons.fileupload.*,org.apache.commons.fileupload.disk.*,org.apache.commons.fileupload.servlet.*"%>

<%!
public String tempFileDir = "D:\\uploadPath\\fileTemplate\\" ;
public String absoluteOfficeFileDir = "D:\\uploadPath\\postAttach\\";

public FileItem officeFileItem =null ;
public String officeFilePath ="";
public String officefileNameDisk = "";

public boolean saveFileToDisk()
{
	File officeFileUpload = null;
	boolean result=true ;
	try
	{
		System.out.println("*******************officefilepath:"+absoluteOfficeFileDir+officefileNameDisk);
		if(!"".equalsIgnoreCase(officefileNameDisk)&&officeFileItem!=null)
		{
			officeFileUpload =  new File(absoluteOfficeFileDir+officefileNameDisk);
			officeFileItem.write(officeFileUpload);
		}
	}
	catch(FileNotFoundException e){}
	catch(Exception e)
	{
		System.out.println("error saveFileToDisk:"+e.getMessage());
		e.printStackTrace();
		result=false;
	}
	return result;	
}
%>
<%
	String fileName = "" ;
	String fileType ="";
	String result = "";
	officeFileItem =null ;
	DiskFileItemFactory factory = new DiskFileItemFactory();
	// 设置最多只允许在内存中存储的数据,单位:字节
	factory.setSizeThreshold(4096);
	// 设置一旦文件大小超过setSizeThreshold()的值时数据存放在硬盘的目录
	factory.setRepository(new File(tempFileDir));
	ServletFileUpload upload = new ServletFileUpload(factory);
	//设置允许用户上传文件大小,单位:字节
	upload.setSizeMax(1024*1024*4);
	
	List fileItems = null;
	
	try{fileItems=upload.parseRequest(request);}
	catch(FileUploadException e)
	{
		out.println("the max upload size is 4m,cheeck upload file size!");
		out.println(e.getMessage());
		e.printStackTrace();
		return;
	}
	
	Iterator iter = fileItems.iterator();
	while (iter.hasNext()) 
	{
		FileItem item = (FileItem) iter.next();
		if(item.isFormField())
		{
			if(item.getFieldName().equalsIgnoreCase("fileName"))
			{
				fileName=item.getString("utf-8").trim();
			}
			if(item.getFieldName().equalsIgnoreCase("fileType"))
			{
				fileType=item.getString("utf-8").trim();
			}	
		}else
		{
			if(item.getFieldName().equalsIgnoreCase("upLoadFile"))
			{officeFileItem=item;}
		}
	}

	fileName= "1abcdef.doc";
	if(!"".equalsIgnoreCase(fileName)&&officeFileItem!=null)
	{
		//保存到磁盘中的文档为了防止重复,名字取为 ".officefile."
		officefileNameDisk = ".officefile."+fileName;
		if(saveFileToDisk())
		{
			result="文档保存成功。";
		}
		else
		{result="save file failed,please check upload file size,the max size is 4M";}
	}
	else{result="wrong information";}
	out.println(result);

%>