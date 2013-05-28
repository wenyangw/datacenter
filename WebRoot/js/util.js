function showloading(message){   
    var loaddiv;   
   	if(loaddiv=document.getElementById("loaddiv")){   
        loaddiv.style.display = "block";   
    }else{   
        loaddiv = document.createElement("DIV");   
        loaddiv.id="loaddiv";   
        loaddiv.style.position = "absolute";   
        loaddiv.style.zIndex = 1000;   
        loaddiv.style.display="";   
        loaddiv.style.left = 0;   
        loaddiv.style.top  = 0;   
        loaddiv.style.border = "1px solid gray"  
        loaddiv.style.background = "#ffffff"  
        loaddiv.style.padding = "5";   
    }   
      
    var scrollTop=0;   
   	if(top.document.documentElement && top.document.documentElement.scrollTop){   
        scrollTop = top.document.documentElement.scrollTop;   
    }else if(document.body){   
        scrollTop = top.document.body.scrollTop;   
    }   
      
    var scrollWid = document.body.scrollWidth/2-50;   
      
    loaddiv.style.left = scrollWid+"px";   
    loaddiv.style.top   = (scrollTop + 250)+"px" ;   
      
    loaddiv.innerHTML = "<h>" + message + "</h>";   
    document.body.style.cursor="wait";     
      
      
  var sWidth,sHeight;   
  sWidth=document.body.scrollWidth;//浏览器工作区域内页面宽度 或使用 screen.width//屏幕的宽度   
  sHeight=document.body.scrollHeight;//屏幕高度（垂直分辨率）   
 
 
 //背景层（大小与窗口有效区域相同，即当弹出对话框时，背景显示为放射状透明灰色）   
  var bgObj=document.createElement("div");//创建一个div对象（背景层） //动态创建元素，这里创建的是 div   
 //定义div属性，即相当于(相当于，但确不是，必须对对象属性进行定义   
 //<div id="bgDiv" style="position:absolute; top:0; background-color:#777; filter:progid:DXImagesTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75); opacity:0.6; left:0; width:918px; height:768px; z-index:10000;"></div>   
  bgObj.setAttribute('id','bgDiv');   
  bgObj.style.position="absolute";   
  bgObj.style.top="0";   
  bgObj.style.background="#ffffff";   
  bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";   
  bgObj.style.opacity="0.6";   
  bgObj.style.left="0";   
  bgObj.style.width=sWidth + "px";   
  bgObj.style.height=sHeight + "px";   
  bgObj.style.zIndex = "10000";   
  document.body.appendChild(bgObj);//在body内添加该div对象   
      
  document.body.appendChild(loaddiv);   
}