package edu.thu.component.des;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import edu.thu.bean.JSONResult;
import edu.thu.bean.User;
import edu.thu.icomponent.AbstractComponent;
import edu.thu.icomponent.ILoginComponent;
import edu.thu.util.CommonUtil;

/**
 * ��¼���ܲ���
 * 
 * @author hujiawei
 * 
 */
public class LoginComponent extends AbstractComponent implements ILoginComponent {

	@Override
	public void login(JSONResult xmlResult, HashMap<String, String> paramMap) {
		InitialContext context;
		try {
			context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup(CommonUtil.JNDI_PORTAL);
			Connection connection = dataSource.getConnection();
			String sql = "select * from portal.SYS_USER where userid='" + paramMap.get("userId") + "'";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				if (rs.getString("userpass").equalsIgnoreCase(paramMap.get("password"))) {
					onResultSucceed(xmlResult, "��¼�ɹ�",
							new User(rs.getString("userid"), rs.getString("username")).buildJsonContent());
				} else {
					onResultFail(xmlResult, "��¼ʧ��", null);
				}
			} else {
				onResultFail(xmlResult, "��¼ʧ��", null);
			}
			rs.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
			onResultException(xmlResult, "������߷���������", null);
		}
	}

}
