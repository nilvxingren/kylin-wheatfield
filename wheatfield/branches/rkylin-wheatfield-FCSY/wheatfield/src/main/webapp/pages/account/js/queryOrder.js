	function submitToQueryOrders(){
		selectFieldsOk();
		var field = $("body").data("field");
		var data = $("#inputForm").serialize();
		if (field!=""&& field!=undefined) {
			data = data + "&"+field;
		}
		 $.ajax({  
	         type: "POST",       
	         url: basePath+'queryOrder', 
	         dataType:'json',
	         data:data,
	         success: function(jsonObj){ 
	        	 console.log(JSON.stringify(jsonObj));
	        	 if (jsonObj.success=="true") {
					alert("成功！");
				}else{
					alert(jsonObj.errMsg);
				}
	 		}
	   }); 
	}
	
	//显示复选框的form
	function showCheckBox(){
		var selectFieldArray = $("body").data("selectArray");
		if (selectFieldArray!=undefined) {
			for (var i = 0; i < selectFieldArray.length; i++) {
				var value = selectFieldArray[i].value;
				$("input[value="+value+"]").attr("checked",true);
			}	
		}
		$("#fieldForm").show();
	}
	
	//点击完成选择后将选中的字段临时保存在页面
	function selectFieldsOk(){
		$("#fieldForm").hide();  
		var selectFields = $("#fieldForm").serialize();
		var selectFieldArray = $("#fieldForm").serializeArray();
		$("body").data("field",selectFields);
		$("body").data("selectArray",selectFieldArray);
	}
		
	
	//全选
	function allSelect(){
		$("input[type='checkbox']").each(function(i,content){
			$(content).attr("checked",true);
		});
	}
	
	//取消选中的
	function cancleSelect(){
		$("input[type='checkbox']").each(function(i,content){
			$(content).attr("checked",false);
		});
	}