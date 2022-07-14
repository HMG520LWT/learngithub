package kyber.wip_rework_station_control.sql.oracle;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import com.kyber.core.annotation.KyberComponent;
import com.kyber.core.annotation.SqlExecute;
import com.kyber.core.constants.SystemConstants;
import com.kyber.core.exception.MESException;
import com.kyber.core.sql.KyberSqlProxy;
import com.kyber.core.sql.SqlBean;
import com.kyber.core.util.CheckUtil;

import kyber.wip_rework_station.sql.action.ReworkQuerySqlAction;

// �̶�д��
@Profile(SystemConstants.DB_PROFILE_ORACLE)
@KyberComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class ReworkQuerySql extends KyberSqlProxy {
    
	// �����������õ������ǵĶ���
	@SqlExecute(ReworkQuerySqlAction.GET_REWORK_TABLE)
	public List<Map> getReworkQuerySql(String siteId, String lotId, String reworkCodeId, String stepName) throws MESException {
		StringBuffer sql = new StringBuffer();
		
        // ƴ���ַ���
		sql.append("select lot_id,");
		sql.append("product_name,");
		sql.append("product_type,");
		sql.append("product_ver,");
		sql.append("step_name,");
		sql.append("qty,");
		sql.append("equipment_id,");
		sql.append("rework_reason,");
		sql.append("rework_code_id,");
		sql.append("create_user,");
		sql.append("create_time,");
		sql.append("lm_user,");
		sql.append("lm_time");
		sql.append(" from R_REWORK_STATION where 1=1 ");
   
		
		
		
		/* 
		 * if (!CheckUtil.isNull(siteId)) { sql.append(" and site_id = ?"); }
		 */
		
		if (!CheckUtil.isNull(lotId)) {
			sql.append(" and lot_id like ?");
		}
		
		if (!CheckUtil.isNull(reworkCodeId)) {
			sql.append("and rework_code_id = ?");
		}
		
		if (!CheckUtil.isNull(stepName)) {
			sql.append("and step_name like ?");
		}
		
		
		// ��sql����sqlBeanִ��
		SqlBean sqlBean = sqlBeanExecuter.createSqlBean(sql.toString(), log);
		
		 // �ж��Ƿ�Ϊ�գ���Ϊnull���������ñ��������Ҫ��������жϸ�����˳�򱣳�һ�¡�	  
		  if(!CheckUtil.isNull(lotId)) { sqlBean.addParameter("%"+lotId+"%"); }
		  
		  if(!CheckUtil.isNull(reworkCodeId)) { sqlBean.addParameter(reworkCodeId); }
		  
		  if(!CheckUtil.isNull(stepName)) { sqlBean.addParameter("%"+stepName+"%"); }
		 
		// ����ִ�н��
		return sqlBean.executeQuery();
	}
}
