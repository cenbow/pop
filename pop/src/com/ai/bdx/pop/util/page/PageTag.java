package com.ai.bdx.pop.util.page;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PageTag extends TagSupport{

	private static final long serialVersionUID = 8083881098226548636L;

	private static Logger log = LogManager.getLogger();
	
	 	private String pageIndex; 			//当前页码
	    private String functionName;        //js方法名称
	    private String totalPageCount; 		//总页数
	    private String totalCount; 			//总记录数
	    private String pageSize; 			//一页记录数 
	    private String name;				//分页对象(SplitPageBean)属性名 
	    private String linkPath;			//连接路径
	    private String key = "0";			//用户用来标识第几次使用
	  
	    
	    private String firstRecord;//记录开始位置
	    private String maxRecord;//记录结束位置
	    public int doStartTag() throws JspException {
	        JspWriter jspWriter = pageContext.getOut();
	        try { 
	            //判断跳转的路径中是否含有?符号
	            if (linkPath != null) {
	                if (linkPath.indexOf("?") < 0) {
	                    linkPath += "?";
	                } else {
	                    linkPath += "&";
	                }
	            }
	            //设置分页信息
	            try {
	                setPageInfo(pageContext);
	            } catch (Exception e) {
	                log.error("初始化分页信息出错：" + e.getMessage());
	                return SKIP_BODY;
	            }
	            //生成分页的html代码
	            jspWriter.println(genhtml());
	        } catch (Exception e) {
	            throw new JspTagException(e.getMessage());
	        }
	        return SKIP_BODY;
	    }

	    public int doEndTag() throws JspException {
	        return EVAL_PAGE;
	    }
	    
	    
	    /**
	     * 设置分页信息
	     *
	     * @exception JspException
	     * @exception NoSuchMethodException
	     * @exception IllegalAccessException
	     * @exception InvocationTargetException
	     */
	    private void setPageInfo(PageContext pageContext) throws JspException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
	    	if (name != null) {
	    		SplitPageBean o = (SplitPageBean)pageContext.getRequest().getAttribute(name);
	    		
	    		this.pageIndex = String.valueOf(o.getPageIndex());
	    		this.totalCount = String.valueOf(o.getTotalCount());
	    		this.pageSize = String.valueOf(o.getPageSize());
	    	    
	    		int _firstRecord=(o.getPageIndex() - 1) * o.getPageSize();
	    	    this.firstRecord =_firstRecord+1+"";
	    	    int _maxRecord=_firstRecord+o.getPageSize();
	    	    this.maxRecord=_maxRecord+"";
	    		if(_maxRecord>=o.getTotalCount()){
	    			this.maxRecord=o.getTotalCount()+"";
	    		}
				int t = Integer.parseInt(this.totalCount);
	            int p = Integer.parseInt(this.pageSize);
	            int r = 0;
	            if (t % p == 0) {
	                r = t / p;
	            } else {
	                r = t / p + 1;
	            }
	            this.totalPageCount = String.valueOf(r);
	        }else{
	        	
	        } 
	    }
     
	    private  String genhtml(){
	    	int c = Integer.parseInt(this.pageIndex);//当前页码
	        int t = Integer.parseInt(this.totalPageCount);//总页码
	        if(t<=0){
	        	return "";
	        }
	        
	    	String goPage="goPage"+getKey();
	    	
	    	StringBuffer b=new StringBuffer();
	    	b.append("<div class=\"pagination\" style=\"width: 100%;float: none;height: 30px;\">");
	    	b.append("<div style=\"display: inline-block;float: left;line-height: 30px;\">每页"+pageSize+"条,共<span style=\"margin: 0 2px;\">"+totalCount+"</span>条,当前第<strong  style=\"margin: 0 2px;color: #22878E;\">"+pageIndex+"</strong>页,共<span  style=\"margin: 0 2px;\">"+totalPageCount+"</span>页</div>");
	    	b.append("<ul style=\"float: right;\">");
	    	
	    	if(c<=1){
	    		b.append("<li class=\"disabled\"><a href=\"javascript:"+goPage+"('1');\"><<</a></li>");
	    		b.append("<li class=\"disabled\"><a href=\"javascript:"+goPage+"('1');\"><</a></li>");
	    	}else{
	    		int prePage=c-1;
	    		b.append("<li><a href=\"javascript:"+goPage+"('1');\"><<</a></li>");
	    		b.append("<li><a href=\"javascript:"+goPage+"('"+prePage+"');\"><</a></li>");
	    	}
	    	
	    	if(t>9){//当总页面大于9时，只显示用户选择页码附近的显示
	    		if(c<=3){
	    			for(int i=1;i<=4;i++){
		    			if(i==c){
		    				b.append("<li class=\"active\"><a href=\"javascript:"+goPage+"('"+i+"');\">"+i+"</a></li>");
		    			}else{
		    				//b.append("<a href=\"javascript:goPage"+getKey()+"("+i+")"+"\"> "+i+" </a>");
		    				b.append("<li ><a href=\"javascript:"+goPage+"('"+i+"');\">"+i+"</a></li>");
		    			}
		    		}
	    			b.append("...");
	    		}else if(3<c&&c<t-3){
	    			for(int i=c-1;i<=(c+1);i++){
		    			if(i==c){
		    				b.append("<li class=\"active\"><a href=\"javascript:"+goPage+"('"+i+"');\">"+i+"</a></li>");
		    			}else{
		    				b.append("<li ><a href=\"javascript:"+goPage+"('"+i+"');\">"+i+"</a></li>");
		    			}
		    		}
	    			b.append("...");
	    		}else{	    			
		    		for(int n=t-6;n<=t-3;n++){
		    			if(n==c){
		    				b.append("<li class=\"active\"><a href=\"javascript:"+goPage+"('"+n+"');\">"+n+"</a></li>");
		    			}else{
		    				b.append("<li ><a href=\"javascript:"+goPage+"('"+n+"');\">"+n+"</a></li>");
		    			}
		    		}
	    			//b.append("...");	
	    		}
	    		//把最后两页显示出来
	    		/*for(int m=t-2;m<=t;m++){
	    			if(m==c){
	    				b.append("<li class=\"active\"><a href=\"#\">"+m+"</a></li>");
	    			}else{
	    				b.append("<li ><a href=\"#\">"+m+"</a></li>");
	    			}
	    		}*/
	    	}else{//当总页面小于9时，页码全部列出
	    		for(int x=1;x<=t;x++){
	    			if(x==c){
	    				b.append("<li class=\"active\"><a href=\"javascript:"+goPage+"('"+x+"');\">"+x+"</a></li>");
	    			}else{
	    				b.append("<li ><a href=\"javascript:"+goPage+"('"+x+"');\">"+x+"</a></li>");
	    			}		    	
	    		}		    	
	    	}
	    	  
	    	  	
	    	if(c<t-1){
	    		int nextPage=c+1;
	    		b.append("<li><a href=\"javascript:"+goPage+"('"+nextPage+"');\">></a></li>");
	    		b.append("<li><a href=\"javascript:"+goPage+"('"+t+"');\">>></a></li>");
                
	    	}else{
	    		b.append("<li class=\"disabled\"><a href=\"javascript:"+goPage+"('"+t+"');\">></a></li>");
	    		b.append("<li class=\"disabled\"><a href=\"javascript:"+goPage+"('"+t+"');\">>></a></li>");
	    	}
	    	b.append("</ul>");
	    	b.append("</div>");
	    	
	    	StringBuffer strFunc=new StringBuffer("<script language=\"javascript\">\n");
	    	strFunc.append("function goPage").append(getKey()).append("(pageStr){\n if(pageStr<=0||pageStr>").append(getTotalPageCount()).append("){alert('页面越界');return;} \n");
	    	if (getLinkPath() == null) {
	    		if(getFunctionName()!=null){
		        	strFunc.append(getFunctionName()).append("(pageStr);");
		        }else{
		        	log.error("标签FunctionName不能为空");
		        }
	        }else{
	        	if(getFunctionName()!=null){
	    			strFunc.append(" var url='").append(getLinkPath()).append("pageNo='+pageStr;\n");
		        	strFunc.append(getFunctionName()).append("(url);");
		        }else{
		        	log.error("标签FunctionName不能为空");
		        }
	        }
	        strFunc.append("\n}\n </script>");
			return b.toString()+strFunc;
	    }
	    
	
	    public String getCurrentPage() {
	        return pageIndex;
	    }

	    public void setCurrentPage(String currentPage) {
	        this.pageIndex = currentPage;
	    }

	    public String getTotalPageCount() {
	        return totalPageCount;
	    }

	    public void setTotalPageCount(String totalPageCount) {
	        this.totalPageCount = totalPageCount;
	    }

	    public String getTotalRecordCount() {
	        return totalCount;
	    }

	    public void setTotalRecordCount(String totalRecordCount) {
	        this.totalCount = totalRecordCount;
	    }

	    public String getPageSize() {
	        return pageSize;
	    }

	    public void setPageSize(String recordCountOfPage) {
	        this.pageSize = recordCountOfPage;
	    } 
	 

	    public String getLinkPath() {

	        return linkPath;
	    }

	    public void setLinkPath(String linkPath) {
	        this.linkPath = linkPath;
	    }

	    public String getKey() {
	        return key;
	    }

	    public void setKey(String key) {
	        this.key = key;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

		public String getFunctionName() {
			return functionName;
		}

		public void setFunctionName(String functionName) {
			this.functionName = functionName;
		}

}
