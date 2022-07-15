// 时间戳转换成日期格式
export function  getDate(val, format) { 
    if (!val) {
      return '';
    }
    // 如果时间戳后不是毫秒值就加000,变成毫秒值
    // if (val && val.length == 10) { val = parseInt(val+'000'); }
    if (val && val.length != 13) {
      if (val && val.length == 10) {
        val = parseInt(val+'000');
      };
      for (var i = 0; i < 13; i++) {
        if (val.length === i) {
          val = parseInt(val*(10^(13-i)));
        }
      }
    }
    var date = new Date(val);
    var o = {   
        "M+" : date.getMonth()+1,                 //月份   
        "d+" : date.getDate(),                    //日
        "h+" : date.getHours(),                   //小时   
        "m+" : date.getMinutes(),                 //分   
        "s+" : date.getSeconds(),                 //秒   
        "q+" : Math.floor((date.getMonth()+3)/3), //季度   
        "S"  : date.getMilliseconds()             //毫秒   
    };   
    if(/(y+)/.test(format)) {
      format = format.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }            
    for(var k in o) {
      if(new RegExp("("+ k +")").test(format)) {
        format = format.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
      }  
    }
    return format;   
  }