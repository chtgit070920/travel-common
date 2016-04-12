<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<link  rel="stylesheet" href="<c:url value='/shared/css/public.css'/>"/>


<%--这里 打包时，请注掉--%>
<script type="text/javascript" src="<c:url value="/shared/js/jquery-1.8.3.min.js" />"></script>


<script type="text/javascript">

// 这里使用到了jquery,宿主页面请引入之.
$(document).ready(function() {

//  $(".menus .other-menus .menu").click(function(){
//    $(this).siblings(".clicked").children("div").removeClass("hide");
//    $(this).addClass("clicked").siblings().removeClass("clicked");
//  });

  $(".menus .other-menus .menu").hover(function(){
    $(this).siblings(".clicked").children("div").addClass("hide");//隐藏
    $(this).addClass("hovered").siblings().removeClass("hovered");
  },function(){
    $(this).siblings(".clicked").children("div").removeClass("hide");
    //$(this).removeClass("hovered").siblings().removeClass("hovered");
    $(this).parent().children().removeClass("hovered");
  } );


//  $(".menus .other-menus .menu div a").click(function(){
//    $(this).addClass("clicked").siblings().removeClass("clicked");
//  });

  $(".menus .other-menus .menu div a").hover(function(){
    $(this).addClass("hovered").siblings().removeClass("hovered");
  },function(){
    //$(this).removeClass("hovered").siblings().removeClass("hovered");
    $(this).parent().children().removeClass("hovered");
  } );


	
});

  function _selectMenu(i,j){
    var target;
    if(i)var target=$(".menus .other-menus").children().eq(i-1);//一级menu
    if(target){
      $(target).siblings(".clicked").children("div").removeClass("hide");//显示隐藏二级菜单框
      $(target).addClass("clicked").siblings().removeClass("clicked");//选中一级菜单
      if(j)$(target).children("div").children().eq(j-1).addClass("clicked").siblings().removeClass("clicked");//选中二级菜单
    }
  }
</script>

<%
  //
  StringBuffer sb=request.getRequestURL();
  String currentService=java.net.URLEncoder.encode(sb.toString(),"UTF-8");
  pageContext.setAttribute("currentService",currentService);

  

%>


<div class="public">

  <!-- title-->
  <div class="public-header public-header-first-background">
    <div class="public-1200">
      <div class="public-wrap">
        <div class="first-level">
          <c:choose>
            <c:when test="${sessionScope._const_cas_assertion_!=null && sessionScope._const_cas_assertion_.principal!=null&& sessionScope._const_cas_assertion_.principal.attributes!=null}">
              <a>欢迎你，${sessionScope._const_cas_assertion_.principal.attributes.name}</a>
            </c:when>
            <c:otherwise>
              <a href="${initParam.casServerUrlPrefix}/login?service=${currentService}">登录</a>
              <a href="${initParam.casServerUrlPrefix}/register?service=${currentService}">注册</a>
            </c:otherwise>
          </c:choose>
          <a href="">我的订单</a>
          <a href="" class="phone">手机版</a>
          <a href="">帮助中心</a>
          <a href="" class="weibo"></a>
          <a href="" class="wixin"></a>
        </div>
      </div>
    </div>
  </div>
  <!-- menus -->
  <div class="public-header public-header-second-background">
    <div class="public-1200">
      <div class="public-wrap">
        <div class="second-level">

          <!--400 phone-->
          <div class="phone-400">400-6600-955</div>

          <!--menus-->
          <div class="menus" id="b">
              <!--<div class="home">
                <a class="tab-link" href="">首 页</a>
              </div>-->
              <ul class="other-menus">

                <li class="menu">
                  <a href="javascript:void(0);" class="tab-link">首 页</a>
                </li>

                <li class="menu">
                  <a href="javascript:void(0);" class="tab-link">目的地</a>
                </li>

                <li class="menu">
                  <a class="tab-link" href="javascript:void(0);">景点门票<em class="up-down"></em></a>
                  <div>
                    <a href="javascript:void(0);" class="first-scenic clicked">门票首页</a>
                    <a href="javascript:void(0);" >周边游</a>
                    <a href="javascript:void(0);">省内景点</a>
                    <a href="javascript:void(0);">国内景点</a>
                  </div>
                </li>

                <li class="menu">
                  <a class="tab-link" href="javascript:void(0);">酒店<em class="up-down"></em></a>
                  <div>
                    <a href="javascript:void(0);" class="first-hotel clicked">酒店首页</a>
                    <a href="javascript:void(0);">省内酒店</a>
                    <a href="javascript:void(0);">国内酒店</a>
                  </div>
                </li>
                <li class="menu"><a href="javascript:void(0);" class="tab-link">美食</a> </li>
                <li class="menu"><a href="javascript:void(0);" class="tab-link">特产</a></li>
                <li class="menu"><a href="javascript:void(0);" class="tab-link">资讯</a></li>
                <li class="menu"><a href="javascript:void(0);" class="tab-link">攻略 </a></li>
              </ul>

            </div>
          </div>





        <!-- left backgroudn + logo-->
        <!--<div style="width: 100%; position:absolute; top:0">

          &lt;!&ndash;left backgroud&ndash;&gt;
          <div style="background: #a6dbf7; float: left; position: absolute; left: 0px; height: 82px; width:auto" class="aaa"></div>
          <script type="text/javascript">
            $(document).ready(function () {
              $(".aaa").css("width", (document.body.scrollWidth - "1200") / 2);
            });
          </script>

          &lt;!&ndash;logo&ndash;&gt;
          <div class="head_1000">
            <div id="logo" class="logo_show"><span href="#" class="mttlogo"><a href=""><img
                    src="images/logo_extension.png"></a></span></div>
          </div>
        </div>-->
      </div>



    </div>
  </div>

  <!--logo头部-->
  <div class="public-header" style="position: absolute;left: 0;top: 0;">
    <div class="public-1200" style="position: relative;">
      <div class="public-wrap" >
        <div class="public-logo"></div>
      </div>
   </div>
  </div>
</div>