/**
 * 加载资源列表
 */
$(function(){
	$('#resList').datagrid({
		toolbar:"#toolButton",
		striped: true,
		fit: true,
		pagination:true,
		url: content+"/resource/list",
		idField:'id',
		singleSelect:false,
		pageNumber:1,
		pageSize:30,   
		fitColumns:true,
		nowrap:true,
		loadMsg:'数据加载中,请稍后……',
		
		frozenColumns:[[
		{field:'ck',checkbox:true},
		{title:'主键ID',field:'id',width:60,align:'center',sortable:true,hidden:true}     
		]],
		columns:[[
			{title:'资源名称',field:"resourceName",width:100,editor:false,align:"none"},
			{title:'资源标识',field:"label",width:180,editor:false,align:"none"},
			{title:'连接地址',field:"requestPath",width:180,editor:false,align:"none"},
			{title:'描述',field:"description",width:180,editor:false,align:"none"},
			{title:'排序',field:"sortNo",width:30,align:"center",
				editor:{
					type:'text'
				}
			}
		]],
			
		pagination:true,
		rownumbers:true,
		
		onBeforeEdit:function(index,row){
		row.editing = true;
		
		},
		onDblClickRow:function(index,row){
		    $('#resList').datagrid('beginEdit', index);
		},
		onClickRow:function(index,row){
		    $('#resList').datagrid('endEdit', index);
		}
	});
}); 


