package com.xxx.lfs.function;


import com.xxx.web.function.DataRow;
import com.xxx.web.function.RequestParameter;
import com.xxx.web.function.ResponseParameter;
import com.xxx.web.jdbc.DBConfigure;


import java.util.List;

/** 添加  */
public class F100006 extends BaseFunction   {

	@Override
	public ResponseParameter execute(RequestParameter requestParameter) throws Exception {
		String phone = requestParameter.getContent().get("phone");
		List<DataRow> list = query(phone);
		response.setList(list);
		return response;
	}

	private List<DataRow> query(String phone) throws Exception {

		String sql ="SELECT * FROM t_count where phone = ? ";
		List<DataRow> list = this.getNewJdbcTemplate().query(sql,new String[]{phone});
		return list;
	}
	public static void main(String arg[] ) throws Exception{

		new DBConfigure().loadConfig();
		F100006 f = new F100006();
		List<DataRow> list =  f.query("94e433225479");
		for(DataRow dataRow:list){
			System.out.println(dataRow);
		}
	}
}
